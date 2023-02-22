package lists;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *
 *  @author FIXME
 */

public class ListsTest {

    @Test
    public void basicRunsTest() {
        IntList input = IntList.list(1, 2, 3, 1, 2);
        IntList run1 = IntList.list(1, 2, 3);
        IntList run2 = IntList.list(1, 2);
        IntListList result = IntListList.list(run1, run2);
        assertEquals(result, Lists.naturalRuns(input));

        IntList input2 = IntList.list(5, 3, 4, 5, 7, 1);
        IntList Run1 = IntList.list(5);
        IntList Run2 = IntList.list(3, 4, 5, 7);
        IntList Run3 = IntList.list(1);
        IntListList result2 = IntListList.list(Run1, Run2, Run3);
        assertEquals(result2, Lists.naturalRuns(input2));

        IntList run3 = IntList.list(1, 2, 3, 4, 5);
        IntListList result3 = IntListList.list(run3);
        assertEquals(result3, Lists.naturalRuns(run3));

        IntList run4 = IntList.list();
        IntListList result4 = new IntListList();
        assertEquals(result4, Lists.naturalRuns(run4));
    }

    //FIXME: Add more tests!

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
