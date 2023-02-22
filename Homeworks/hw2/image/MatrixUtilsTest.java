package image;

import lists.Utils;
import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *  @author FIXME
 */
public class MatrixUtilsTest {
    @Test
    public void accVertTest() {
        double[][] test1 = {{1000000, 1000000, 1000000, 1000000},
                {1000000, 75990, 30003, 1000000},
                {1000000, 30002, 103046, 1000000},
                {1000000, 29515, 38273, 1000000},
                {1000000, 73403, 35399, 1000000},
                {1000000, 1000000, 1000000, 1000000}};
        double [][] result1 = {{1000000, 1000000, 1000000, 1000000},
                {2000000, 1075990, 1030003, 2000000},
                {2075990, 1060005, 1133049, 2030003},
                {2060005, 1089520, 1098278, 2133049},
                {2089520, 1162923, 1124919, 2098278},
                {2162923, 2124919, 2124919, 2124919}};

        assertArrayEquals(result1, MatrixUtils.accumulateVertical(test1));
    }

    @Test
    public void transposeTest() {
        double[][] test1 = {{1000000, 1000000, 1000000, 1000000},
                {1000000, 75990, 30003, 1000000},
                {1000000, 30002, 103046, 1000000},
                {1000000, 29515, 38273, 1000000},
                {1000000, 73403, 35399, 1000000},
                {1000000, 1000000, 1000000, 1000000}};
        double[][] result1 = {{1000000, 1000000, 1000000, 1000000, 1000000, 1000000},
                {1000000, 75990, 30002, 29515, 73403, 1000000},
                {1000000, 30003, 103046, 38273, 35399, 1000000},
                {1000000, 1000000, 1000000, 1000000, 1000000, 1000000}};
        assertArrayEquals(result1, MatrixUtils.matrixTranspose(test1));
        assertArrayEquals(test1, MatrixUtils.matrixTranspose(result1));
    }

    @Test
    public void smallestTest() {
        double[] test1 = {1, 2, 3 ,4 ,5};
        assertEquals(0, MatrixUtils.smallestIndex(test1, 0, 4));
    }

    @Test
    public void vertSeamTest() {
        double[][] test1 = {{10, 4, 5},
                {3, 10, 18},
                {8, 5, 19}};
        int[] result1 = {1, 0, 1};
        assertArrayEquals(result1, MatrixUtils.findVerticalSeam(test1));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(MatrixUtilsTest.class));
    }
}
