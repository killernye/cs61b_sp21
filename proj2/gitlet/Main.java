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
                Repository.commit(message);
                break;
            case "checkout":
                // java gitlet.Main checkout -- [file name]
                // java gitlet.Main checkout [commit id] -- [file name]

                if (args.length == 3) {
                    fileName = args[2];
                    Repository.checkout(fileName, "head");
                } else if (args.length == 4) {
                    fileName = args[3];
                    commitId = args[1];
                    Repository.checkout(fileName, commitId);
                }
                break;
            case "status":
                validateNumArgs("log", args, 1);
                Repository.status();
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
