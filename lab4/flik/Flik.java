package flik;

import org.junit.Test;

/** An Integer tester created by Flik Enterprises.
 * @author Josh Hug
 * */
public class Flik {
    /** @param a Value 1
     *  @param b Value 2
     *  @return Whether a and b are the same */
    public static boolean isSameNumber(Integer a, Integer b) {
        return a.equals(b);
    }
    @Test
    public void isSame(){
        int a = 100000;
        int b = 100000;
        boolean flag = isSameNumber(a,b);
        System.out.println(flag);
    }
}
