package timingtest;
import deque.ArrayDeque;
import deque.LinkedListDeque;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(LinkedListDeque<Integer> Ns, LinkedListDeque<Double> times, LinkedListDeque<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        LinkedListDeque<Integer> Ns = new LinkedListDeque<>();
        LinkedListDeque<Double> times = new LinkedListDeque<>();
        LinkedListDeque<Integer> opCounts = new LinkedListDeque<>();
        for(int i = 1000; i <= 128000; i *= 2){
            SLList<Integer> cur = new SLList<>();
            Ns.addLast(i);
            opCounts.addLast(10000);
            for(int j = 0; j < i; j += 1){
                cur.addLast(1);
            }
            Stopwatch sw = new Stopwatch();
            for(int k = 0; k < 10000; k += 1){
                cur.getLast();
            }
            times.addLast(sw.elapsedTime());
        }
        printTimingTable(Ns,times,opCounts);
    }

}
