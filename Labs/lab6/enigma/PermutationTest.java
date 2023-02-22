package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/**
 * The suite of all JUnit tests for the Permutation class. For the purposes of
 * this lab (in order to test) this is an abstract class, but in proj1, it will
 * be a concrete class. If you want to copy your tests for proj1, you can make
 * this class concrete by removing the 4 abstract keywords and implementing the
 * 3 abstract methods.
 *
 *  @author Alana Li
 */
public abstract class PermutationTest {

    /**
     * For this lab, you must use this to get a new Permutation,
     * the equivalent to:
     * new Permutation(cycles, alphabet)
     * @return a Permutation with cycles as its cycles and alphabet as
     * its alphabet
     * @see Permutation for description of the Permutation conctructor
     */
    abstract Permutation getNewPermutation(String cycles, Alphabet alphabet);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet(chars)
     * @return an Alphabet with chars as its characters
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet(String chars);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet()
     * @return a default Alphabet with characters ABCD...Z
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet();

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    private final Alphabet testa1 = getNewAlphabet("abcdefghijk");

    private final Permutation test1 = getNewPermutation("(adc) (eb)", testa1);

    /* ***** TESTS ***** */

    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha,
                           Permutation perm, Alphabet alpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                    e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                    c, perm.invert(e));
            int ci = alpha.toInt(c), ei = alpha.toInt(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                    ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                    ci, perm.invert(ei));
        }
    }

    @Test
    public void checkIdTransform() {
        Alphabet alpha = getNewAlphabet();
        Permutation perm = getNewPermutation("", alpha);
        checkPerm("identity", UPPER_STRING, UPPER_STRING, perm, alpha);
    }

    @Test
    public void permuteIntTest() {
        assertEquals(3, test1.permute(0));
        assertEquals(0, test1.permute(2));
        assertEquals(1, test1.permute(4));
        assertEquals(4, test1.permute(1));
        assertEquals(9, test1.permute(9));
        assertEquals(10, test1.permute(10));
        assertEquals(4, test1.permute(12));

    }

    @Test
    public void invertIntTest() {
        assertEquals(2, test1.invert(0));
        assertEquals(0, test1.invert(3));
        assertEquals(4, test1.invert(1));
        assertEquals(1, test1.invert(4));
        assertEquals(9, test1.invert(9));
        assertEquals(10, test1.invert(10));
        assertEquals(4, test1.invert(12));
    }

    @Test
    public void permuteCharTest() {
        assertEquals('d', test1.permute('a'));
        assertEquals('c', test1.permute('d'));
        assertEquals('a', test1.permute('c'));
        assertEquals('e', test1.permute('b'));
        assertEquals('b', test1.permute('e'));
        assertEquals('i', test1.permute('i'));
        assertEquals('j', test1.permute('j'));
    }

    @Test
    public void invertCharTest() {
        assertEquals('c', test1.invert('a'));
        assertEquals('a', test1.invert('d'));
        assertEquals('d', test1.invert('c'));
        assertEquals('b', test1.invert('e'));
        assertEquals('e', test1.invert('b'));
        assertEquals('i', test1.invert('i'));
        assertEquals('j', test1.invert('j'));
    }

    @Test
    public void derangementTest() {
        Permutation temp = getNewPermutation("(abcedfghijk)", testa1);
        Permutation temp2 = getNewPermutation("(abcedf) (ghijk)", testa1);
        assertFalse(test1.derangement());
        assertTrue(temp.derangement());
        assertTrue(temp2.derangement());
    }
}
