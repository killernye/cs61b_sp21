package tester;

import static org.junit.Assert.*;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {

    @Test
    public void testTest() {
        String a = "55363\n56463";

        System.out.println(a);
    }


    @Test
    public void randomizedTest() {
        StudentArrayDeque<Integer> sad = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ads = new ArrayDequeSolution<>();
        String errorMessages = "\n";
        for (int i = 0; i < 500000; i += 1) {
            int operationNumber = StdRandom.uniform(0, 8);
            if (operationNumber == 0) {
                int randVal = StdRandom.uniform(0, 100);
                errorMessages += "addFirst(" + randVal + ")\n";
                sad.addFirst(randVal);
                ads.addFirst(randVal);
            } else if (operationNumber == 1) {
                int randVal = StdRandom.uniform(0, 100);
                sad.addLast(randVal);
                ads.addLast(randVal);
                errorMessages += "addLast(" + randVal + ")\n";
            } else if (operationNumber == 2) {
                //errorMessages += "isEmpty()\n";
                assertEquals(errorMessages, ads.isEmpty(), sad.isEmpty());
            } else if (operationNumber == 3) {
                //errorMessages += "size()\n";
                assertEquals(errorMessages, ads.size(), sad.size());
            } else if (operationNumber == 4) {
                errorMessages += "removeFirst()\n";
                assertEquals(errorMessages, ads.removeFirst(), sad.removeFirst());
            } else if (operationNumber == 5) {
                errorMessages += "removeLast()\n";
                assertEquals(errorMessages, ads.removeLast(), sad.removeLast());
            } else {
                if (ads.size() == 0) continue;
                int randIndex = StdRandom.uniform(0, ads.size());
                errorMessages += "get(" + randIndex + ")\n";
                assertEquals(errorMessages, ads.get(randIndex), sad.get(randIndex));
            }
        }
    }

}
