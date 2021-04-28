package gitlet;


import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Utils.*;
/** Represents a gitlet commit object.
 *  A commit consist a version of the snapshot of all the tracked file.
 *
 *  @author NYE
 */
public class Commit implements Serializable {
    /**
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;

    /** TimeStamp. */
    Date date;

    /** Parent1 commit. */
    String parent1;

    /** Potential Parent2 commit. */
    String parent2;

    /** mapping fileName =======> blob name. */
    Map<String, String> blobMap;

    /** SHA-1 Value of this commit. */
    String name;

    /**
     * Constructors.
     */
    public Commit(String m, Date d, String p1, String p2) {
        message = m;
        date = d;
        parent1 = p1;
        parent2 = p2;
        blobMap = new HashMap<>();
        name = "haven't get a sha-1 name yet";
    }


    /**
     * List all the method below.
     */

    /** Read a Commit from file. */
    public static Commit readCommit(String inFile) {
        if (inFile == null) {
            return null;
        }

        File file = join(Repository.COMMITS_DIR, inFile);
        return readObject(file, Commit.class);
    }


    /** Save a Commit. */
    public void saveCommit() {
        String commitName = commitName();
        name  = commitName;
        File outFile = join(Repository.COMMITS_DIR, commitName);
        writeObject(outFile, this);
    }

    /** SHA-1 this Commit. */
    public String commitName() {

        List<String> lst = new LinkedList<>();
        lst.add(message);

        if (parent1 != null) {
                lst.add(parent1);
        }
        if (parent2 != null) {
            lst.add(parent2);
        }

        for (Map.Entry<String, String> fileBlobPair: blobMap.entrySet()) {
            lst.add(fileBlobPair.getKey());
            lst.add(fileBlobPair.getValue());
        }

        Object[] arr = lst.toArray();
        return sha1(arr);
    }

    /** Check if this commit contains a file. */
    public boolean containsFile(String file) {
        return blobMap.containsKey(file);
    }

    /** Returns the sha-1 name of the version of the file which this commit keeps. */
    public String getBlob(String fileName) {
        return blobMap.get(fileName);
    }


    /** Check if the content of this file the same with the commit version. */
    public boolean isIdentical(String name, String shaValue) {
        return shaValue.equals(blobMap.get(name));
    }

    @Override
    /** Print the nice log version of a commit. */
    public String toString() {
        StringBuffer sb = new StringBuffer("===\n");
        sb.append("commit ");
        sb.append(name);
        sb.append('\n');

        String pattern = "EEE, d MMM yyyy HH:mm:ss Z";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String a  = sdf.format(date);
        sb.append(a);
        sb.append('\n');

        sb.append(message);
        sb.append('\n');

        return sb.toString();
    }

    /** Clone from a parent commit and incorporate the changes from the stage area.
     *  Returns the sha1 value of new commit.
     */
    static String cloneAndChange(String parent, Map<String, String> stage, String m) {
        Commit p = readCommit(parent);
        Date d = new Date();
        Commit c = new Commit(m, d, parent, null);

        c.blobMap = p.blobMap;
        for (Map.Entry<String, String> pair: stage.entrySet()) {
            c.blobMap.put(pair.getKey(), pair.getValue());
        }

        c.saveCommit();
        return c.commitName();
    }

    /** Create a new Commit. */
    static String createCommit(String m, Date d, String p1, String p2) {
        Commit c = new Commit(m, d, p1, p2);
        String name = c.commitName();
        c.saveCommit();

        return name;
    }

}
