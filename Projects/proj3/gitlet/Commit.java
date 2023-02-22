package gitlet;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.io.Serializable;
import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;


public class Commit implements Serializable {

    /**
     * Commit message.
     */
    private final String _MSG;
    /**
     * Parents message.
     */
    private String _PMSG = null;
    /**
     * Commit ID (hash).
     */
    private final String _NAME;
    /**
     * Time commit was created.
     */
    private final String _TIME;
    /**
     * Staged files.
     */
    private final ArrayList<String> _ADDED;
    /**
     * Removed files.
     */
    private final ArrayList<String> _REMOVED;
    /**
     * Blobs of files.
     */
    private final TreeMap<String, String> _BLOBS;
    /**
     * Parent commit.
     */
    private final String _PARENT;
    /**
     * Parent commit 2.
     */
    private String _PARENT2 = null;

    public Commit() {
        _MSG = "initial commit";
        _TIME = "Thu Jan 1 00:00:00 1970 -0700";
        _PARENT = null;
        _ADDED = new ArrayList<>();
        _REMOVED = new ArrayList<>();
        _BLOBS = new TreeMap<>();
        _NAME = hashName();
        File x = Utils.join(".gitlet", "commits", _NAME);
        Utils.writeObject(x, this);
    }

    public Commit(String message, ArrayList<String> addedFiles,
                  ArrayList<String> removedFiles, TreeMap<String,
            String> blobMap, String parentName) {
        ZonedDateTime now = ZonedDateTime.now();
        _MSG = message;
        _PARENT = parentName;
        _ADDED = addedFiles;
        _REMOVED = removedFiles;
        _BLOBS = blobMap;
        _TIME = now.format(DateTimeFormatter.
                ofPattern("EEE MMM d kk:mm:ss uuuu xxxx"));
        _NAME = hashName();
        File x = Utils.join(".gitlet", "commits", _NAME);
        Utils.writeObject(x, this);
    }

    public Commit(String message, String pmessage, ArrayList<String> addedFiles,
                  ArrayList<String> removedFiles, TreeMap<String,
            String> blobMap, String parentName, String parentName2) {
        ZonedDateTime now = ZonedDateTime.now();
        _MSG = message;
        _PMSG = pmessage;
        _PARENT = parentName;
        _PARENT2 = parentName2;
        _ADDED = addedFiles;
        _REMOVED = removedFiles;
        _BLOBS = blobMap;
        _TIME = now.format(DateTimeFormatter.
                ofPattern("EEE MMM d kk:mm:ss uuuu xxxx"));
        _NAME = hashName();
        File x = Utils.join(".gitlet", "commits", _NAME);
        Utils.writeObject(x, this);
    }

    public String getID() {
        return this._NAME;
    }

    public String getPmsg() {
        return this._PMSG;
    }

    public String getMsg() {
        return this._MSG;
    }

    public String getParent() {
        return this._PARENT;
    }

    public String getParent2() {
        return this._PARENT2;
    }

    public String getTime() {
        return this._TIME;
    }

    public TreeMap<String, String> getBlobs() {
        return this._BLOBS;
    }

    public ArrayList<String> getAdded() {
        return this._ADDED;
    }

    public ArrayList<String> getRemoved() {
        return this._REMOVED;
    }

    public String hashName() {
        byte[] s = Utils.serialize(this);
        return Utils.sha1(s);
    }

}
