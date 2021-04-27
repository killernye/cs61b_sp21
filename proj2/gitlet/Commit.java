package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.util.*;

import static gitlet.Utils.*;
/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
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

    /** TimeStamp. */

    /** Parent1 commit. */
    String parent1;

    /** Potential Parent2 commit. */
    String parent2;

    /** mapping fileName =======> blob name. */
    Map<String, String> blobMap;

    /* TODO: fill in the rest of this class. */

    /** Creating a fresh Start Commit. */
    public Commit(String m) {
        message = m;
        parent1 = null;
        parent2 = null;
        blobMap = new HashMap<>();
    }

    /** Save a Commit. */
    public void saveCommit() {
        String commitName = commitName();
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

    /** Check if the content of this file the same with the commit version. */
    public boolean isIdentical(String name, String shaValue) {
        return shaValue.equals(blobMap.get(name));
    }
}
