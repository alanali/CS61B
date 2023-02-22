package arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *  @author FIXME
 */

public class ArraysTest {
    @Test
    public void concatTest() {
        int[] arr1 = new int[]{1, 2, 3, 4, 5};
        int[] arr2 = new int[]{};
        int[] arr3 = new int[]{3};
        int[] arr4 = new int[]{3, 1, 2, 3, 4 ,5};
        int[] arr5 = new int[]{1, 2, 3, 4, 5, 3};

        assertArrayEquals(arr4, Arrays.catenate(arr3, arr1));
        assertArrayEquals(arr1, Arrays.catenate(arr1, arr2));
        assertArrayEquals(arr5, Arrays.catenate(arr1, arr3));
        assertArrayEquals(arr2, Arrays.catenate(arr2, arr2));
    }
    @Test
    public void removeTest() {
        int[] arr1 = new int[]{1, 2, 3, 4, 5};
        int[] arr2 = new int[]{1, 4, 5};
        int[] arr3 = new int[]{1, 2, 3, 4};
        int[] arr4 = new int[]{};
        int[] arr5 = new int[]{1};

        assertArrayEquals(null, Arrays.remove(arr1, 1, 5));
        assertArrayEquals(arr2, Arrays.remove(arr1, 1, 2));
        assertArrayEquals(arr3, Arrays.remove(arr1, 4, 1));
        assertArrayEquals(arr4, Arrays.remove(arr2, 0, 3));
        assertArrayEquals(arr5, Arrays.remove(arr1, 1, 4));
    }
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
