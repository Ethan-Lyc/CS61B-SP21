package gitlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Stage implements Serializable {
    // <filename, blob's id>
    private HashMap<String, String> added;
    // <filename>
    private HashSet<String> removed;
    public Stage() {
        added = new HashMap<>();
        removed = new HashSet<>();
    }
    public void addFile(String filename, String blobID) {
        added.put(filename, blobID);
        removed.remove(filename);
    }
    public boolean isEmpty() {
        return added.isEmpty() && removed.isEmpty();
    }
    public void removeFile(String filename) {
        added.remove(filename);
        removed.add(filename);
    }
    public HashMap<String, String> getAdded() {
        return added;
    }
    public HashSet<String> getRemoved() {
        return removed;
    }
    public ArrayList<String> getStagedFilename() {
        ArrayList<String> res = new ArrayList<>();
        res.addAll(added.keySet());
        res.addAll(removed);
        return res;
    }
}
