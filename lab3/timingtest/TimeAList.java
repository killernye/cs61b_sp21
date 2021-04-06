package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

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
        // TODO: 3 列 == 3个list
        // 也就是说，每一行的数据都是息息相关的；
        // 我应该写一个辅助方法，单独计算每一行的内容
        // 先创建三个链表

        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();

        for (int i = 1000; i <= 128000; i *= 2) {
            timeAList(Ns, times, opCounts, i);
        }

        printTimingTable(Ns, times, opCounts);
    }

    /**
     * 计算AList添加N个数字，需要花费的时间和调用addlast()方法的次数；
     */
    private static void timeAList(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts, int n) {
        AList<Integer> lst = new AList<>();
        int ops = 0;
        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < n; i += 1) {
            lst.addLast(i);
            ops += 1;
        }
        double timeInSeconds = sw.elapsedTime();

        Ns.addLast(n);
        times.addLast(timeInSeconds);
        opCounts.addLast(ops);
    }
}
