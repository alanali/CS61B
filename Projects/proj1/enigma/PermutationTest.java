package enigma;

import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Alana Li
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    private static final Alphabet TESTA1 = new Alphabet("abcdefghijk");

    private static final Permutation TEST1 = new Permutation("(adc) (eb)",
            TESTA1);

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void addCycleTest() {
        Permutation testtemp = new Permutation("(adc) (eb)", TESTA1);

        testtemp.addCycle("fg");
        assertEquals('g', testtemp.permute('f'));
        assertEquals('f', testtemp.permute('g'));
        assertEquals('f', testtemp.invert('g'));
        assertEquals('g', testtemp.invert('f'));

        testtemp.addCycle("khj");
        assertEquals('h', testtemp.permute('k'));
        assertEquals('j', testtemp.permute('h'));
        assertEquals('k', testtemp.permute('j'));
        assertEquals('j', testtemp.invert('k'));
        assertEquals('k', testtemp.invert('h'));
        assertEquals('h', testtemp.invert('j'));
    }

    @Test
    public void permuteIntTest() {
        assertEquals(3, TEST1.permute(0));
        assertEquals(0, TEST1.permute(2));
        assertEquals(1, TEST1.permute(4));
        assertEquals(4, TEST1.permute(1));
        assertEquals(9, TEST1.permute(9));
        assertEquals(10, TEST1.permute(10));
        assertEquals(4, TEST1.permute(12));
    }

    @Test
    public void invertIntTest() {
        assertEquals(2, TEST1.invert(0));
        assertEquals(0, TEST1.invert(3));
        assertEquals(4, TEST1.invert(1));
        assertEquals(1, TEST1.invert(4));
        assertEquals(9, TEST1.invert(9));
        assertEquals(10, TEST1.invert(10));
        assertEquals(4, TEST1.invert(12));

    }

    @Test
    public void permuteCharTest() {
        assertEquals('d', TEST1.permute('a'));
        assertEquals('c', TEST1.permute('d'));
        assertEquals('a', TEST1.permute('c'));
        assertEquals('e', TEST1.permute('b'));
        assertEquals('b', TEST1.permute('e'));
        assertEquals('i', TEST1.permute('i'));
        assertEquals('j', TEST1.permute('j'));
    }

    @Test
    public void invertCharTest() {
        assertEquals('c', TEST1.invert('a'));
        assertEquals('a', TEST1.invert('d'));
        assertEquals('d', TEST1.invert('c'));
        assertEquals('b', TEST1.invert('e'));
        assertEquals('e', TEST1.invert('b'));
        assertEquals('i', TEST1.invert('i'));
        assertEquals('j', TEST1.invert('j'));
    }

    @Test
    public void derangementTest() {
        Permutation temp = new Permutation("(abcedfghijk)", TESTA1);
        Permutation temp2 = new Permutation("(abcedf) (ghijk)", TESTA1);
        assertFalse(TEST1.derangement());
        assertTrue(temp.derangement());
        assertTrue(temp2.derangement());
    }

}
