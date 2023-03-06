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
 *  @author TODO
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
    public static final File ADDSTAGE_FILE = join(GITLET_DIR, "add_stage");
    public static final File REMOVESTAGE_FILE = join(GITLET_DIR, "remove_stage");
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
    public static void add(String fileName){
        check();
        File file = getFileFromCWD(fileName);
        if(!file.exists()){
            System.out.println("File does not exist.");
            System.exit(0);
        }
        Blob blob = new Blob(file);
        addToStage(blob);
    }
    public static void addToStage(Blob blob){

    }
    public static File getFileFromCWD(String fileName){
        return join(CWD, fileName);
    }
}
