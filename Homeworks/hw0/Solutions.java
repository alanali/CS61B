/** Solutions to the HW0 Java101 exercises.
 *  @author Allyson Park and Alana Li
 */
public class Solutions {

    /** Returns whether or not the input x is even.
     */
    public static boolean isEven(int x) {
        if (x % 2 == 0) {
           return true;
        }
        return false;
    }
    
    /** Returns max number of non-empty integer array.
     */
    public static int max(int[] y) {
       int max = y[0];
       for (int i = 1; i < y.length; i++) {
          if (y[i] > max) {
             max = y[i];
          }
       }
       return max;
    }
    
    /** Returns whether a string is included in an array of strings.
     */
    public static boolean wordBank(String word, String[] bank) {
       for (int i = 0; i < bank.length; i++) {
          if (word.equals(bank[i])) {
             return true;
          }
       }
       return false;
    }
    
    /** Returns whether 3 numbers in the integer array can be summed to 0, with repetition.
     */
    public static boolean threeSum(int[] a) {
       for (int i = 0; i < a.length; i++) {
          int int1 = a[i];
          for (int j = 0; j < a.length; j++) {
             int int2 = a[j];
             for (int k = 0; k < a.length; k++) {
                int int3 = a[k];
                if (int1 + int2 + int3 == 0) {
                   return true;
                }
             }
          }
       }
       return false;
    }
    // TODO: Fill in the method signatures for the other exercises
    // Your methods should be static for this HW. DO NOT worry about what this means.
    // Note that "static" is not necessarily a default, it just happens to be what
    // we want for THIS homework. In the future, do not assume all methods should be
    // static.

}
