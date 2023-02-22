import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author Alana Li
 */
public class BSTStringSetTest  {
    private static final ArrayList<String> animals = new ArrayList<>();
    private static BSTStringSet _set = new BSTStringSet();
    private static ArrayList<String> ans = new ArrayList<>();

    @Test
    public void testNothing() {
        ans.add("doggo");
        ans.add("fishy");
        ans.add("gato");
        ans.add("panda");

        animals.add("panda");
        animals.add("doggo");
        animals.add("gato");
        animals.add("fishy");
        for (String s : animals) {
            _set.put(s);
        }
        assertEquals(ans, _set.asList());
    }
}
