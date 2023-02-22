import static org.junit.Assert.*;
import org.junit.Test;

public class MultiArrTest {

    @Test
    public void testMaxValue() {
        int[][] testArr =  {{1, 2, 3}, {5, 2}, {4}};
        int[][] testArr2 = {{3, 3, 3}, {2, 2, 7}, {7}};
        int[][] testArr3 = {{-3, -2, -1}, {-9, -5}};
        assertEquals(5, MultiArr.maxValue(testArr));
        assertEquals(7, MultiArr.maxValue(testArr2));
        assertEquals(-1, MultiArr.maxValue(testArr3));
    }

    @Test
    public void testAllRowSums() {
        int[][] testArr =  {{1, 2, 3}, {5, 2}, {4}};
        int[][] testArr2 = {{3, 3, 3}, {2, 2, 7}, {7}};
        int[][] testArr3 = {{-3, -2, -1}, {-9, -5}};

        int[] ansArr = {6, 7, 4};
        assertArrayEquals(ansArr, MultiArr.allRowSums(testArr));
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
