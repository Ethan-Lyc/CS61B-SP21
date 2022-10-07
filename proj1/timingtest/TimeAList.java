package timingtest;

import deque.ArrayDeque;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(ArrayDeque<Integer> Ns, ArrayDeque<Double> times, ArrayDeque<Integer> opCounts) {
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
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE
        ArrayDeque<Integer> N = new ArrayDeque<>();
        ArrayDeque<Double> times = new ArrayDeque<>();
        ArrayDeque<Integer> opCount = new ArrayDeque<>();
        for(int op = 1000; op <= 128000; op *= 2){
            N.addLast(op);
            opCount.addLast(op);
            ArrayDeque<Integer> cur = new ArrayDeque<>();
            Stopwatch sw = new Stopwatch();
            for(int i = 0; i < op; i += 1) {
                cur.addLast(1);
            }
            times.addLast(sw.elapsedTime());
        }
        printTimingTable(N,times,opCount);
    }
}
