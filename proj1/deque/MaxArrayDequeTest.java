package deque;

import jh61b.junit.In;
import net.sf.saxon.functions.Minimax;
import org.apache.commons.math3.analysis.function.Max;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Comparator;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;

public class MaxArrayDequeTest {
    @Test
    public void maxStringTest(){
        Comparator<String> c = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int res = 0;
                if(o1 == null && o2 != null){
                    return res = -1;
                }else if(o1 != null && o2 == null){
                    return res = 1;
                }else if(o1 == null && o2 == null){
                    return res = 0;
                }else if(o1 != null && o2 != null){
                    if(o1.length() > o2.length()){
                        return res = 1;
                    }else if(o1.length() == o2.length()){
                        return  res = 0;
                    }else{
                        return  res = -1;
                    }
                }
                return res;
            }

        };
        MaxArrayDeque<String> deque = new MaxArrayDeque<>(c);
        deque.addLast("nimabi");
        deque.addLast("caonimade");
        String s = deque.max();
        assertEquals("返回的是caonimade",s,"caonimade");
        System.out.println(s.toString());
    }
    @Test
    public void maxIntegerTest(){
        MaxArrayDeque<Integer> deque = new MaxArrayDeque<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if(o1 > o2){
                    return 1;
                }else if(o1 == o2){
                    return 0;
                }else{
                    return -1;
                }
            }
        });
        deque.addLast(1);
        deque.addLast(100);
        deque.addLast(123);
        Integer max = deque.max();
        assertEquals("答案是123",max.intValue(),123);
    }
    @Test
    public void maxItem(){
        MaxArrayDeque<Integer> deque = new MaxArrayDeque<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if(o1 > o2){
                    return 1;
                }else if(o2 > o1){
                    return  -1;
                }else{
                    return 0;
                }
            }
        });
        for(int i = 0; i < 500; i += 1){
            deque.addLast(i);
        }
        Integer max = deque.max(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if(o1 < o2){
                    return 1;
                }else if(o1 > o2){
                    return -1;
                }else {
                    return 0;
                }
            }
        });
        assertEquals(0,(long)max);

    }
    @Test
    public void maxWithoutComparatorTest() {
        MaxArrayDeque<Integer> mad = new MaxArrayDeque<>(new IntComparator());

        for (int i = 0; i < 5; i++) {
            mad.addLast(i);
        }

        assertEquals((Integer) 4, mad.max());
    }

    @Test
    public void maxWithComparatorTest() {
        MaxArrayDeque<String> mad = new MaxArrayDeque<>(new StringComparator());

        mad.addLast("Java is good!");
        mad.addLast("java is good");

        assertEquals("java is good", mad.max());
        assertEquals("Java is good!", mad.max(new StringLengthComparator()));
    }

    private static class IntComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer i1, Integer i2) {
            return i1 - i2;
        }
    }

    private static class StringComparator implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            int l1 = s1.length();
            int l2 = s2.length();

            for (int i = 0; i < Math.min(l1, l2); i++) {
                int s1Char = s1.charAt(i);
                int s2Char = s2.charAt(i);

                if (s1Char != s2Char) {
                    return s1Char - s2Char;
                }
            }

            if (l1 != l2) {
                return l1 - l2;
            }
            return 0;
        }
    }

    private static class StringLengthComparator implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            return s1.length() - s2.length();
        }
    }
}
