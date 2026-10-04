package structures;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class BuildHeapComparison {

    private static final int[] SIZES = {100, 1_000, 10_000, 100_000};
    private static final int TOTAL_RUNS = 6;
    private static final int SEED = 42;

    public static void main(String[] args) throws IOException {
        try (FileWriter writer = new FileWriter("results/buildheap_comparison.csv")) {
            writer.write("method,n,time_ms,comparisons,moves\n");

            for (int n : SIZES) {
                int[] data = randomArray(n, SEED);

                // Method 1: n separate insert() calls — O(n log n)
                runCase("insert_n_times", n, data, writer, () -> {
                    MinHeap heap = new MinHeap();
                    Metrics m = heap.getMetrics();
                    m.startTimer();
                    for (int x : data) {
                        heap.insert(x);
                    }
                    m.stopTimer();
                    return m;
                });

                runCase("floyd_build_heap", n, data, writer, () -> {
                    MinHeap heap = new MinHeap();
                    Metrics m = heap.getMetrics();
                    m.startTimer();
                    heap.buildHeap(data);
                    m.stopTimer();
                    return m;
                });
            }
        }
        System.out.println("Done. Results written to results/buildheap_comparison.csv");
    }

    private interface MeasuredRun {
        Metrics run();
    }

    private static void runCase(String method, int n, int[] data, FileWriter writer, MeasuredRun task) throws IOException {
        double[] times = new double[TOTAL_RUNS];
        long[] comparisons = new long[TOTAL_RUNS];
        long[] moves = new long[TOTAL_RUNS];

        for (int i = 0; i < TOTAL_RUNS; i++) {
            Metrics m = task.run();
            times[i] = m.getElapsedMillis();
            comparisons[i] = m.getComparisons();
            moves[i] = m.getMoves();
        }

        double medianTime = median(Arrays.copyOfRange(times, 1, TOTAL_RUNS));
        long medianComparisons = medianLong(Arrays.copyOfRange(comparisons, 1, TOTAL_RUNS));
        long medianMoves = medianLong(Arrays.copyOfRange(moves, 1, TOTAL_RUNS));

        writer.write(String.format("%s,%d,%.3f,%d,%d%n", method, n, medianTime, medianComparisons, medianMoves));
        System.out.printf("%s | n=%d | time=%.3fms, comparisons=%d, moves=%d%n",
                method, n, medianTime, medianComparisons, medianMoves);
    }

    private static int[] randomArray(int n, int seed) {
        Random rnd = new Random(seed);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = rnd.nextInt(1_000_000);
        return a;
    }

    private static double median(double[] values) {
        double[] sorted = values.clone();
        Arrays.sort(sorted);
        return sorted[sorted.length / 2];
    }

    private static long medianLong(long[] values) {
        long[] sorted = values.clone();
        Arrays.sort(sorted);
        return sorted[sorted.length / 2];
    }
}
