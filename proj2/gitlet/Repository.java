package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

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
    Map<String, String> staged; //TODO CHANGE staged to added
    Set<String> removed;

    /** Head Pointer which points to the branch we are currently working with. */
    String head;

    // TODO access modifier revise to package-private
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

        String message = "initial commit";
        Date date = new Date(0);
        String commit = Commit.createCommit(message, date, null, null);

        branches = new TreeMap<>();
        branches.put("master", commit);

        staged = new TreeMap<>();
        removed = new TreeSet<>();
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
     * Unstage the File if it is in the added.
     * Stage for removal if it's in the last commit and delete from working directory.
     */
    public static void rm(String fileName) {
        if (!isInit()) {
            Utils.message("Not in an initialized Gitlet directory.");
            System.exit(0);
        }

        Repository repo = readRepo();

        File inFile = Utils.join(CWD, fileName);

        if (!isStaged(repo, fileName) && !headPtr(repo).containsFile(fileName)) {
            Utils.message("No reason to remove the file.");
            System.exit(0);
        }

        if (isStaged(repo, fileName)) {
            offStage(repo, fileName);
        }

        if (headPtr(repo).containsFile(fileName)) {
            repo.removed.add(fileName);
            if (inFile.exists()) {
                Utils.restrictedDelete(inFile);
            }
        }
    }


    /**
     * Snapshot the staging area making a commit.
     */
    public static void commit(String message, String parent2) {
        // check if init
        if (!isInit()) {
            Utils.message("Not in an initialized Gitlet directory.");
            System.exit(0);
        }


        Repository repo = readRepo();
        // Check if staged is empty
        if (repo.staged.isEmpty() && repo.removed.isEmpty()) {
            Utils.message("No changes added to the commit.");
            System.exit(0);
        }

        if (message.equals("")) {
            Utils.message("Please enter a commit message.");
            System.exit(0);
        }

        // copy HEAD commit and change the file in the staged
        String current = repo.branches.get(repo.head);
        String commit = Commit.cloneAndChange(current, repo.staged, repo.removed, message, parent2);
        repo.staged.clear();
        repo.removed.clear();


        // change repo metadata
        repo.branches.put(repo.head, commit);
        //TODO repo.branches.put(branch, commit);  这里API不适合，PARENT2对应的分支也要更新
        saveRepo(repo);
    }

    /**
     * List the commit log starting from head commit.
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
     * List all the commits ever made in this repo.
     */
    public static void globalLog() {
        if (!isInit()) {
            Utils.message("Not in an initialized Gitlet directory.");
            System.exit(0);
        }

        // Read all the logs
        List<String> commits = Utils.plainFilenamesIn(COMMITS_DIR);
        for (String id: commits) {
            Commit c = Commit.readCommit(id);
            System.out.println(c);
        }
    }

    /**
     * Prints out the ids of all commits that have the given commit message, one per line.
     */
    public static void find(String message) {
        if (!isInit()) {
            Utils.message("Not in an initialized Gitlet directory.");
            System.exit(0);
        }

        List<String> commits = Utils.plainFilenamesIn(COMMITS_DIR);
        Boolean flag = false;

        for (String fileName: commits) {
            Commit c = Commit.readCommit(fileName);
            if (c.message.equals(message)) {
                flag = true;
                System.out.println(c.name);
            }
        }

        if (!flag) {
            Utils.message("Found no commit with that message.");
            System.exit(0);
        }
    }

    /**
     * Restore specified file to the older version.
     */
    public static void checkout(String fileName, String commitId) {
        if (!isInit()) {
            Utils.message("Not in an initialized Gitlet directory.");
            System.exit(0);
        }

        Repository repo = readRepo();
        Commit c = null;
        if (commitId.equals("head")) {
            c = headPtr(repo);
        } else if (commitId.length() >= 40){
            c = Commit.readCommit(commitId);
        } else {
            List<String> commits = Utils.plainFilenamesIn(COMMITS_DIR);
            for (String id: commits) {
                if (id.startsWith(commitId)) {
                    c = Commit.readCommit(id);
                    break;
                }
            }
            if (c == null) {
                Utils.message("No commit with that id exists.");
                System.exit(0);
            }
        }

        c.checkout(fileName);
    }

    /**
     * Checkout a given branch.
     */
    public static void checkout(String branch) {
        if (!isInit()) {
            Utils.message("Not in an initialized Gitlet directory.");
            System.exit(0);
        }

        Repository repo = readRepo();

        if (!repo.branches.containsKey(branch)) {
            Utils.message("No such branch exists.");
            System.exit(0);
        }

        if (branch.equals(repo.head)) {
            Utils.message("No need to checkout the current branch.");
            System.exit(0);
        }

        if (repo.containsUntrackedFile()) {
            Utils.message("There is an untracked file in the way; delete it, or add and commit it first.");
            System.exit(0);
        }



        //  Restore working directory from the given branch commit
        //  Delete the files not tracked by the given branch
        Commit cur = headPtr(repo);
        Set<String> oldFiles = cur.files();

        // TODO: Write a untrackedFiles() method
        // List<String> workingFile = Utils.plainFilenamesIn(CWD);
        // delete all the files in (workingFile - untrackedFiles)


        repo.head = branch;
        repo.staged.clear();
        repo.removed.clear();
        Commit current = headPtr(repo);


        for (String fileName: oldFiles) {
            if (!current.containsFile(fileName)) {
                File file = Utils.join(CWD, fileName);
                Utils.restrictedDelete(file);
            }
        }
        current.checkout();

        saveRepo(repo);
    }

    /**
     * Checks out all the files tracked by the given commit.
     */
    public static void reset(String commitId) {
        if (!isInit()) {
            Utils.message("Not in an initialized Gitlet directory.");
            System.exit(0);
        }

        Repository repo = readRepo();
        String id = isValidCommitID(commitId);
        if (id == null) {
            Utils.message("No commit with that id exists.");
            System.exit(0);
        }

        if (repo.containsUntrackedFile()) {
            Utils.message("There is an untracked file in the way; delete it, or add and commit it first.");
            System.exit(0);
        }


        repo.staged.clear();
        repo.removed.clear();
        repo.branches.put(repo.head, id);

        Commit current = Commit.readCommit(id);
        //  Restore working directory from the given branch commit
        //  Delete the files not tracked by the given branch
        List<String> workingFile = Utils.plainFilenamesIn(CWD);
        for (String fileName: workingFile) { //TODO 这一段是错的啊，可以参照CHECKOUT来修改，untracked files shouldn't be delete
            if (!current.containsFile(fileName)) {
                File file = Utils.join(CWD, fileName);
                Utils.restrictedDelete(file);
            } else {
                current.checkout(fileName);
            }
        }

        saveRepo(repo);
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

        System.out.println(repo);
    }

    /**
     * Creates a new branch with the given name, and points it at the current head commit.
     */
    public static void branch(String branchName) {
        if (!isInit()) {
            Utils.message("Not in an initialized Gitlet directory.");
            System.exit(0);
        }

        Repository repo = readRepo();
        if (repo.branches.containsKey(branchName)) {
            Utils.message("A branch with that name already exists.");
            System.exit(0);
        }

        String head = repo.branches.get(repo.head);
        repo.branches.put(branchName, head);

        saveRepo(repo);
    }

    /**
     * Deletes the branch with the given name.
     */
    public static void rmBranch(String branchName) {
        if (!isInit()) {
            Utils.message("Not in an initialized Gitlet directory.");
            System.exit(0);
        }

        Repository repo = readRepo();
        if (!repo.branches.containsKey(branchName)) {
            Utils.message("A branch with that name does not exist.");
            System.exit(0);
        }

        if (branchName.equals(repo.head)) {
            Utils.message("Cannot remove the current branch.");
            System.exit(0);
        }

        repo.branches.remove(branchName);
        saveRepo(repo);
    }

    /** Merge Current branch to the given branch. */
    //TODO if-else 语句太复杂了，没有注释，后来者根本没法看懂
    public static void merge(String other) {
        if (!isInit()) {
            Utils.message("Not in an initialized Gitlet directory.");
            System.exit(0);
        }

        Repository repo = readRepo();
        if (!repo.staged.isEmpty() || !repo.removed.isEmpty()) {
            Utils.message("You have uncommitted changes.");
            System.exit(0);
        }

        if (!repo.branches.containsKey(other)) {
            Utils.message("A branch with that name does not exist.");
            System.exit(0);
        }

        if (other.equals(repo.branches.get(repo.head))) {
            Utils.message("Cannot merge a branch with itself.");
            System.exit(0);
        }

        if (repo.containsUntrackedFile()) {
            Utils.message("There is an untracked file in the way; delete it, or add and commit it first.");
            System.exit(0);
        }

        String headId = repo.branches.get(repo.head);
        String otherId = repo.branches.get(other);

        String split = Commit.splitId(headId, otherId);

        if (split.equals(otherId)) {
            Utils.message("Given branch is an ancestor of the current branch.");
            System.exit(0);
        }

        if (split.equals(headId)) {
            checkout(other);
            Utils.message("Current branch fast-forwarded.");
            System.exit(0);
        }

        Set<String> allFiles = new HashSet<>();
        Commit firstC = Commit.readCommit(headId);
        Commit secondC = Commit.readCommit(otherId);
        Commit splitC = Commit.readCommit(split);

        Set<String> firstFiles = firstC.files();
        Set<String> secondFiles = secondC.files();
        Set<String> splitFiles = splitC.files();

        allFiles.addAll(firstFiles);
        allFiles.addAll(secondFiles);
        allFiles.addAll(splitFiles);

        String message = "Merged " + other + " into " + repo.head +".";

        for (String fileName: allFiles) {
            if (!splitC.containsFile(fileName)) {
                if (firstC.containsFile(fileName) && secondC.containsFile(fileName)) {
                    if (Commit.isModified(fileName, firstC, secondC)) {
                        String res = Commit.conflict(fileName, firstC, secondC);
                        repo.staged.put(fileName, res);
                        writeFile(fileName, res);
                        System.out.println("Encountered a merge conflict.");
                    }
                } else if (!firstC.containsFile(fileName)) {
                    String blob = secondC.getBlob(fileName);
                    writeFile(fileName, blob);
                    repo.staged.put(fileName, blob);
                }
            } else {
                if (firstC.containsFile(fileName) && secondC.containsFile(fileName)) {
                    if (Commit.isModified(fileName, splitC, firstC) && Commit.isModified(fileName, splitC, secondC)) {
                        if (Commit.isModified(fileName, firstC, secondC)) {
                            String res = Commit.conflict(fileName, firstC, secondC);
                            repo.staged.put(fileName, res);
                            writeFile(fileName, res);
                            System.out.println("Encountered a merge conflict.");
                        }
                    } else if (Commit.isModified(fileName, splitC, secondC)) {
                        String blob = secondC.getBlob(fileName);
                        writeFile(fileName, blob);
                        repo.staged.put(fileName, blob);
                    }
                } else if (firstC.containsFile(fileName)) {
                    if (Commit.isModified(fileName, splitC, firstC)) {
                        String res = Commit.conflict(fileName, firstC, secondC);
                        repo.staged.put(fileName, res);
                        writeFile(fileName, res);
                        System.out.println("Encountered a merge conflict.");
                    } else {
                        repo.removed.add(fileName);
                    }
                } else if (secondC.containsFile(fileName)) {
                    if (Commit.isModified(fileName, splitC, secondC)) {
                        String res = Commit.conflict(fileName, firstC, secondC);
                        repo.staged.put(fileName, res);
                        writeFile(fileName, res);
                        System.out.println("Encountered a merge conflict.");
                    }
                }
            }
        }

        saveRepo(repo);
        commit(message, other);
    }

    /**
     *  List ALL the Helper FUNCTION below.
     *  List ALL the Helper FUNCTION below.
     */

    /** Rewrite a file to the given ID version. */
    public static void writeFile(String fileName, String blobId) {
        File file = Utils.join(CWD, fileName);
        File blob = Utils.join(BlOBS_DIR, fileName, blobId);

        writeContents(file, readContents(blob));
    }

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
    //TODO  should change the name to stageForAddition
    private static void staging(Repository repo, File infile) {
        byte[] contents = Utils.readContents(infile);
        String sha1 = Utils.sha1(contents);
        String fileName = infile.getName();

        File outFile = Utils.join(BlOBS_DIR, fileName, sha1);
        Utils.writeContents(outFile, contents);

        repo.staged.put(fileName, sha1);
    }

    /** Remove a file from staging area. */
    // TODO: Refactor: offstage ===> removeFromAddition
    private static void offStage(Repository repo, String fileName) {
        if (!isStaged(repo, fileName)) {
            Utils.message("File is not on the stage.");
            System.exit(0);
        }

        repo.staged.remove(fileName);
    }

    /** Check if a file in the staging area. */
    // TODO Refactor: isStaged ===> isStagedForAddition
    private static boolean isStaged(Repository repo, String fileName) {
        return repo.staged.containsKey(fileName);
    }

    /** Check if a file identical to the version on the stage. */
    private static boolean isIdentical(Repository repo, String name, String sha1) {
        return sha1.equals(repo.staged.get(name));
    }

    /**
     * Check if a commit Id is valid.
     * if it is, returns the complete id (some given ids are shorten version.)
     * otherwise return null.
     */
    private static String isValidCommitID(String id) {
        List<String> commits = Utils.plainFilenamesIn(COMMITS_DIR);

        if (id.length() > 40) {
            return null;
        } else if (id.length() == 40) {
            if (commits.contains(id)) {
                return id;
            } else {
                return null;
            }
        } else {
            for (String commitId: commits) {
                if (commitId.startsWith(id)) {
                    return commitId;
                }
            }
            return null;
        }
    }





    /** check if in an initialized Gitlet Directory. */
    static boolean isInit() {
        return GITLET_DIR.exists();
    }

    @Override
    /** Repo Status. */
    public String toString() {
        StringBuffer sb = new StringBuffer("=== Branches ===\n");
        Set<String> branchesKey = branches.keySet();
        for(String b: branchesKey) {
            if (b.equals(head)) {
                sb.append("*");
            }
            sb.append(b);
            sb.append('\n');
        }

        sb.append("\n=== Staged Files ===\n");
        for(String s: staged.keySet()) {
            sb.append(s);
            sb.append('\n');
        }

        sb.append("\n=== Removed File ===\n");
        for (String r: removed) {
            sb.append(r);
            sb.append('\n');
        }

        sb.append("\n=== Modifications Not Staged For Commit ===\n");

        List<String> workingFile = Utils.plainFilenamesIn(CWD);
        Set<String> modified = new TreeSet<>();
        Commit current = headPtr(this);

        for (String fileName: workingFile) {
            File file = Utils.join(CWD, fileName);
            String sha1 = sha1File(file);

            if (!isStaged(this, fileName) && !this.removed.contains(fileName)) {
                if (current.containsFile(fileName) && !current.isIdentical(fileName, sha1)) {
                    modified.add(fileName);
                }
            } else if (isStaged(this, fileName)){

                if(!isIdentical(this, fileName, sha1)) {
                    modified.add(fileName);
                }
            }
        }
        for (String fileName: staged.keySet()) {
            if (!workingFile.contains(fileName)) {
                modified.add(fileName);
            }
        }

        for (String fileName: current.blobMap.keySet()) {
            if (!removed.contains(fileName) && !workingFile.contains(fileName)) {
                modified.add(fileName);
            }
        }

        for (String s: modified) {
            sb.append(s);
            sb.append('\n');
        }


        sb.append("\n=== Untracked Files ===\n");

        for (String fileName: workingFile) {
            if (!isStaged(this, fileName) && !current.containsFile(fileName)) {
                sb.append(fileName);
                sb.append('\n');
            }
        }

        return sb.toString();
    }

    /** check if untracked files exist. */
    public boolean containsUntrackedFile() {
        List<String> workingFile = Utils.plainFilenamesIn(CWD);
        Commit current = headPtr(this);

        for (String fileName: workingFile) {
            if (!isStaged(this, fileName) && !current.containsFile(fileName)) {
                return true;
            }
        }
        return  false;
    }
}
