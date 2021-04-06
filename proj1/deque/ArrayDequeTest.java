package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class ArrayDequeTest {
    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {
        ArrayDeque<String> ad1 = new ArrayDeque<>();
        assertTrue(ad1.isEmpty());
        assertEquals(0, ad1.size());

        ad1.addFirst("hello");
        assertFalse(ad1.isEmpty());
        assertEquals(1, ad1.size());

        ad1.addLast("world");
        assertEquals(2, ad1.size());

        ArrayDeque<Integer> ad2 = new ArrayDeque<>();
        ad2 = ad2.of(1, 2, 3, 4, 5, 10);
        assertEquals(6, ad2.size());

        ad2.addFirst(0);
        ad2.addLast(11);

        ad2.printDeque();
    }
    
    @Test
    public void realTest() {
        System.out.println(Math.floorMod(-1, 5));
    }

    @Test
    public void removeTest() {
        ArrayDeque<String> ad2 = new ArrayDeque<>();
        assertEquals(null, ad2.removeFirst());

        ad2 = ad2.of("one", "two", "three", "four", "five", "ten");

        assertEquals(6, ad2.size());
        assertEquals("one", ad2.removeFirst());

        ad2.addFirst("one");
        ad2.removeFirst();

        assertEquals(5, ad2.size());
        ad2.printDeque();


        assertEquals("ten", ad2.removeLast());
        ad2.addLast("six");
        assertEquals("six", ad2.removeLast());

        ad2.printDeque();

        assertEquals("five", ad2.get(3));
    }

    @Test
    public void addTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ad1.addLast(1);
        ad1.removeFirst();
        ad1.addLast(2);
        ad1.removeFirst();
        ad1.addLast(3);
        ad1.addLast(4);
        ad1.addLast(5);
    }


    @Test
    public void getTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        LinkedListDeque<Integer> ad2 = new LinkedListDeque<>();

        int N  = 50000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);

            if (operationNumber == 0) {
                int ranVal = StdRandom.uniform(0, 100);
                ad1.addLast(ranVal);
                ad2.addLast(ranVal);

                System.out.println("ADD last number " + ranVal);
                System.out.print("ad1: ");
                ad1.printDeque();
                System.out.print("ad2: ");
                ad2.printDeque();
            } else if (operationNumber == 1) {
                assertEquals(ad1.size(), ad2.size());
            } else if (operationNumber == 2) {
                assertEquals(ad1.removeFirst(), ad2.removeFirst());

                System.out.println("Remove first number! ");
                System.out.print("ad1: ");
                ad1.printDeque();
                System.out.print("ad2: ");
                ad2.printDeque();
            } else if (operationNumber  == 3) {
                int ranIndex = StdRandom.uniform(0, ad1.size() + 1);
                assertEquals(ad1.get(ranIndex), ad2.get(ranIndex));
            }
        }
    }

    @Test
    public void equalsTest() {
        ArrayDeque<Integer> deq1 = new ArrayDeque<>();
        LinkedListDeque<Integer> deq2 = new LinkedListDeque<>();

        for (int i = 0; i < 100; i += 1) {
            deq1.addFirst(i);
            deq2.addFirst(i);
        }

        //assertTrue(deq1.equals(deq2));

        for (int i: deq1) {
            System.out.println(i);
        }

        ArrayDeque<String> deq3 = new ArrayDeque<>();

        System.out.println(deq1.getClass());
    }

    @Test
    public void arrayTest() {
        int[] a  = {1, 2, 3, 4, 5};
        for (int k: a) {
            System.out.println(k);
        }
    }


}
