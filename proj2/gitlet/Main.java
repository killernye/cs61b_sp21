package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author NYE
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            Utils.message("Please enter a command.");
            System.exit(0);
        }

        String firstArg = args[0];
        String fileName;
        String message;
        String commitId;
        switch(firstArg) {
            case "init":
                validateNumArgs("init", args, 1);
                Repository.init();
                break;
            case "add":
                validateNumArgs("add", args, 2);
                fileName = args[1];
                Repository.add(fileName);
                break;
            case "log":
                validateNumArgs("log", args, 1);
                Repository.log();
                break;
            case "commit":
                //java gitlet.Main commit [message]
                validateNumArgs("commit", args, 2);
                message = args[1];
                Repository.commit(message, null);
                break;
            case "checkout":
                // java gitlet.Main checkout -- [file name]
                // java gitlet.Main checkout [commit id] -- [file name]
                // java gitlet.Main checkout [branch name]
                if (args.length == 3 && args[1].equals("--")) {
                    fileName = args[2];
                    Repository.checkout(fileName, "head");
                } else if (args.length == 4 && args[2].equals("--")) {
                    fileName = args[3];
                    commitId = args[1];
                    Repository.checkout(fileName, commitId);
                } else if (args.length == 2) {
                    String branch = args[1];
                    Repository.checkout(branch);
                } else {
                    Utils.message("Incorrect operands.");
                    System.exit(0);
                }
                break;
            case "status":
                validateNumArgs("status", args, 1);
                Repository.status();
                break;
            case "global-log":
                validateNumArgs("global-log", args, 1);
                Repository.globalLog();
                break;
            case "find":
                validateNumArgs("find", args, 2);
                Repository.find(args[1]);
                break;
            case "branch":
                validateNumArgs("branch", args, 2);
                Repository.branch(args[1]);
                break;
            case "rm-branch":
                validateNumArgs("rm-branch", args, 2);
                Repository.rmBranch(args[1]);
                break;
            case "reset":
                validateNumArgs("reset", args, 2);
                Repository.reset(args[1]);
                break;
            case "merge":
                validateNumArgs("merge", args, 2);
                Repository.merge(args[1]);
                break;
            default:
                Utils.message("No command with that name exists.");
                System.exit(0);
        }
    }


    /**
     * Checks the number of arguments versus the expected number,
     * throws a RuntimeException if they do not match.
     *
     * @param cmd Name of command you are validating
     * @param args Argument array from command line
     * @param n Number of expected arguments
     */
    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            Utils.message("Incorrect operands.");
            System.exit(0);
        }
    }
}
