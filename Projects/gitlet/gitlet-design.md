# Gitlet Design Document
**Author:** Alana Li

## 1. Classes and Data Structures

### Main.java
This class is responsible for reading user input and calling the 
appropriate classes and methods to implement them, as well as
taking care of the commit tree.

#### Fields
- ArrayList<String> added: Array of files to be committed
- HashMap blobs: Hashmap of files staged
  - Key: File name (String)
  - Value: File contents (String)
- TreeMap tree: Structure of commits
  - Key: Commit ID (String)
  - Value: Commit message (String)
- ArrayList<Commit> branches: Array of branches
- Commit head: Current commit
- Commit parent: Parent of current commit
- Commit master: Set to head of master branch
- Commit branch: Current branch
- File CWD: Current working directory


### Commit.java
This class creates commit objects and files that store the info
of each commit.

#### Fields
- String msg: Message associated with commit
- String name: Randomly generated name of commit
- Timestamp time: Time commit was created
- ArrayList added: Array of files to be committed
- HashMap blobs: Hashmap of files staged
  - Key: File name (String)
  - Value: File contents (String)
- String parent: Parent of commit


## 2. Algorithms

#### Main.java
1. void init(): Creates new directory, new TreeMap, make first
   default commit with Commit(), set as head. Clear branches. Set 
   master pointer to first commit. Add master to branches.
2. void add(String file): Get name of file to be added, call
   stage(file)
3. void stage(String file): Is file in HashMap?
  - Y: Does the file have the same contents?
    - Y: Do nothing.
    - N: Replace contents, append file to ArrayList
  - N: Add file to HashMap, append file to ArrayList
4. void commit(String msg): Call Commit constructor with parent,
   msg. Set head to new commit, parent to old commit. Call head.add
   with ArrayList and HashMap. Clear added. Use .put to add new
   TreeMap node with key as head.name and value as head.msg. Set
   branch to head.
5. void status(): Prints status of files based on HashMap.
6. void log(): Prints status of files based on HashMap.
7. void globalLog(): Prints status of files based on HashMap.
8. void findMsg(String msg): Loops through TreeMap, if msg matches,
   print commit name, else print "Found no commit with that message."
9. Commit findCommit(String name): Loops through TreeMap, return
   commit that matches the name.
10. void remove(String file): File in added?
  - Y: Remove from added
    File in blobs?
  - Y: Remove from working directory
11. void branch(String name): Is name taken?
  - Y: "A branch with that name already exists."
  - N: Create new commit, set to head, add to branches
12. void reBranch(String name): Does branch exist?
  - Y: Is branch == head?
    - Y: "Cannot remove the current branch."
    - N: Remove from branches
  - N: "A branch with that name does not exist."
13. void reset(String name): Loops through blobs, if file not in
    added, remove from blobs. Set head to commit of that name, clear
    blobs.
14. void merge(String name): TODO!
15. void checkout: TODO!


#### Commit.java
1. Commit(): Creates first commit
   - this.parent = null;
   - this.msg = "initial commit";
   - this.time = 00:00:00 UTC, Thursday, 1 January 1970;
   - this.added = null;
   - this.blobs = null;
   - this.name = random;
2. Commit(Commit parent, String msg, ArrayList added, HashMap blobs):
   - this.parent = parent;
   - this.msg = msg;
   - this.time = current time;
   - this.added = added;
   - this.blobs = blobs;
   - this.name = random;
3. void add(ArrayList added, HashMap blobs): Update this.added and
   this.blobs by adding files from parent, create new file and store
   all commit details inside.

## 3. Persistence

The current head and parent needs to be updated everytime a commit is made.
The current branch also needs to be stored and updated everytime a commit is
made on that branch.

Everytime a file is added, it needs to be checked if the file is being tracked
and whether there's been any changes. Also, each commit needs a way to store
the current state of the files. The tracked files from the parent commit are
carried over when making a new commit, which is why we also need to pass the
parent in when making a new commit.

Everytime a branch is made, a new commit is made which is set to head. Commit
branch is set to the new commit. The commit is also added to the ArrayList of
current branches.

## 4. Design Diagram

Attach a picture of your design diagram illustrating the structure of your
classes and data structures. The design diagram should make it easy to 
visualize the structure and workflow of your program.

