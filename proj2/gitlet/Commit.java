package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static gitlet.Utils.sha1;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Yicheng Liao
 */
public class Commit {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    /** The time of this Commit. */
    private String timestamp;
    /** The parent of this Commit. */
    private List<String> parents;
    /** The blobs of this Commit. */
    private Map<String,String> blobs;
    /** The sha1 of this Commit. */
    private String Id;
    /** The File of this Commit. */
    private File file;


    public Commit(String message, Date timestamp, List<String> parents, Map<String,String> blobs, String[] files){
        this.message = message;
        this.timestamp = dateToTimeStamp(timestamp);
        this.parents = parents;
        this.blobs = blobs;
        this.Id = sha1(message + timestamp + parents + blobs + files);
        this.file = new File(".gitlet/objects/" + Id);
    }

    /* TODO: fill in the rest of this class. */
    public String getMessage(){
        return message;
    }
    public String getTimestamp(){
        return timestamp;
    }
    public List<String> getParents(){
        return parents;
    }
    public Map<String,String> getBlobs(){
        return blobs;
    }
    public String getId(){
        return Id;
    }
    public File getFile(){
        return file;
    }
    public void setMessage(String message){
        this.message = message;
    }
    private static String dateToTimeStamp(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
        return dateFormat.format(date);
    }
}
