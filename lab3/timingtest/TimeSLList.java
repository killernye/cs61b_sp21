package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
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
        timeGetLast();
    }

    public static void timeGetLast() {
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();

        for (int i = 1000; i <= 128000; i *= 2) {
            timeGetLast(Ns, times, opCounts, i, 1000);
        }

        printTimingTable(Ns, times, opCounts);
    }

    private static void timeGetLast(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts, int n, int M) {
        SLList<Integer> lst = new SLList<>();
        for (int i = 0; i < n; i += 1) {
            lst.addLast(i);
        }

        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < M; i += 1)
            lst.getLast();

        double timeInSeconds = sw.elapsedTime();

        Ns.addLast(n);
        times.addLast(timeInSeconds);
        opCounts.addLast(M);
    }

}
