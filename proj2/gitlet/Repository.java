package gitlet;


import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static gitlet.Utils.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Yicheng Liao
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public File CWD;

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
    public File GITLET_DIR;

    /**
     * The staging directory, restores staging Blobs
     */
    public File STAGING_DIR;

    /**
     * The Stage Object
     */
    public File STAGE;

    /**
     * The Objects directory, stores committed blobs & commits
     */
    public File BLOBS_DIR;
    public File COMMITS_DIR;

    /**
     * The branches directory
     */
    public File REFS_DIR;
    public File HEADS_DIR;
    public File REMOTES_DIR;
    /**
     * stores current branch's name if it points to tip
     */
    public File HEAD;
    // Note that in Gitlet, there is no way to be in a detached head state

    public File CONFIG;
    public void init(){
        //Failure cases
        if(GITLET_DIR.exists() && GITLET_DIR.isDirectory()){
            System.out.println("A Gitlet version-control system already exists in the current directory");
            System.exit(0);
        }
        //create directories
        GITLET_DIR.mkdir();
        STAGING_DIR.mkdir();
        writeObject(STAGE,new Stage());
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
        File master = join(HEADS_DIR,branchName);
        writeContents(master,id);

        //Create Head
        writeContents(HEAD,branchName);

        writeContents(CONFIG,"");

    }

    /**
     * 1. Staging an already-staged file overwrites the previous entry in the staging area with the new contents.
     * 2. If the current working version of the file is identical to the version in the current commit,
     * do not stage it to be added, and remove it from the staging area if it is already there
     * (as can happen when a file is changed, added, and then changed back to it’s original version).
     * 3. The file will no longer be staged for removal (see gitlet rm), if it was at the time of the command.
     *
     * @param filename
     */
    public void add(String filename){
        File file = join(CWD,filename);
        if(!file.exists()){
            System.out.println("File does not exist.");
            System.exit(0);
        }

        Commit head = getHead();
        Stage stage = readStage();

        String headId = head.getBlobs().getOrDefault(filename,"");
        String stageId = stage.getAdded().getOrDefault(filename,"");

        Blob blob = new Blob(filename,CWD);
        String blobId = blob.getId();

        if(blobId.equals(headId)){
            //no need to add the file
            //del the file from staging area
            join(STAGING_DIR,stageId).delete();
            stage.getAdded().remove(stageId);
            stage.getRemoved().remove(stageId);
            writeStage(stage);
        }else if(!blobId.equals(stageId)){
            //update staging area
            if(!stageId.equals("")){
                join(STAGING_DIR,stageId).delete();
            }

            writeObject(join(STAGING_DIR,blobId),blob);
            //change stage added files
            stage.addFile(filename,blobId);
            writeStage(stage);

        }
    }
    /*Saves a snapshot of tracked files in the current commit and staging area so they can
     be restored at a later time, creating a new commit. The commit is said to be tracking
      the saved files. By default, each commit’s snapshot of files will be exactly the same
       as its parent commit’s snapshot of files; it will keep versions of files exactly as
       they are, and not update them. A commit will only update the contents of files it is
       tracking that have been staged for addition at the time of commit, in which case the
       commit will now include the version of the file that was staged instead of the version
       it got from its parent. A commit will save and start tracking any files that were staged
       for addition but weren’t tracked by its parent. Finally, files tracked in the current commit
        may be untracked in the new commit as a result being staged for removal by the rm command (below).
        */
    public void commit(String message){
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
    public void rm(String fileName){
        File file = join(CWD,fileName);
        Commit commit = getHead();
        Stage stage = readStage();

        String stageId = stage.getAdded().getOrDefault(fileName,"");
        String headId = commit.getBlobs().getOrDefault(fileName,"");
        Blob blob = new Blob(fileName,CWD);
        String blobId = blob.getId();

        if(stageId == "" && headId == ""){
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
        // Unstage the file if it is currently staged for addition.
        //file is staged, remove it
        if(!stageId.equals("")){
            stage.getAdded().remove(fileName);
        }
        if(!headId.equals("")){
            stage.getRemoved().add(fileName);
        }
        if(blob.exists() && blobId.equals(headId)){
            //remove file from the working directory
            //if the user has not already done so
            restrictedDelete(file);
        }
        writeStage(stage);
    }
    public void log(){
        StringBuilder sb = new StringBuilder();
        Commit commit = getHead();
        while(commit != null){
            sb.append(commit.getCommitAsString());
            commit = getCommitFromId(commit.getFirstParentId());
        }
        System.out.println(sb);
    }
    public void global_log(){
        StringBuilder sb = new StringBuilder();
        List<String> filenames = plainFilenamesIn(COMMITS_DIR);
        for (String filename : filenames) {
            Commit commit = getCommitFromId(filename);
            sb.append(commit.getCommitAsString());
        }
        System.out.println(sb);
    }
    public void find(String message){
        StringBuilder sb = new StringBuilder();
        List<String> filenames = plainFilenamesIn(COMMITS_DIR);
        for (String filename : filenames) {
            Commit commit = getCommitFromId(filename);
            if(commit.getMessage().equals(message)){
                sb.append(commit.getID() + "\n");
            }
        }
        if(sb.length() == 0){
            System.out.println("Found no commit with that message." );
        }
        System.out.println(sb);
    }
    public void status(){
        StringBuilder sb = new StringBuilder();
        sb.append("=== Branches ===" + "\n");
        List<String> branches = plainFilenamesIn(HEADS_DIR);
        String headname = getHeadBranchName();
        for (String branch : branches) {
            if(branch.equals(headname)){
                sb.append("*" + branch + "\n");
            }else{
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

        System.out.println(sb);
    }

    private Commit getCommitFromId(String firstParentId) {
        File commitFile = join(COMMITS_DIR,firstParentId);
        if(firstParentId.equals("null") || !commitFile.exists()){
            return null;
        }
        Commit commit = readObject(commitFile, Commit.class);
        return commit;
    }

    private void commitWith(String message, List<Commit> parents) {
        Stage stage = readStage();
        //if no files have been staged, abort.
        if(stage.isEmpty()){
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        Commit commit = new Commit(message,parents,stage);
        clearstage(stage);
        writeCommitToFile(commit);

        String commitId = commit.getID();
        String branchName = getHeadBranchName();
        File branch = getBranchFile(branchName);
        writeContents(branch,commitId);
    }

    private File getBranchFile(String branchName) {
        return join(HEADS_DIR,branchName);
    }

    private void clearstage(Stage stage) {
        File[] files = STAGING_DIR.listFiles();
        if(files == null){
            return;
        }
        Path targetDir = BLOBS_DIR.toPath();
        for (File file : files) {
            Path source = file.toPath();
            try {
                Files.move(source,targetDir.resolve(source.getFileName()),REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        writeStage(new Stage());

    }

    private void writeStage(Stage stage) {
        writeObject(STAGE,stage);
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
        File branch = join(HEADS_DIR,branchName);
        String commitId = readContentsAsString(branch);
        Commit commit = readObject(join(COMMITS_DIR,commitId),Commit.class);
        return commit;
    }

    private String getHeadBranchName() {
        String branchName = readContentsAsString(HEAD);
        return branchName;
    }

    @Test
    public void test(){
/*        init();
        add("testforAdd");
        commit("testCommit");
        add("testforAdd-2");
        commit("test-Commit - 2");
        log();
        add("test -3");
        commit("testCommit");
        find("test");*/
        init();
        add("testforAdd");
        commit("testCommit");
        add("testforAdd-2");
        commit("test-Commit - 2");
        add("test -3");
        rm("testforAdd-2");
        status();

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

}
