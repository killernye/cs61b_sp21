package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args.length == 0) {
            throw new RuntimeException("Must have at least one argument.");
        }

        String firstArg = args[0];
        String fileName;
        String message;
        String commitId;
        switch(firstArg) {
            case "init":
                validateNumArgs("init", args, 1);
                Repository.repositoryInit();
                break;
            case "add":
                validateNumArgs("add", args, 2);
                fileName = args[1];
                Repository.addFile(fileName);
                break;
            case "log":
                validateNumArgs("log", args, 1);
                Repository.repositoryLog();
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
                    Repository.checkout("head", fileName);
                } else if (args.length == 4) {
                    fileName = args[3];
                    commitId = args[1];
                    Repository.checkout(fileName, commitId);
                }
                break;
            default:
                throw new RuntimeException("Unknown command");
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
            throw new RuntimeException(
                    String.format("Invalid number of arguments for: %s.", cmd));
        }
    }
}
