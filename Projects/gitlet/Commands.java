package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.TreeMap;
import java.util.List;

//python3 tester.py --show=all --verbose samples/test?
//python3 tester.py --keep --debug samples/test40-special-merge-cases.in

public class Commands {
    /**
     * Current working directory.
     */
    private static final File CWD = new File(System.getProperty("user.dir"));
    public static void init() throws IOException {
        new File(".gitlet").mkdirs();
        new File(".gitlet/commits").mkdirs();
        new File(".gitlet/data").mkdirs();
        new File(".gitlet/data/head").createNewFile();
        new File(".gitlet/data/parent").createNewFile();
        new File(".gitlet/data/currBranch").createNewFile();
        new File(".gitlet/data/master").createNewFile();
        new File(".gitlet/data/staged").createNewFile();
        new File(".gitlet/data/blobs").createNewFile();
        new File(".gitlet/data/removed").createNewFile();
        new File(".gitlet/data/branches").createNewFile();
        Commit initial = new Commit();
        updateCurrBranch("master");
        updateHead(initial.getID());
        File split = new File(".gitlet/data/splitpoint-master");
        Utils.writeObject(split, initial);
        ArrayList<String> branch = new ArrayList<>();
        branch.add("master");
        updateBranch("master", initial.getID());
        File branches = findFile("branches", "d");
        Utils.writeObject(branches, branch);
        File removed = findFile("removed", "d");
        Utils.writeObject(removed, new ArrayList<String>());
    }

    /**
     * Adds file to the staged area (added) and blobs.
     * @param name - file name to be staged
     */
    public static void stage(String name) {
        File f = findFile(name, "n");
        if (f == null) {
            System.out.println("File does not exist.");
            return;
        }
        File b = findFile("blobs", "d");
        File s = findFile("staged", "d");
        File r = findFile("removed", "d");
        TreeMap<String, String> blobs;
        ArrayList<String> staged = currStaged();
        ArrayList<String> removed = currRemoved();
        if (removed.contains(name)) {
            removed.remove(name);
            Utils.writeObject(r, removed);
        }
        if (b.length() == 0) {
            blobs = new TreeMap<>();
            blobs.put(name, Utils.readContentsAsString(f));
            Utils.writeObject(b, blobs);
            updateStaged(name);
        } else {
            blobs = currBlobs();
            if (blobs.containsKey(name)) {
                if (blobs.get(name).equals(Utils.readContentsAsString(f))) {
                    staged.remove(name);
                    Utils.writeObject(s, staged);
                } else {
                    blobs.remove(name);
                    blobs.put(name, Utils.readContentsAsString(f));
                    updateStaged(name);
                }
            } else {
                blobs.put(name, Utils.readContentsAsString(f));
                updateStaged(name);
            }
            Utils.writeObject(b, blobs);
        }
    }

    public static void rm(String file) {
        File s = findFile("staged", "d");
        File r = findFile("removed", "d");
        ArrayList<String> staged = currStaged();
        ArrayList<String> removed = currRemoved();
        TreeMap<String, String> blobs = currBlobs();
        if (!staged.contains(file) && !blobs.containsKey(file)) {
            System.out.println("No reason to remove the file.");
            return;
        }
        boolean isStaged = staged.contains(file);
        boolean isTracked = blobs.containsKey(file);
        if (isStaged) {
            staged.remove(file);
            Utils.writeObject(s, staged);
        } else if (isTracked) {
            removed.add(file);
            Utils.restrictedDelete(file);
            Utils.writeObject(r, removed);
        }
    }

    public static void commit(String file) {
        File branch = findFile(currBranch(), "d");
        String branchID = Utils.readContentsAsString(branch);
        File p = findFile(branchID, "c");
        updateParent(branchID);
        Commit parent = Utils.readObject(p, Commit.class);
        String parentID = parent.getID();
        ArrayList<String> added = currStaged();
        ArrayList<String> removed = currRemoved();
        if (added.size() == 0 && removed.size() == 0) {
            System.out.println("No changes added to the commit.");
            return;
        }
        added = update(added, parent.getAdded());
        TreeMap<String, String> blobs = currBlobs();
        blobs = update(blobs, parent.getBlobs());
        Commit c = new Commit(file, added, removed, blobs, parentID);
        updateHead(c.getID());
        updateBranch(currBranch(), c.getID());
        clearStage();
    }

    public static void checkout(String file) {
        String headID = Utils.readContentsAsString(findFile("head", "d"));
        File headCommit = findFile(headID, "c");
        Commit head = Utils.readObject(headCommit, Commit.class);
        File found = Commands.findFile(file, "n");
        if (found != null) {
            Utils.writeContents(found, head.getBlobs().get(file));
        } else {
            System.out.println("File " + file + " was not found.");
        }
    }

    public static void checkout(String id, String file) throws IOException {
        if (id.length() < 40) {
            List<String> files = Utils.plainFilenamesIn(".gitlet/commits");
            for (String f : files) {
                if (f.startsWith(id)) {
                    checkout(f, file);
                }
            }
            return;
        }
        File commit = findFile(id, "c");
        if (commit == null) {
            print("No commit with that id exists.");
            return;
        }
        Commit pick = Utils.readObject(commit, Commit.class);
        if (!pick.getBlobs().containsKey(file)) {
            print("File does not exist in that commit.");
            return;
        }
        pick = Utils.readObject(commit, Commit.class);
        if (!pick.getBlobs().containsKey(file)) {
            print("File does not exist in that commit.");
            return;
        }
        File found = Commands.findFile(file, "n");
        if (found != null) {
            Utils.writeContents(found, pick.getBlobs().get(file));
        } else {
            File n = new File(".gitlet/" + file);
            Utils.writeContents(n, pick.getBlobs().get(file));

        }
    }

    public static void checkoutBranch(String branch) {
        if (!currBranches().contains(branch)) {
            print("No such branch exists.");
            return;
        } else if (currBranch().equals(branch)) {
            print ("No need to checkout the current branch.");
            return;
        }

        File branchHead = findFile(currBranch(), "d");
        String branchID = Utils.readContentsAsString(branchHead);
        File branchFile = findFile(branchID, "c");
        Commit currBranch = Utils.readObject(branchFile, Commit.class);

        File head = findFile(currHead(), "c");
        Commit curr = Utils.readObject(head, Commit.class);

        File g = findFile(branch, "d");
        String givenID = Utils.readContentsAsString(g);
        File givenCommit = findFile(givenID, "c");
        Commit given = Utils.readObject(givenCommit, Commit.class);
        List<String> files = Utils.plainFilenamesIn(CWD);
        for (String f : files) {
            if (curr.getBlobs().containsKey(f) && !given.getBlobs().containsKey(f)) {
                Utils.restrictedDelete(f);
            } else if (!currBranch.getBlobs().containsKey(f) && given.getBlobs().containsKey(f)) {
                print("There is an untracked file in the way; "
                        + "delete it or add and commit it first.");
                return;
            }
        }
        for (String s : given.getBlobs().keySet()) {
            File found = findFile(s, "n");
            if (found != null) {
                found.delete();
            }
            File n = new File(s);
            Utils.writeContents(n, given.getBlobs().get(s));
        }
        clearStage();
        updateHead(branchID);
        updateCurrBranch(branch);
    }

    public static void reset(String commit) {
        File given = findFile(commit, "c");
        if (given == null) {
            print("No commit with that id exists.");
            return;
        }
        File c = findFile(currHead(), "c");
        List<String> files = Utils.plainFilenamesIn(".gitlet");
        Commit x = Utils.readObject(given, Commit.class);
        Commit curr = Utils.readObject(c, Commit.class);
        TreeMap<String, String> tracked = x.getBlobs();
        TreeMap<String, String> ctracked = curr.getBlobs();
        for (String f : files) {
            if (!curr.getBlobs().containsKey(f) && x.getBlobs().containsKey(f)) {
                print("There is an untracked file in the way; "
                        + "delete it or add it first.");
                return;
            }
        }
        if (!currUntracked().isEmpty()) {
            print("There is an untracked file in the way; "
                    + "delete it or add it first.");
            return;
        }
        for (String s : ctracked.keySet()) {
            if (!tracked.containsKey(s)) {
                Utils.restrictedDelete(s);
            }
        }
        File branchHead = findFile(currBranch(), "d");
        Utils.writeContents(branchHead, commit);
        clearStage();
    }

    /**
     * Helper for merge, checks merge errors.
     * @param branch - branch to merge
     * @return true if no errors, false otherwise
     */
    public static boolean mergeCheck(String branch) {
        if (currStaged().size() != 0 || currRemoved().size() != 0) {
            print("You have uncommitted changes.");
            return false;
        } else if (!currBranches().contains(branch)) {
            print("A branch with that name does not exist.");
            return false;
        } else if (currBranch().equals(branch)) {
            print("Cannot merge a branch with itself.");
            return false;
        }
        return true;
    }

    public static void merge(String branch) throws IOException {
        if (!mergeCheck(branch)) {
            return;
        }
        String otherC = Utils.readContentsAsString(findFile(branch, "d"));
        String headC = Utils.readContentsAsString(findFile(currBranch(), "d"));
        List<String> fileList = Utils.plainFilenamesIn(CWD);
        File o = findFile(otherC, "c");
        File h = findFile(headC, "c");
        Commit otherCommit = Utils.readObject(o, Commit.class);
        Commit headCommit = Utils.readObject(h, Commit.class);
        File s = findFile("splitpoint-" + branch, "d");
        Commit splitCommit = Utils.readObject(s, Commit.class);
        File c = findFile(currHead(), "c");
        Commit curr = Utils.readObject(c, Commit.class);
        for (String f : fileList) {
            if (!curr.getBlobs().containsKey(f)
                    && headCommit.getBlobs().containsKey(f)) {
                print("There is an untracked file in the way; "
                        + "delete it, or add and commit it first.");
                return;
            }
        }
        if (branch.equals("master")) {
            checkoutBranch(branch);
            print("Current branch fast-forwarded.");
            return;
        }
        if (splitCommit.getID().equals(otherCommit.getID())) {
            print("Given branch is an ancestor of the current branch.");
            return;
        }
        ArrayList<Commit> commits = new ArrayList<>();
        commits.add(splitCommit);
        commits.add(headCommit);
        commits.add(otherCommit);
        ArrayList<String> items = combinedFiles(commits);

        ArrayList<String> split = getFiles(items, splitCommit);
        ArrayList<String> head = getFiles(items, headCommit);
        ArrayList<String> other = getFiles(items, otherCommit);

        ArrayList<String> combined = mergedFiles(split, head, other, items);
        for (int i = 0; i < combined.size(); i++) {
            String x = combined.get(i);
            if (!x.equals("RR")) {
                File found = findFile(items.get(i), "n");
                if (found != null) {
                    Utils.writeContents(found, x);
                } else {
                    File n = new File(".gitlet/" + items.get(i));
                    Utils.writeContents(n, x);
                }
            }
        }
        String msg = "Merged " + branch + " into " + currBranch() + ".";
        Commit n = new Commit(msg, currStaged(), currRemoved(), currBlobs(),
                headC, otherC);
        updateHead(n.getID());
        clearStage();
    }

    public static ArrayList<String> mergedFiles(ArrayList<String> split,
                                                ArrayList<String> head,
                                                ArrayList<String> other,
                                                ArrayList<String> items) {
        ArrayList<String> contents = new ArrayList<>();
        for (int i = 0; i < split.size(); i++) {
            String s = split.get(i);
            if (s == null) {
                if (head.get(i) == null && other.get(i) != null) {
                    contents.add(other.get(i));
                } else if (head.get(i) != null && other.get(i) == null) {
                    contents.add(head.get(i));
                }
            } else if (s.equals(head.get(i)) && other.get(i) == null) {
                contents.add("");
            } else if (s.equals(other.get(i)) && head.get(i) == null) {
                contents.add("RR");
            } else if (s.equals(head.get(i)) && !s.equals(other.get(i))) {
                contents.add(other.get(i));
            } else if (!s.equals(head.get(i)) && s.equals(other.get(i))) {
                contents.add(head.get(i));
            } else if (!s.equals(head.get(i)) && !s.equals(other.get(i))) {
                if (head.get(i).equals(other.get(i))) {
                    contents.add(head.get(i));
                } else {
                    print("Encountered a merge conflict.");
                    contents.add("<<<<<<< HEAD\n" + head.get(i) + "=======\n"
                            + other.get(i) + ">>>>>>>\n");
                    updateStaged(items.get(i));
                }
            }
        }
        return contents;
    }

    /**
     * Helper for merge, returns ArrayList of file contents of given
     * commit and file names.
     * @param files - ArrayList of files to look up
     * @param c - commit to use
     * @return ArrayList of file contents of files in commit
     */
    public static ArrayList<String> getFiles(ArrayList<String> files,
                                               Commit c) {
        ArrayList<String> ret = new ArrayList<>();
        for (String f : files) {
            ret.add(c.getBlobs().getOrDefault(f, null));
        }
        return ret;
    }

    /**
     * Helper for merge, returns ArrayList of files included in given commits.
     * @param commits - ArrayList of commits
     * @return files names from commits (ArrayList)
     */
    public static ArrayList<String> combinedFiles(ArrayList<Commit> commits) {
        ArrayList<String> files = new ArrayList<>();
        for (Commit c : commits) {
            for (String s : c.getBlobs().keySet()) {
                if (!files.contains(s)) {
                    files.add(s);
                }
            }
        }
        return files;
    }

    public static void branch(String branch) throws IOException {
        if (currBranches().contains(branch)) {
            print("A branch with that name already exists.");
        } else {
            File c = findFile(currHead(), "c");
            Commit curr = Utils.readObject(c, Commit.class);
            File split = new File(".gitlet/data/splitpoint-" + branch);
            Utils.writeObject(split, curr);
            updateBranches(branch);
            new File(".gitlet/data/" + branch).createNewFile();
            updateBranch(branch, currHead());
        }
    }

    public static void rmBranch(String branch) {
        File delete = findFile(branch, "d");
        if (delete == null) {
            print("A branch with that name does not exist.");
            return;
        } else if (branch.equals(currBranch())) {
            print("Cannot remove the current branch.");
            return;
        }
        File branches = findFile("branches", "d");
        delete.delete();
        ArrayList<String> b = currBranches();
        b.remove(branch);
        Utils.writeObject(branches, b);
    }

    public static void log() {
        String print = "";
        File branch = findFile(currBranch(), "d");
        String headID = Utils.readContentsAsString(branch);
        File headCommit = findFile(headID, "c");
        Commit head = Utils.readObject(headCommit, Commit.class);
        while (head != null) {
            print += "===\ncommit " + head.getID() + "\n";
            print += "Date: " + head.getTime() + "\n";
            print += head.getMsg() + "\n";
            if (head.getParent() != null) {
                print += "\n";
                String parentID = head.getParent();
                head = Utils.readObject(findFile(parentID, "c"), Commit.class);
            } else {
                break;
            }
        }
        System.out.println(print);
    }

    public static void globalLog() {
        String print = "";
        List<String> commits = Utils.plainFilenamesIn(".gitlet/commits");
        if (commits == null) {
            print("No commits.");
            return;
        }
        for (String c : commits) {
            Commit commit = Utils.readObject(findFile(c, "c"), Commit.class);
            print += "\n===\n";
            print += "commit " + commit.getID() + "\n";
            print += "Date: " + commit.getTime() + "\n";
            print += commit.getMsg() + "\n";
        }
        print(print);
    }

    public static void status() {
        if (!Files.exists(Paths.get(".gitlet"))) {
            print("Not in an initialized Gitlet directory.");
            return;
        }
        String print = "";
        ArrayList<String> branches = currBranches();
        ArrayList<String> staged = currStaged();
        ArrayList<String> removed = currRemoved();
        print += "=== Branches ===\n";
        for (String b : branches) {
            if (Objects.equals(b, currBranch())) {
                print += "*";
            }
            print += b + "\n";
        }
        print += "\n=== Staged Files ===\n";
        for (String s : staged) {
            print += s + "\n";
        }
        print += "\n=== Removed Files ===\n";
        for (String r : removed) {
            print += r + "\n";
        }
        print += "\n=== Modifications Not Staged For Commit ===\n";
        print += "\n=== Untracked Files ===\n";
        System.out.println(print);
    }

    public static void find(String msg) {
        boolean found = false;
        List<String> commits = Utils.plainFilenamesIn(".gitlet/commits");
        if (commits == null) {
            print("No commits.");
            return;
        }
        for (String c : commits) {
            Commit commit = Utils.readObject(findFile(c, "c"), Commit.class);
            if (commit.getMsg().equals(msg)) {
                print(commit.getID());
                found = true;
            }
        }
        if (!found) {
            print("Found no commit with that message.");
        }
    }

    /**
     * Returns the File with the same file name.
     * @param name - file name to be found
     * @param type - folder that file is in (data, commits, cwd)
     * @return File that matches the name
     */
    public static File findFile(String name, String type) {
        File dir;
        if (type.equals("d")) {
            dir = new File(".gitlet/data");
        } else if (type.equals("c")) {
            dir = new File(".gitlet/commits");
        } else {
            dir = CWD;
        }
        File[] files = dir.listFiles();
        for (File f : files) {
            if (f.getName().equals(name)) {
                return f;
            }
        }
        return null;
    }

    /**
     * Returns the updated ArrayList of added objects with parent values
     * included.
     * @param a - current ArrayList
     * @param old - parent ArrayList
     * @return Combined ArrayList
     */
    public static ArrayList<String> update(ArrayList<String> a,
                                           ArrayList<String> old) {
        for (String s : old) {
            if (!a.contains(s)) {
                a.add(s);
            }
        }
        return a;
    }

    /**
     * Returns the updated treemap with parent values included.
     * @param b - current treemap
     * @param old - parent treemap
     * @return Combined treemap
     */
    public static TreeMap<String, String> update(TreeMap<String, String> b,
                                                 TreeMap<String, String> old) {
        for (String s : old.keySet()) {
            if (!b.containsKey(s)) {
                b.put(s, old.get(s));
            }
        }
        return b;
    }

    /**
     * Update head commit's id.
     * @param h - commit id
     */
    public static void updateHead(String h) {
        File f = findFile("head", "d");
        if (f == null) {
            System.out.println("Could not find head file.");
        } else {
            Utils.writeContents(f, h);
        }
    }

    /**
     * Update so parent is pointed to commit id p.
     * @param p - commid id of parent
     */
    public static void updateParent(String p) {
        File f = findFile("parent", "d");
        if (f == null) {
            System.out.println("Could not find parent file.");
        } else {
            Utils.writeContents(f, p);
        }
    }

    /**
     * Changes commit id stored in branch.
     * @param branch - branch to change
     * @param id - commit id branch is now pointed to
     */
    public static void updateBranch(String branch, String id) {
        File f = findFile(branch, "d");
        if (f == null) {
            print("Could not find " + branch + " file.");
        } else {
            Utils.writeContents(f, id);
        }
    }

    /**
     * Adds branch to branches file.
     * @param branch - name of branch to add.
     */
    public static void updateBranches(String branch) {
        File f = findFile("branches", "d");
        ArrayList<String> b;
        if (f == null) {
            print("Could not find " + branch + " file.");
        } else {
            b = currBranches();
            b.add(branch);
            Utils.writeObject(f, b);
        }
    }

    /**
     * Changed the branch name stored in currBranch.
     * @param b - branch name
     */
    public static void updateCurrBranch(String b) {
        File f = findFile("currBranch", "d");
        if (f == null) {
            System.out.println("Could not find currBranch file.");
        } else {
            Utils.writeContents(f, b);
        }
    }

    /**
     * Adds file to staged ArrayList.
     * @param s - file to be added
     */
    @SuppressWarnings("unchecked")
    public static void updateStaged(String s) {
        File f = findFile("staged", "d");
        if (f == null) {
            System.out.println("Could not find staged file.");
        } else {
            ArrayList<String> info;
            if (f.length() == 0) {
                info = new ArrayList<>();
            } else {
                info = Utils.readObject(f, ArrayList.class);
            }
            info.add(0, s);
            Utils.writeObject(f, info);
        }
    }

    @SuppressWarnings("unchecked")
    public static void updateRemoved(String s) {
        File f = findFile("removed", "d");
        if (f == null) {
            System.out.println("Could not find removed file.");
        } else {
            ArrayList<String> info;
            if (f.length() == 0) {
                info = new ArrayList<>();
            } else {
                info = Utils.readObject(f, ArrayList.class);
            }
            info.add(0, s);
            Utils.writeObject(f, info);
        }
    }

    public static void clearStage() {
        File f = findFile("staged", "d");
        File r = findFile("removed", "d");
        if (f == null) {
            System.out.println("Could not find staged file.");
        } else {
            Utils.writeObject(f, new ArrayList<>());
            Utils.writeObject(r, new ArrayList<>());
        }
    }

    public static void addBranch(String branch) {
        File b = findFile("branches", "d");
        ArrayList<String> branches = currBranches();
        branches.add(branch);
        Utils.writeObject(b, branches);
    }

    /**
     * Returns the commit ID of head commit.
     * @return head commit (String)
     */
    public static String currHead() {
        File c = findFile("head", "d");
        return Utils.readContentsAsString(c);
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<String> currStaged() {
        File s = findFile("staged", "d");
        if (s.length() == 0) {
            return new ArrayList<>();
        }
        return Utils.readObject(s, ArrayList.class);
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<String> currRemoved() {
        File r = findFile("removed", "d");
        return Utils.readObject(r, ArrayList.class);
    }

    @SuppressWarnings("unchecked")
    public static TreeMap<String, String> currBlobs() {
        File b = findFile("blobs", "d");
        if (b.length() == 0) {
            return new TreeMap<String, String>();
        }
        return Utils.readObject(b, TreeMap.class);
    }

    /**
     * Returns ArrayList of the names of active branches.
     * @return active branches (ArrayList_
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<String> currBranches() {
        File b = findFile("branches", "d");
        return Utils.readObject(b, ArrayList.class);
    }

    /**
     * Returns name of current branch.
     * @return name of current branch (String)
     */
    public static String currBranch() {
        File b = findFile("currBranch", "d");
        return Utils.readContentsAsString(b);
    }

    public static ArrayList<String> currUntracked(Commit c) {
        List<String> files = Utils.plainFilenamesIn(".gitlet");
        ArrayList<String> untracked = new ArrayList<>();
        for (String f : files) {
            if (!c.getBlobs().containsKey(f)) {
                untracked.add(f);
            }
        }
        return untracked;
    }

    public static ArrayList<String> currUntracked() {
        List<String> files = Utils.plainFilenamesIn(".gitlet");
        ArrayList<String> untracked = new ArrayList<>();
        File h = findFile(currHead(), "c");
        Commit head = Utils.readObject(h, Commit.class);
        for (String f : files) {
            if (!head.getBlobs().containsKey(f)
                    && ! head.getAdded().contains(f)) {
                untracked.add(f);
            }
        }
        return untracked;
    }

    public static void print(String msg) {
        System.out.println(msg);
    }

    public static void print() {
        System.out.println("ERROR ERROR ERROR ERROR ERROR");
    }
}
