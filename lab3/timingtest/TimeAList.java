package timingtest;
import edu.princeton.cs.algs4.Stopwatch;
import randomizedtest.AListNoResizing;

import javax.swing.*;

/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
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
        AList<Integer> N = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCount = new AList<>();
        for(int op = 1000; op <= 128000; op *= 2){
            N.addLast(op);
            opCount.addLast(op);
            AList<Integer> cur = new AList<>();
            Stopwatch sw = new Stopwatch();
            for(int i = 0; i < op; i += 1) {
                cur.addLast(1);
            }
            times.addLast(sw.elapsedTime());
        }
        printTimingTable(N,times,opCount);
    }
}
