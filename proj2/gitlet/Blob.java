package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Repository.OBJECTS_DIR;
import static gitlet.Utils.*;

public class Blob implements Serializable {
    private String id;

    private byte[] bytes;
    /* The Original File that in the working directory */
    private File fileName;

    private String filePath;
    /* The blobname of  that turn the original File in the directory into a blob*/
    private File blobSaveFileName;
    
    public Blob(File fileName) {
        this.fileName = fileName;
        this.bytes = Utils.readContents(fileName);
        this.filePath = fileName.getPath();
        this.id = generateID();
        this.blobSaveFileName = generateFileName();

    }
    public String getBlobID() {
        return id;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public String getPath() {
        return filePath;
    }

    public File getBlobSaveFileName() {
        return blobSaveFileName;
    }

    public String getFileName(){
        return fileName.getName();
    }

    private File generateFileName() {
        return Utils.join(OBJECTS_DIR, this.id);

    }

    private String generateID() {
        return Utils.sha1(this.bytes,filePath);
    }
    public void save() {
        writeObject(blobSaveFileName, this);
    }

}
