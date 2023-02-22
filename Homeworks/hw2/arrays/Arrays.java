package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author
 */
class Arrays {

    /* C1. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        int size = A.length + B.length;
        int[] both = new int[size];
        int index = 0;
        for (int i : A) {
            both[index] = i;
            index += 1;
        }
        for (int j : B) {
            both[index] = j;
            index += 1;
        }
        return both;
    }


    /* C2. */
    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. If the start + len is out of bounds for our array, you
     *  can return null.
     *  Example: if A is [0, 1, 2, 3] and start is 1 and len is 2, the
     *  result should be [0, 3]. */
    static int[] remove(int[] A, int start, int len) {
        if (start + len > A.length) {
            return null;
        }
        int size = A.length - len;
        if (size == 0) {
            return new int[]{};
        }
        int[] result = new int[size];

        int num = 0;
        int index = 0;
        for (int i : A) {
            if (index < start) {
                result[num] = i;
                num += 1;
            } else if (index >= start + len) {
                result[num] = i;
                num += 1;
            }
            index += 1;
        }
        return result;
    }

}
