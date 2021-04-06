package deque;

import net.sf.saxon.expr.Component;
import net.sf.saxon.functions.Minimax;
import org.junit.Test;
import static org.junit.Assert.*;

public class MaxArrayDequeTest {




    @Test
    public void testMax() {

        MaxArrayDeque.IntegerComparator ic = new MaxArrayDeque.IntegerComparator();
        MaxArrayDeque<Integer> mad1 = new MaxArrayDeque<>(ic);
        //MaxArrayDeque<String> mad2 = new MaxArrayDeque<>(ic);

        mad1 = mad1.of(1,52,5,64,66,3,7,5,8,48,54,8,6,2,6,25,252);

        Integer expected = 252;
        assertEquals(expected, mad1.max());

    }

}
