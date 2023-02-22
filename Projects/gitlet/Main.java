package gitlet;

import java.io.File;
import java.io.IOException;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Alana Li
 */
public class Main {
    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) throws IOException {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        switch (args[0]) {
        case "init":
            File gitlet = new File(".gitlet");
            if (gitlet.exists()) {
                System.out.println("A gitlet version-control system "
                        + "already exists in the current directory.");
            } else {
                Commands.init();
            }
            break;
        case "add":
            Commands.stage(args[1]);
            break;
        case "commit":
            if (args[1].length() == 0) {
                System.out.println("Please enter a commit message.");
                break;
            }
            Commands.commit(args[1]);
            break;
        case "checkout":
            checkoutHelper(args);
            break;
        case "reset":
            Commands.reset(args[1]);
            break;
        case "log":
            Commands.log();
            break;
        case "global-log":
            Commands.globalLog();
            break;
        case "find":
            Commands.find(args[1]);
            break;
        case "rm":
            Commands.rm(args[1]);
            break;
        case "status":
            Commands.status();
            break;
        case "branch":
            Commands.branch(args[1]);
            break;
        case "rm-branch":
            Commands.rmBranch(args[1]);
            break;
        case "merge":
            Commands.merge(args[1]);
            break;
        default:
            System.out.println("No command with that name exists.");
        }
    }

    public static void checkoutHelper(String... args) throws IOException {
        if (args.length == 2) {
            Commands.checkoutBranch(args[1]);
        } else if (args.length == 3) {
            if (!args[1].equals("--")) {
                System.out.println("Incorrect operands.");
            }
            Commands.checkout(args[2]);
        } else if (args.length == 4) {
            if (!args[2].equals("--")) {
                System.out.println("Incorrect operands.");
            }
            Commands.checkout(args[1], args[3]);
        }
    }
}
