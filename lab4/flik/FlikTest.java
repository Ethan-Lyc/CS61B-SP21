package flik;

import org.junit.Test;
import org.junit.*;

import static org.junit.Assert.assertTrue;

public class FlikTest {
    @Test
    public void numberTest() {
        for (int i = 0; i < 1000; i += 1) {
            assertTrue(Flik.isSameNumber(i,i));
        }
    }
}
