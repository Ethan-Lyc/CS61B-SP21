package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Yicheng Liao
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        Repository repo = new Repository();
        String firstArg = args[0];
        switch (firstArg) {
            case "init":
                // handle the `init` command
                repo.checkCommandLength(args.length, 1);
                repo.init();
                break;
            case "add":
                // handle the `add [filename]` command
                repo.checkCommandLength(args.length, 2);
                repo.checkIfInitDirectoryExists();
                repo.add(args[1]);
                break;
            case "rm":
                repo.checkCommandLength(args.length, 2);
                repo.checkIfInitDirectoryExists();
                repo.rm(args[1]);
                break;
            case "commit":
                repo.checkCommandLength(args.length, 2);
                repo.checkIfInitDirectoryExists();
                repo.commit(args[1]);
                break;
            case "log":
                repo.checkCommandLength(args.length, 1);
                repo.checkIfInitDirectoryExists();
                repo.log();
                break;
            case "global-log":
                repo.checkCommandLength(args.length, 1);
                repo.checkIfInitDirectoryExists();
                repo.globalLog();
                break;
            case "checkout":
                int len = args.length;  // 2 3 4
                if (len < 2 || len > 4) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                repo.checkIfInitDirectoryExists();
                if (len == 2) {
                    // java gitlet.Main checkout [branch name]
                    repo.checkoutFromBranch(args[1]);
                } else if (len == 3) {
                    // java gitlet.Main checkout -- [file name]
                    repo.checkEqual(args[1], "--");
                    repo.checkoutFileFromHead(args[2]);
                } else if (len == 4) {
                    // java gitlet.Main checkout [commit id] -- [file name]
                    repo.checkEqual(args[2], "--");
                    repo.checkoutFileFromCommitId(args[1], args[3]);
                }
                break;

            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }
}
