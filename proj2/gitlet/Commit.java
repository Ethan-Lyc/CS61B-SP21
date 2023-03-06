package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import static gitlet.Utils.join;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Yicheng Liao
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;

    private Map<String, String> pathToBlobID = new HashMap<>();

    private List<String> parents;

    private Date currentTime;

    private String id;

    private File commitSaveFileName;

    private String timeStamp;

    public Commit(String message, Map<String, String> pathToBlobID, List<String> parents) {
        this.message = message;
        this.pathToBlobID = pathToBlobID;
        this.parents = parents;
        this.currentTime = new Date();
        this.timeStamp = dateToTimeStamp(this.currentTime);
        this.id = generateID();
        this.commitSaveFileName = generateFileName();
    }

    public Commit() {
        this.currentTime = new Date(0);
        this.timeStamp = dateToTimeStamp(this.currentTime);
        this.message = "initial commit";
        this.pathToBlobID = new HashMap<>();
        this.parents = new ArrayList<>();
        this.id = generateID();
        this.commitSaveFileName = generateFileName();
    }

    private static String dateToTimeStamp(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
        return dateFormat.format(date);
    }
    private String generateID() {
        return Utils.sha1(generateTimeStamp(), message, parents.toString(), pathToBlobID.toString());
    }
    private String generateTimeStamp() {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.CHINA);
        return dateFormat.format(currentTime);
    }
    private File generateFileName() {
        return join(Repository.OBJECTS_DIR, id);
    }
    public void save(){
        Utils.writeObject(commitSaveFileName, this);
    }
    public String getMessage() {
        return message;
    }
    public Map<String, String> getPathToBlobID() {
        return pathToBlobID;
    }
    public List<String> getParents() {
        return parents;
    }
    public Date getCurrentTime() {
        return currentTime;
    }
    public String getId() {
        return id;
    }
    public File getCommitSaveFileName() {
        return commitSaveFileName;
    }
    public String getTimeStamp() {
        return timeStamp;
    }
}
