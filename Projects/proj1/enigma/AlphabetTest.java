package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

/** The suite of all JUnit tests for the Alphabet class.
 *  @author Alana Li
 */

public class AlphabetTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    private static final Alphabet TEST1 = new Alphabet("abcdefg");
    private static final Alphabet TEST2 = new Alphabet("!@#$%^&:></?,");

    @Test
    public void sizeTest() {
        assertEquals(7, TEST1.size());
        assertEquals(13, TEST2.size());
    }

    @Test
    public void containsTest() {
        assertTrue(TEST1.contains('a'));
        assertFalse(TEST1.contains('A'));
        assertTrue(TEST2.contains('^'));
        assertFalse(TEST2.contains('}'));
    }

    @Test
    public void toCharTest() {
        assertEquals('a', TEST1.toChar(0));
        assertEquals('g', TEST1.toChar(6));
    }

    @Test
    public void toIntTest() {
        assertEquals(2, TEST1.toInt('c'));
        assertEquals(5, TEST2.toInt('^'));
    }

}
