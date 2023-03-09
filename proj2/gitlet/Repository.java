package gitlet;










import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static gitlet.Utils.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

//

/** Represents a gitlet repository.
 *
 *  does at a high level.
 *
 *  @author Yicheng Liao
 */
public class Repository {
    /**
     *
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    File CWD;

    public Repository() {
        this.CWD = new File(System.getProperty("user.dir"));
        configDIRS();
    }

    /*    public Repository(String cwd) {
        this.CWD = new File(cwd);
        configDIRS();
    }*/

    private void configDIRS() {
        this.GITLET_DIR = join(CWD, ".gitlet");
        this.STAGING_DIR = join(GITLET_DIR, "staging");
        this.STAGE = join(GITLET_DIR, "stage");
        this.BLOBS_DIR = join(GITLET_DIR, "blobs");
        this.COMMITS_DIR = join(GITLET_DIR, "commits");
        this.REFS_DIR = join(GITLET_DIR, "refs");
        this.HEADS_DIR = join(REFS_DIR, "heads");
        this.REMOTES_DIR = join(REFS_DIR, "remotes");
        this.HEAD = join(GITLET_DIR, "HEAD");
        this.CONFIG = join(GITLET_DIR, "config");
    }

    /**
     * The .gitlet directory.
     * <p>
     * .gitlet
     * -- staging
     * -- [stage]
     * -- blobs
     * -- commits
     * -- refs
     *  -- heads -> [master][branch name]
     *  -- remotes
     *      -- [remote name] ->[branch name]
     * -- [HEAD]
     * -- [config]
     */
    File GITLET_DIR;

    /**
     * The staging directory, restores staging Blobs
     */
    File STAGING_DIR;

    /**
     * The Stage Object
     */
    File STAGE;

    /**
     * The Objects directory, stores committed blobs & commits
     */
    File BLOBS_DIR;
    File COMMITS_DIR;

    /**
     * The branches directory
     */
    File REFS_DIR;
    File HEADS_DIR;
    File REMOTES_DIR;
    /**
     * stores current branch's name if it points to tip
     */
    File HEAD;
    // Note that in Gitlet, there is no way to be in a detached head state

    File CONFIG;
    public void init() {
        //Failure cases
        if (GITLET_DIR.exists() && GITLET_DIR.isDirectory()) {
            System.out.println("A Gitlet version-control system"
                   + " already exists in the current directory");
            System.exit(0);
        }
        //create directories
        GITLET_DIR.mkdir();
        STAGING_DIR.mkdir();
        writeObject(STAGE, new Stage());
        BLOBS_DIR.mkdir();
        COMMITS_DIR.mkdir();
        REFS_DIR.mkdir();
        HEADS_DIR.mkdir();
        REMOTES_DIR.mkdir();

        //initial Commit
        Commit initialCommit = new Commit();
        writeCommitToFile(initialCommit);
        String id = initialCommit.getID();

        //create branch: master
        String branchName = "master";
        File master = join(HEADS_DIR, branchName);
        writeContents(master, id);

        //Create Head
        writeContents(HEAD, branchName);

        writeContents(CONFIG, "");

    }

    /**
     * 1. Staging an already-staged file overwrites the previous entry in
     * the staging area with the new contents.
     * 2. If the current working version of the file is identical to the
     * version in the current commit,
     * do not stage it to be added, and remove it from the staging area
     * if it is already there
     * (as can happen when a file is changed, added, and then changed back
     * to it’s original version).
     * 3. The file will no longer be staged for removal (see gitlet rm), if
     * it was at the time of the command.
     *
     */
    public void add(String filename) {
        File file = join(CWD, filename);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }

        Commit head = getHead();
        Stage stage = readStage();

        String headId = head.getBlobs().getOrDefault(filename, "");
        String stageId = stage.getAdded().getOrDefault(filename, "");

        Blob blob = new Blob(filename, CWD);
        String blobId = blob.getId();

        if (blobId.equals(headId)) {
            //no need to add the file
            //del the file from staging area
            join(STAGING_DIR, stageId).delete();
            stage.getAdded().remove(stageId);
            stage.getRemoved().remove(filename);
            writeStage(stage);
        } else if (!blobId.equals(stageId)) {
            //update staging area
            if (!stageId.equals("")) {
                join(STAGING_DIR, stageId).delete();
            }

            writeObject(join(STAGING_DIR, blobId), blob);
            //change stage added files
            stage.addFile(filename, blobId);
            writeStage(stage);

        }
    }
    public void commit(String message) {
        if (message.equals("")) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }

        Commit head = getHead();
        commitWith(message, List.of(head));
    }

    /*
    *Unstage the file if it is currently staged for addition.
    *If the file is tracked in the current commit, stage it
    *for removal and remove the file from the working directory
    *if the user has not already done so
    *(do not remove it unless it is tracked in the current commit).
     */
    public void rm(String fileName) {
        File file = join(CWD, fileName);
        Commit commit = getHead();
        Stage stage = readStage();

        String stageId = stage.getAdded().getOrDefault(fileName, "");
        String headId = commit.getBlobs().getOrDefault(fileName, "");
        Blob blob = new Blob(fileName, CWD);
        String blobId = blob.getId();

        if (stageId.equals("") && headId.equals("")) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
        // Unstage the file if it is currently staged for addition.
        //file is staged, remove it
        if (!stageId.equals("")) {
            stage.getAdded().remove(fileName);
        } else {
            stage.getRemoved().add(fileName);
        }
        if (blob.exists() && blobId.equals(headId)) {
            //remove file from the working directory
            //if the user has not already done so
            restrictedDelete(file);
        }
        writeStage(stage);
    }
    public void log() {
        StringBuilder sb = new StringBuilder();
        Commit commit = getHead();
        while (commit != null) {
            sb.append(commit.getCommitAsString());
            commit = getCommitFromId(commit.getFirstParentId());
        }
        System.out.println(sb);
    }
    public void globalLog() {
        StringBuilder sb = new StringBuilder();
        List<String> filenames = plainFilenamesIn(COMMITS_DIR);
        for (String filename : filenames) {
            Commit commit = getCommitFromId(filename);
            sb.append(commit.getCommitAsString());
        }
        System.out.println(sb);
    }
    public void find(String message) {
        StringBuilder sb = new StringBuilder();
        List<String> filenames = plainFilenamesIn(COMMITS_DIR);
        for (String filename : filenames) {
            Commit commit = getCommitFromId(filename);
            if (commit.getMessage().equals(message)) {
                sb.append(commit.getID() + "\n");
            }
        }
        if (sb.length() == 0) {
            System.out.println("Found no commit with that message.");
        }
        System.out.println(sb);
    }
    public void status() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Branches ===" + "\n");
        List<String> branches = plainFilenamesIn(HEADS_DIR);
        String headname = getHeadBranchName();
        for (String branch : branches) {
            if (branch.equals(headname)) {
                sb.append("*" + branch + "\n");
            } else {
                sb.append(branch + "\n");
            }
        }
        sb.append("\n");

        sb.append("=== Staged Files ===" + "\n");
        Stage stage = readStage();
        for (String s : stage.getAdded().keySet()) {
            sb.append(s + "\n");
        }
        sb.append("\n");

        sb.append("=== Removed Files ===" + "\n");
        for (String s : stage.getRemoved()) {
            sb.append(s + "\n");
        }
        sb.append("\n");

        sb.append("=== Modifications Not Staged For Commit ===" + "\n");
        sb.append("\n");

        sb.append("=== Untracked Files ===" + "\n");

        System.out.println(sb);
    }
    /**
     * java gitlet.Main checkout -- [file name]
     */
    public void checkoutFileFromHead(String fileName) {
        Commit head = getHead();
        checkoutFileFromCommit(head, fileName);
    }

    /*
    * java gitlet.Main checkout [commit id] -- [file name]
    */
    public void checkoutFileFromCommitId(String commitId, String fileName) {
        Commit commit = getCommitFromIdHelper(commitId);
        checkoutFileFromCommit(commit, fileName);
    }
    /*
    * java gitlet.Main checkout [branch name]
    */
    public void checkoutFromBranch(String branchName) {
        File branchFile = getBranchFile(branchName);
        if (branchFile == null || !branchFile.exists()) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }

        String headBranchName = getHeadBranchName();
        if (headBranchName.equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }

        Commit commit = getCommitFromBranchName(branchName);

        //If a working file is untracked in the current branch
        //and would be overwritten by the checkout
        validUntrackedFile(commit.getBlobs());

        //Any files that are tracked in the current branch but are
        // not present in the checked-out branch are deleted.
        clearstage(readStage());
        replaceWorkingPlaceWithCommit(commit);


        // change HEAD point to this branch
        writeContents(HEAD, branchName);
    }



    /*
     * Creates a new branch with the given name, and points it at
     * the current head commit. A branch is nothing more than a name
     *  for a reference (a SHA-1 identifier) to a commit node. This
     *   command does NOT immediately switch to the newly created
     *   branch (just as in real Git). Before you ever call branch,
     *   your code should be running with a default branch called
     *   “master”.
     * */
    public void createbranch(String branchName) {
        File branchFile = join(HEADS_DIR, branchName);

        if (branchFile.exists()) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        Commit headCommit = getHead();
        writeContents(branchFile, headCommit.getID());
    }

    public void removeBranch(String branchName) {
        File branchFile = join(HEADS_DIR, branchName);
        if (!branchFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        String currentBranch = readContentsAsString(HEAD);

        if (branchName.equals(currentBranch)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        removeBranchFile(branchName);


    }

    /* java gitlet.Main reset [commit id] */
    public void reset(String commitId) {
        Commit commit = getCommitFromIdHelper(commitId);

        //If a working file is untracked in the current branch
        //and would be overwritten by the checkout
        validUntrackedFile(commit.getBlobs());

        //Any files that are tracked in the current branch but are
        // not present in the checked-out branch are deleted.
        clearstage(readStage());
        replaceWorkingPlaceWithCommit(commit);

        String curHead = readContentsAsString(HEAD);
        File headfile = join(HEADS_DIR, curHead);
        writeContents(headfile, commit.getID());
    }


    public void merge(String branchName) {
        checkIfUncommitted();
        checkIfBranchExists(branchName);
        if (branchName.equals(getHeadBranchName())) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
        File otherBranchFile = getBranchFile(branchName);

        Commit head = getHead();
        Commit other = getCommitFromBranchName(branchName);
        Commit splitPoint = getLatestCommonAncestor(branchName);

        if (splitPoint.equals(other)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            System.exit(0);
        } else if (splitPoint.equals(head)) {
            checkoutFromBranch(branchName);
            System.out.println("Current branch fast-forwarded.");
            return;
        }

        //3.merge
        mergeWithLCA(splitPoint, head, other);

    }

    private void mergeWithLCA(Commit splitPoint, Commit head, Commit other) {
        Set<String> filenames = getAllFileNames(splitPoint, head, other);


    }

    private Set<String> getAllFileNames(Commit splitPoint, Commit head, Commit other) {
        Set<String> set = new HashSet<>();
        for (String s : splitPoint.getBlobs().keySet()) {
            set.add(s);
        }
        for (String s : head.getBlobs().keySet()) {
            set.add(s);
        }
        for (String s : other.getBlobs().keySet()) {
            set.add(s);
        }
        return set;
    }

    private Commit getLatestCommonAncestor(String branchName) {

        Commit head = getHead();
        Commit otherBranch = getCommitFromBranchName(branchName);

        HashMap<String, Integer> currAncestor = dfsCurrentHead(head);
        HashMap<String, Integer> otherAncestor = dfsCurrentHead(otherBranch);
        String key = new String();
        int depth = Integer.MAX_VALUE;
        for (String parent : currAncestor.keySet()) {
            if (otherAncestor.containsKey(parent)) {
                if (currAncestor.get(parent) < depth) {
                    depth = currAncestor.get(parent);
                    key = parent;
                }
            }
        }

        return getCommitFromId(key);


    }

    private HashMap<String, Integer> dfsCurrentHead(Commit commit) {
        HashMap<String, Integer> map = new HashMap<>();
        Commit head = commit;
        map.put(head.getID(), 0);
        int depth = 1;
        while (!head.getFirstParentId().equals("null")) {
            String parentCommitId = head.getFirstParentId();
            Commit parent = getCommitFromId(parentCommitId);
            map.put(parentCommitId, depth);
            depth += 1;
            head = parent;
        }
        return map;
    }

    private void checkIfBranchExists(String branchName) {
        File branchFile = getBranchFile(branchName);
        if (!branchFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
    }
    private void checkIfUncommitted() {
        Stage stage = readStage();
        if (!stage.isEmpty()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
    }

    private void removeBranchFile(String branchName) {
        File branchFile = join(HEADS_DIR, branchName);
        branchFile.delete();
    }

    private void replaceWorkingPlaceWithCommit(Commit commit) {
        File[] files = CWD.listFiles();
        for (File file : files) {
            file.delete();
        }
        HashMap<String, String> blobs = commit.getBlobs();
        for (String fileName : blobs.keySet()) {
            String blobId = blobs.get(fileName);
            File currentFile = join(CWD, fileName);
            Blob blob = getBlobFromBlobId(blobId);
            writeContents(currentFile, blob.getContent());
        }
    }




    /**
     * If a working file is untracked in the current branch
     * and would be overwritten by the blobs(checkout).
     */
    private void validUntrackedFile(HashMap<String, String> blobs) {
        List<String> untrackedFiles = getUntrackedFiles();
        if (untrackedFiles.isEmpty()) {
            return;
        }

        for (String untrackedFile : untrackedFiles) {
            String blobId = new Blob(untrackedFile, CWD).getId();
            String checkoutFileId = blobs.getOrDefault(untrackedFile, "");
            if (!blobId.equals(checkoutFileId)) {
                System.out.println("There is an untracked file in the way;"
                       + " delete it, or add and commit it first.");
                System.exit(0);
            }
        }
    }


    private List<String> getUntrackedFiles() {
        List<String> res = new ArrayList<>();
        List<String> stageFiles = readStage().getStagedFilename();
        Set<String> headFiles = getHead().getBlobs().keySet();
        for (String s : plainFilenamesIn(CWD)) {
            if (!stageFiles.contains(s) && !headFiles.contains(s)) {
                res.add(s);
            }
        }
        Collections.sort(res);
        return res;
    }

    private List<File> getCommitFiles(Commit commit) {
        HashMap<String, String> blobs = commit.getBlobs();
        List<File> files = new ArrayList<>();
        for (String fileName : blobs.keySet()) {
            String blobId = blobs.get(fileName);
            Blob blob = getBlobFromBlobId(blobId);
            File blobFile = join(CWD, fileName);
            writeObject(blobFile, blob);
            files.add(blobFile);
        }
        return files;
    }

    private Blob getBlobFromBlobId(String blobId) {
        File blobFile = join(BLOBS_DIR, blobId);
        Blob blob = readObject(blobFile, Blob.class);
        return blob;
    }


    private Commit getCommitFromIdHelper(String commitId) {
        Commit commit = getCommitFromId(commitId);
        if (commit == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        return commit;
    }


    private void checkoutFileFromCommit(Commit head, String filename) {
        String blobId = head.getBlobs().getOrDefault(filename, "");
        checkoutFileFromBlobId(blobId);
    }

    private void checkoutFileFromBlobId(String blobId) {
        if (blobId.equals("")) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        File blobFile = join(BLOBS_DIR, blobId);
        Blob blob = readObject(blobFile, Blob.class);
        checkoutFileFromBlob(blob);
    }

    private void checkoutFileFromBlob(Blob blob) {
        String fileName = blob.getFilename();
        File file = join(CWD, fileName);
        writeContents(file, blob.getContent());

    }


    private Commit getCommitFromId(String firstParentId) {
        File commitFile = join(COMMITS_DIR, firstParentId);
        if (!commitFile.exists() || firstParentId.equals("null")) {
            return null;
        }
        Commit commit = readObject(commitFile, Commit.class);
        return commit;
    }

    private void commitWith(String message, List<Commit> parents) {
        Stage stage = readStage();
        //if no files have been staged, abort.
        if (stage.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        Commit commit = new Commit(message, parents, stage);
        clearstage(stage);
        writeCommitToFile(commit);

        String commitId = commit.getID();
        String branchName = getHeadBranchName();
        File branch = getBranchFile(branchName);
        writeContents(branch, commitId);
    }

    private File getBranchFile(String branchName) {
        return join(HEADS_DIR, branchName);
    }

    private void clearstage(Stage stage) {
        File[] files = STAGING_DIR.listFiles();
        if (files == null) {
            return;
        }
        Path targetDir = BLOBS_DIR.toPath();
        for (File file : files) {
            Path source = file.toPath();
            try {
                Files.move(source, targetDir.resolve(source.getFileName()), REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        writeStage(new Stage());

    }

    private void writeStage(Stage stage) {
        writeObject(STAGE, stage);
    }


    private Stage readStage() {
        Stage stage = readObject(STAGE, Stage.class);
        return stage;
    }

    private Commit getHead() {
        String branchName = getHeadBranchName();
        Commit head = getCommitFromBranchName(branchName);
        return head;
    }

    private Commit getCommitFromBranchName(String branchName) {
        File branch = join(HEADS_DIR, branchName);
        String commitId = readContentsAsString(branch);
        Commit commit = readObject(join(COMMITS_DIR, commitId), Commit.class);
        return commit;
    }

    private String getHeadBranchName() {
        String branchName = readContentsAsString(HEAD);
        return branchName;
    }



    public void test() {
    /*        createbranch("second");
        add("test -3");
        commit("add test - 3");
        HashMap<String,Integer> mapMaster = dfsCurrentHead(getHead());
        HashMap<String,Integer> secondParents = dfsCurrentHead(getCommitFromBranchName("second"));
        for (String s : mapMaster.keySet()) {
            System.out.println("key == " + s + "depth = " + mapMaster.get(s));
        }
        System.out.println("\n");
        for (String s : secondParents.keySet()) {
            System.out.println("key == " + s + "depth = " + secondParents.get(s));
        }*/

    }

    private void writeCommitToFile(Commit commit) {
        File file = join(COMMITS_DIR, commit.getID());
        writeObject(file, commit);
    }
    void checkCommandLength(int actual, int expected) {
        if (actual != expected) {
            messageIncorrectOperands();
        }
    }
    void messageIncorrectOperands() {
        System.out.println("Incorrect operands.");
        System.exit(0);
    }

    public void checkIfInitDirectoryExists() {
        if (!GITLET_DIR.isDirectory()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }

    public void checkEqual(String arg, String s) {
        if (!arg.equals(s)) {
            messageIncorrectOperands();
        }
    }
}
