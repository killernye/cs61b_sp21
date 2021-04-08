package flik;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

public class testFlik {

    @Test
    public void testIsSameNumber() {
        Integer a = 127;
        Integer b = 127;
        assertTrue(a== b);
        Integer c = 128;
        Integer d = 128;
        assertTrue(c == d);
    }
}
