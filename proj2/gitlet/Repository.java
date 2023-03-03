package gitlet;

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
    public static final File BRANCHES = join(GITLET_DIR, "branches");
    public static final File OBJECTS = join(GITLET_DIR, "objects");

    /* TODO: fill in the rest of this class. */
    public static void init(){
        if(GITLET_DIR.exists()){
            System.out.println("A gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        GITLET_DIR.mkdir();
        OBJECTS.mkdir();
        BRANCHES.mkdir();
        Commit initialCommit = new Commit("initial commit", new Date(0),
                new ArrayList<>(), new HashMap<>(), null);
        File initialCommitFile = initialCommit.getFile();
        writeObject(initialCommitFile, Commit.class);
        File master = join(BRANCHES, "master");
        writeContents(master, sha1(initialCommit.toString()));
    }
    @Test
    public void testInit(){
        init();
    }
}
