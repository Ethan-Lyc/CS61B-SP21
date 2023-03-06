package gitlet;

import org.checkerframework.checker.units.qual.C;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static gitlet.Utils.*;

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
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File REFS_DIR = join(GITLET_DIR, "refs");
    /** The staging area. */
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");
    public static final File HEADS_DIR = join(REFS_DIR, "heads");
    public static final File HEAD_FILE = join(GITLET_DIR, "HEAD");
    public static final File REMOTES_DIR = join(REFS_DIR, "remotes");
    public static final File STAGE = join(GITLET_DIR, "stage");
    public static final File STAGING_DIR = join(GITLET_DIR, "staging");
    public static Commit currCommit;


    /* TODO: fill in the rest of this class. */
    public static void init(){
        if(GITLET_DIR.exists()){
            System.out.println("A gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        GITLET_DIR.mkdir();
        OBJECTS_DIR.mkdir();
        REFS_DIR.mkdir();
        HEADS_DIR.mkdir();

        initCommit();
        initHead();
        initHeads();
    }
    public static void initCommit(){
        Commit initCommit = new Commit();
        currCommit = initCommit;
        initCommit.save();
    }
    public static void initHead(){
        writeContents(HEAD_FILE, "master");
    }
    public static void initHeads(){
        File HEADS_FILE = join(HEADS_DIR, "master");
        writeContents(HEADS_FILE, currCommit.getId());
    }
    @Test
    public void testInit(){
        init();
    }
    public static void check(){
        if(!GITLET_DIR.exists()){
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }
    /**
     * 1. Staging an already-staged file overwrites the previous entry in the staging area with the new contents.
     * 2. If the current working version of the file is identical to the version in the current commit,
     * do not stage it to be added, and remove it from the staging area if it is already there
     * (as can happen when a file is changed, added, and then changed back to it’s original version).
     * 3. The file will no longer be staged for removal (see gitlet rm), if it was at the time of the command.
     *
     * @param fileName
     */
    public void add(String fileName){
        check();
        File file = getFileFromCWD(fileName);
        if(!file.exists()){
            System.out.println("File does not exist.");
            System.exit(0);
        }
        Commit head = getHead();
        Stage stage = readStage();
        String headId = head.getPathToBlobID().getOrDefault(file.getPath(),"");
        String stageId = stage.getAdded().getOrDefault(fileName, "");
        Blob blob = new Blob(file);
        String blobId = blob.getBlobID();
    }

    private Stage readStage() {
        return readObject(STAGE, Stage.class);
    }

    public void addToStage(Blob blob){


    }
    public File getFileFromCWD(String fileName){
        return join(CWD, fileName);
    }
    private String getHeadBranchName() {
        return readContentsAsString(HEAD_FILE);
    }
    private Commit getHead(){
        String headBranchName = getHeadBranchName();
        File branchFile = getBranchFile(headBranchName);
        Commit head = getCommitFromBranchFile(branchFile);
        return head;
    }

    private Commit getCommitFromBranchFile(File branchFile) {
        String commitId = readContentsAsString(branchFile);
        return getCommitFromId(commitId);
    }

    private Commit getCommitFromId(String commitId) {
        File commitFile = join(OBJECTS_DIR, commitId);
        if(commitId.equals("null") || !commitFile.exists()){
            return null;
        }
        return readObject(commitFile, Commit.class);
    }

    private File getBranchFile(String branchName) {
        return join(HEADS_DIR, branchName);
    }


}
