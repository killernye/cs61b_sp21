package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {

    @Test
    public void simpleComparisonTest() {
        AListNoResizing<Integer> expected = new AListNoResizing<>();
        BuggyAList<Integer> actual = new BuggyAList<>();

        expected.addLast(4);
        expected.addLast(5);
        expected.addLast(6);

        actual.addLast(4);
        actual.addLast(5);
        actual.addLast(6);

        assertEquals(expected.removeLast(), actual.removeLast());
        assertEquals(expected.removeLast(), actual.removeLast());
        assertEquals(expected.removeLast(), actual.removeLast());
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> correct = new AListNoResizing<>();
        BuggyAList<Integer> broken = new BuggyAList<>();
        int N = 50000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                correct.addLast(randVal);
                broken.addLast(randVal);
                //System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int sizeOfCorrect = correct.size();
                int sizeOfBroken = broken.size();
                assertEquals(sizeOfCorrect, sizeOfBroken);
            } else if (operationNumber == 2) {
                // getLast
                if (correct.size() > 0 && broken.size() > 0) {
                    assertEquals(correct.getLast(), broken.getLast());
                }
            } else {
                if (correct.size() > 0 && broken.size() > 0) {
                    assertEquals(correct.removeLast(), broken.removeLast());
                }
            }
        }
    }

}
