import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        assertEquals(CompoundInterest.numYears(2056), 34);
        assertEquals(CompoundInterest.numYears(2022), 0);
        assertEquals(CompoundInterest.numYears(3000), 978);
    }

    @Test
    public void testFutureValue() {
        // When working with decimals, we often want to specify a certain
        // range of "wiggle room", or tolerance. For example, if the answer
        // is 5.04, but anything between 5.02 and 5.06 would be okay too,
        // then we can do assertEquals(5.04, answer, .02).

        // The variable below can be used when you write your tests.
        double tolerance = 0.01;
        assertEquals(CompoundInterest.futureValue(10, 12, 2024), 12.54, 0.01);
        assertEquals(CompoundInterest.futureValue(100, 10, 2050), 1442.09, 0.01);
        assertEquals(CompoundInterest.futureValue(100, -10, 2050), 5.23, 0.01);
        assertEquals(CompoundInterest.futureValue(100, 0, 3000), 100, 0.01);
        assertEquals(CompoundInterest.futureValue(100, 10, 2022), 100, 0.01);
    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals(11.80, CompoundInterest.futureValueReal(10, 12, 2024, 3), 0.01);
        assertEquals(13.82, CompoundInterest.futureValueReal(10, 12, 2024, -5), 0.01);
        assertEquals(295712.29, CompoundInterest.futureValueReal(1000000, 0, 2062, 3), 0.01);
    }


    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(16550, CompoundInterest.totalSavings(5000, 2024, 10), 0.01);
        assertEquals(2100, CompoundInterest.totalSavings(1000, 2023, 10), 0.01);
    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
        assertEquals(15571.89, CompoundInterest.totalSavingsReal(5000, 2024, 10, 3), 0.01);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
