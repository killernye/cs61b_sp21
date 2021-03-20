package IntList;

import static org.junit.Assert.*;
import org.junit.Test;

public class SquarePrimesTest {

    /**
     * Here is a test for isPrime method. Try running it.
     * It passes, but the starter code implementation of isPrime
     * is broken. Write your own JUnit Test to try to uncover the bug!
     */
    @Test
    public void testSquarePrimesSimple() {
        IntList lst = IntList.of(14, 15, 16, 17, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 289 -> 18", lst.toString());
        assertTrue(changed);
    }

    @Test
    public void testSquarePrimeSimple2() {
        IntList lst = IntList.of(-1, 0, 1, 2, 3, 5, 6, 10);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("-1 -> 0 -> 1 -> 4 -> 9 -> 25 -> 6 -> 10", lst.toString());
        assertTrue(changed);
    }
}
