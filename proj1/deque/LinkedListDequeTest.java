package deque;

import org.junit.Test;
import static org.junit.Assert.*;


/** Performs some basic linked list tests. */
public class LinkedListDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

        LinkedListDeque<String> lld1 = new LinkedListDeque<String>();

		assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
		lld1.addFirst("front");

        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

		lld1.addLast("middle");
		assertEquals(2, lld1.size());

		lld1.addLast("back");
		assertEquals(3, lld1.size());

		System.out.println("Printing out deque: ");
		lld1.printDeque();
    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {


        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
		// should be empty
		assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

		lld1.addFirst(10);
		// should not be empty
		assertFalse("lld1 should contain 1 item", lld1.isEmpty());

		int actual = lld1.removeFirst();
		// should be empty
		assertTrue("lld1 should be empty after removal", lld1.isEmpty());
        assertEquals(10, actual);

        LinkedListDeque<String> lld2 = new LinkedListDeque<>();
        lld2 = lld2.of("first", "second", "third", "fourth", "fifth");
        assertEquals(5, lld2.size());

        assertEquals("first", lld2.removeFirst());
        assertEquals(4, lld2.size());

        assertEquals("second", lld2.removeFirst());
        assertEquals(3, lld2.size());

        lld2.addFirst("second");

        assertEquals("second", lld2.removeFirst());
        assertEquals(3, lld2.size());

        assertEquals("third", lld2.removeFirst());
        assertEquals(2, lld2.size());

        assertEquals("fourth", lld2.removeFirst());
        assertEquals(1, lld2.size());

        assertEquals("fifth", lld2.removeFirst());
        assertEquals(0, lld2.size());

        assertEquals(null, lld2.removeFirst());
        assertEquals(0, lld2.size());
    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);

        LinkedListDeque<String> lld2 = new LinkedListDeque<>();
        lld2 = lld2.of("fifth", "fourth", "third", "second", "first");
        assertEquals(5, lld2.size());

        assertEquals("first", lld2.removeLast());
        assertEquals(4, lld2.size());

        assertEquals("second", lld2.removeLast());
        assertEquals(3, lld2.size());

        lld2.addLast("second");

        assertEquals("second", lld2.removeLast());
        assertEquals(3, lld2.size());

        assertEquals("third", lld2.removeLast());
        assertEquals(2, lld2.size());

        assertEquals("fourth", lld2.removeLast());
        assertEquals(1, lld2.size());

        assertEquals("fifth", lld2.removeLast());
        assertEquals(0, lld2.size());

        assertEquals(null, lld2.removeLast());
        assertEquals(0, lld2.size());
    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {


        LinkedListDeque<String>  lld1 = new LinkedListDeque<String>();
        LinkedListDeque<Double>  lld2 = new LinkedListDeque<Double>();
        LinkedListDeque<Boolean> lld3 = new LinkedListDeque<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();
    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());

    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {


        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }
    }

    @Test
    /* Check if get method implemented correctly. */
    public void getTest() {
        LinkedListDeque<String> lld2 = new LinkedListDeque<>();
        lld2 = lld2.of("fifth", "fourth", "third", "second", "first");
        assertEquals("fifth", lld2.get(0));
        assertEquals("third", lld2.get(2));
        assertEquals("first", lld2.get(4));
    }


    @Test
    public void equalsTest() {
        LinkedListDeque<String> lld2 = new LinkedListDeque<>();
        lld2 = lld2.of("fifth", "fourth", "third", "second", "first");

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addLast(55555);

        assertEquals(false, lld2.equals(lld1));

        LinkedListDeque<String> lld3 = new LinkedListDeque<>();
        lld3 = lld3.of("fifth", "fourth", "third", "second", "first");

        assertEquals(true, lld2.equals(lld3));
    }

    @Test
    public void getRecursiveTest() {
        LinkedListDeque<String> lld2 = new LinkedListDeque<>();
        lld2 = lld2.of("fifth", "fourth", "third", "second", "first");
        assertEquals("fifth", lld2.getRecursive(0));
        assertEquals("third", lld2.getRecursive(2));
        assertEquals("first", lld2.getRecursive(4));
    }

}
