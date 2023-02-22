/**
 * Scheme-like pairs that can be used to form a list of integers.
 *
 * @author P. N. Hilfinger; updated by Linda Deng (1/26/2022)
 */
public class IntDList {

    /**
     * First and last nodes of list.
     */
    protected DNode _front, _back;

    /**
     * An empty list.
     */
    public IntDList() {
        _front = _back = null;
    }

    /**
     * @param values the ints to be placed in the IntDList.
     */
    public IntDList(Integer... values) {
        _front = _back = null;
        for (int val : values) {
            insertBack(val);
        }
    }

    /**
     * @return The first value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getFront() {
        return _front._val;
    }

    /**
     * @return The last value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getBack() {
        return _back._val;
    }

    /**
     * @return The number of elements in this list.
     */
    public int size() {
        DNode pos = _front;
        if (_front == null && _back == null) {
            return 0;
        }
        int s = 1;
        while(pos != _back) {
            pos = pos._next;
            s += 1;
        }
        return s;
    }

    /**
     * @param index index of node to return,
     *          where index = 0 returns the first node,
     *          index = 1 returns the second node, and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size.
     * @return The node at index
     */
    private DNode getNode(int index) {
        DNode result = _front;
        for (int i = 0; i < index; i++) {
            result = result._next;
        }
        return result;
    }

    /**
     * @param index index of element to return,
     *          where index = 0 returns the first element,
     *          index = 1 returns the second element,and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size.
     * @return The integer value at index index
     */
    public int get(int index) {
        return getNode(index)._val;
    }

    /**
     * @param d DNode to be inserted as first element
     */
    public void insertFirst(DNode d) {
        _front = d;
        _back = d;
    }

    /**
     * @param d value to be inserted in the front
     */
    public void insertFront(int d) {
        DNode insert = new DNode(d);
        /* If adding first element */
        if (size() == 0) {
            insertFirst(insert);
            return;
        }
        insert._next = _front;
        _front._prev = insert;
        _front = insert;
    }

    /**
     * @param d value to be inserted in the back
     */
    public void insertBack(int d) {
        DNode insert = new DNode(d);
        /* If adding first element */
        if (size() == 0) {
            insertFirst(insert);
            return;
        }
        insert._prev = _back;
        _back._next = insert;
        _back = insert;
    }

    /**
     * @param d     value to be inserted
     * @param index index at which the value should be inserted
     *              where index = 0 inserts at the front,
     *              index = 1 inserts at the second position, and so onh.
     *              You can assume index will always be a valid index,
     *              i.e 0 <= index <= size.
     */
    public void insertAtIndex(int d, int index) {
        DNode insert = new DNode(d);
        /* Insert at first element */
        if (index == 0) {
            insertFront(d);
        }
        /* Insert at last element */
        else if (index == size()) {
            insertBack(d);
        } else {
            DNode pos = _front;
            for (int i = index; i > 0; i--) {
                if (i == 1) {
                    insert._next = pos._next;
                    pos._next = insert;
                    insert._prev = pos;
                } else {
                    pos = pos._next;
                }
            }
        }
    }

    /**
     * Removes the first item in the IntDList and returns it.
     * Assume `deleteFront` is never called on an empty IntDList.
     *
     * @return the item that was deleted
     */
    public int deleteFront() {
        int frontVal = getFront();
        if (size() == 1) {
            _front = null;
            _back = null;
        } else {
            _front = _front._next;
            _front._prev = null;
        }
        return frontVal;
    }

    /**
     * Removes the last item in the IntDList and returns it.
     * Assume `deleteBack` is never called on an empty IntDList.
     *
     * @return the item that was deleted
     */
    public int deleteBack() {
        int backVal = getBack();
        if (size() == 1) {
            _front = null;
            _back = null;
        } else {
            _back = _back._prev;
            _back._next = null;
        }
        return backVal;
    }

    /**
     * @param index index of element to be deleted,
     *          where index = 0 returns the first element,
     *          index = 1 will delete the second element, and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size.
     * @return the item that was deleted
     */
    public int deleteAtIndex(int index) {
        /* Delete first value */
        if (index == 0) {
            return deleteFront();
        }
        /* Delete last value */
        else if (index == size() - 1) {
            return deleteBack();
        } else {
            int sol = 0;
            DNode pos = _front;
            for (int i = index; i > 0; i--) {
                if (i == 1) {
                    sol = pos._next._val;
                    pos._next._next._prev = pos;
                    pos._next = pos._next._next;
                } else {
                    pos = pos._next;
                }
            }
            return sol;
        }
    }

    /**
     * @return a string representation of the IntDList in the form
     * [] (empty list) or [1, 2], etc.
     * Hint:
     * String a = "a";
     * a += "b";
     * System.out.println(a); //prints ab
     */
    public String toString() {
        if (size() == 0) {
            return "[]";
        }
        String str = "[";
        DNode curr = _front;
        for (; curr._next != null; curr = curr._next) {
            str += curr._val + ", ";
        }
        str += curr._val +"]";
        return str;
    }

    /**
     * DNode is a "static nested class", because we're only using it inside
     * IntDList, so there's no need to put it outside and "pollute the
     * namespace" with it. This is also referred to as encapsulation.
     * Look it up for more information!
     */
    static class DNode {
        /** Previous DNode. */
        protected DNode _prev;
        /** Next DNode. */
        protected DNode _next;
        /** Value contained in DNode. */
        protected int _val;

        /**
         * @param val the int to be placed in DNode.
         */
        protected DNode(int val) {
            this(null, val, null);
        }

        /**
         * @param prev previous DNode.
         * @param val  value to be stored in DNode.
         * @param next next DNode.
         */
        protected DNode(DNode prev, int val, DNode next) {
            _prev = prev;
            _val = val;
            _next = next;
        }
    }

}
