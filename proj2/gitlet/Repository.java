package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static gitlet.Utils.*;



/** Represents a gitlet repository.
 *  The real Boss of this program.
 *
 *  @author NYE
 */
public class Repository implements Serializable {
    /**
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The tip commits of every branch we store */
    Map<String, String> branches;

    /** Staging Area. */
    Map<String, String> staged;

    /** Head Pointer which points the branch we are currently working with. */
    String head;


    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File COMMITS_DIR = join(GITLET_DIR, "commits");
    public static final File BlOBS_DIR = join(GITLET_DIR, "blobs");
    public static final File REPO = join(GITLET_DIR, "repo");


    /**
     * Repo Constructor.
     */
    public Repository() {
        head = "master";

        String message = "init commit";
        Date date = new Date(0);
        String commit = Commit.createCommit(message, date, null, null);

        branches = new HashMap<>();
        branches.put("master", commit);

        staged = new HashMap<>();
    }



    /**
     * List ALL the Repository OPERATION below!
     * List ALL the Repository OPERATION below!
     */

    /** Initiate a Repository. */
    public static void init() {
        // check if a Dir already exists
        if (isInit()) {
            Utils.message("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }

        // setUP DIR
        GITLET_DIR.mkdir();
        COMMITS_DIR.mkdir();
        BlOBS_DIR.mkdir();
        writeContents(REPO);


        // Create a repo using factory method
        Repository repo = createRepo();

        // Save a repo
        saveRepo(repo);
    }

    /**
     * Add a file to the staging area.
     */
    public static void add(String fileName) {
        // Check if init
        if (!isInit()) {
            Utils.message("Not in an initialized Gitlet directory.");
            System.exit(0);
        }

        Repository repo = readRepo();
        File inFile = Utils.join(CWD, fileName);
        if (!inFile.exists()) {
            Utils.message("File does not exist.");
            System.exit(0);
        }

        String fileSha1Value = sha1File(inFile);

        // check if the file tracked before
        File fileBlobDir = Utils.join(BlOBS_DIR, fileName);
        Commit head = headPtr(repo);

        if (!fileBlobDir.exists()) {
            fileBlobDir.mkdir();
        }

        if (!head.containsFile(fileName)) {
            staging(repo, inFile);
        } else {
            if (!head.isIdentical(fileName, fileSha1Value)) {
                staging(repo, inFile);
            } else {
                if (isStaged(repo, fileName)) {
                    offStage(repo, fileName);
                }
            }
        }

        saveRepo(repo);

    }

    /**
     * Snapshot the staging area making a commit.
     */
    public static void commit(String message) {
        // TODO check if init
        if (!isInit()) {
            Utils.message("Not in an initialized Gitlet directory.");
            System.exit(0);
        }

        Repository repo = readRepo();
        // TODO Check if staged is empty
        if (repo.staged.isEmpty()) {
            Utils.message("No changes added to the commit.");
            System.exit(0);
        }

        if (message.equals("")) {
            Utils.message("Please enter a commit message.");
            System.exit(0);
        }

        // TODO copy HEAD commit and change the file in the staged
        String head = repo.branches.get(repo.head);
        String commit = Commit.cloneAndChange(head, repo.staged, message);
        repo.staged.clear();


        // TODO change repo metadata
        repo.branches.put(repo.head, commit);
        saveRepo(repo);
    }

    /**
     * List the commit log of this repo.
     */
    public static void log() {
        if (!isInit()) {
            Utils.message("Not in an initialized Gitlet directory.");
            System.exit(0);
        }

        Repository repo = readRepo();

        // Iterate through current commits list.
        Commit current = headPtr(repo);
        while (current != null) {
            System.out.println(current);
            current = Commit.readCommit(current.parent1);
        }
    }

    /**
     * Restore specified file to the older version.
     */
    public static void checkout(String fileName, String commitId) {
        //TODO
    }

    /**
     * Show the status of this repo.
     */
    public static void status() {
        if (!isInit()) {
            Utils.message("Not in an initialized Gitlet directory.");
            System.exit(0);
        }

        Repository repo = readRepo();
        Set<String> stage = repo.staged.keySet();
        System.out.println(stage);

    }


    /**
     *  List ALL the Helper FUNCTION below.
     *  List ALL the Helper FUNCTION below.
     */

    /** Create a Repo. */
    private static Repository createRepo() {
        return new Repository();
    }

    /** Save a Repo. */
    private static void saveRepo(Repository repo) {
        Utils.writeObject(REPO, repo);
    }

    /** Read a Repo. */
    private static Repository readRepo() {
        return Utils.readObject(REPO, Repository.class);
    }

    /** Return head Ptr of a repo. */
    private static Commit headPtr(Repository repo) {
        String head = repo.branches.get(repo.head);
        return Commit.readCommit(head);
    }

    /** Get Sha-1 Value of a file. */
    private static String sha1File(File file) {
        byte[] contents = Utils.readContents(file);
        return Utils.sha1(contents);
    }

    /** add a pair of file and its sha1 value to stage area. */
    private static void staging(Repository repo, File infile) {
        byte[] contents = Utils.readContents(infile);
        String sha1 = Utils.sha1(contents);
        String fileName = infile.getName();

        File outFile = Utils.join(BlOBS_DIR, fileName, sha1);
        Utils.writeContents(outFile, contents);

        repo.staged.put(fileName, sha1);
    }

    /** Remove a file from staging area. */
    private static void offStage(Repository repo, String fileName) {
        if (!isStaged(repo, fileName)) {
            Utils.message("File is not on the stage.");
            System.exit(0);
        }

        repo.staged.remove(fileName);
    }

    /** Check if a file in the staging area. */
    private static boolean isStaged(Repository repo, String fileName) {
        return repo.staged.containsKey(fileName);
    }

    /** Check if a file identical to the version on the stage. */
    private static boolean isIdentical(Repository repo, String name, String sha1) {
        return sha1.equals(repo.staged.get(name));
    }

    /**
    /** check if in an initialized Gitlet Directory. */
    static boolean isInit() {
        return GITLET_DIR.exists();
    }
}
