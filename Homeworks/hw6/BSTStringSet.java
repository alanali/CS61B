import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author Alana Li
 */
public class BSTStringSet implements StringSet, Iterable<String> {
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {
        Node x = search(s);
        if (x == null) {
            _root = new Node(s);
        } else {
            int val = s.compareTo(x.s);
            if (val < 0) {
                x.left = new Node(s);
            } else if (val > 0) {
                x.right = new Node(s);
            }
        }
    }

    private Node search(String s) {
        if (_root == null) {
            return null;
        }
        Node x = _root;
        while (true) {
            Node n;
            int val = s.compareTo(x.s);
            if (val < 0) {
                n = x.left;
            } else if (val > 0) {
                n = x.right;
            } else {
                return x;
            }
            if (n == null) {
                return x;
            }
            x = n;
        }
    }

    @Override
    public boolean contains(String r) {
        Node x = search(r);
        if (x == null) {
            return false;
        } else if (x.s.equals(r)) {
            return true;
        }
        return false;
    }

    @Override
    public List<String> asList() {
        ArrayList<String> ret = new ArrayList<>();
        for (String word : this) {
            ret.add(word);
        }
        return ret;
    }


    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }

    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

//    @Override
    public Iterator<String> iterator(String L, String U) {
        return null;  // FIXME: PART B (OPTIONAL)
    }


    /** Root node of the tree. */
    private Node _root;
}
