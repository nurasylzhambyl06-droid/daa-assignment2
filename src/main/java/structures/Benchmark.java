package structures;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class Benchmark {

    private static final int[] SIZES = {100, 1_000, 10_000, 100_000};
    private static final int TOTAL_RUNS = 6;
    private static final int SEED = 42;

    public static void main(String[] args) throws IOException {
        try (FileWriter writer = new FileWriter("results/results.csv")) {
            writer.write("workload,variant,structure,n,time_ms,steps,moves,comparisons\n");

            for (int n : SIZES) {
                runW1RandomAccess(n, writer);
                runW2Search(n, writer);
                runW3InsertRemove(n, "head", writer);
                runW3InsertRemove(n, "middle", writer);
                runW4PriorityProcessing(n, writer);
            }
        }
        System.out.println("Done. Results written to results/results.csv");
    }

    private static void runW1RandomAccess(int n, FileWriter writer) throws IOException {
        for (String structureName : new String[]{"DynamicArray", "MyLinkedList"}) {
            Result r = measure(TOTAL_RUNS, () -> {
                IntList list = newStructure(structureName);
                fill(list, n, SEED);
                Random rnd = new Random(SEED + 1);
                Metrics m = list.getMetrics();
                m.reset();
                m.startTimer();
                for (int i = 0; i < 10_000; i++) {
                    list.get(rnd.nextInt(n));
                }
                m.stopTimer();
                return m;
            });
            writeRow(writer, "W1", "-", structureName, n, r);
        }
    }

    // ---------- W2: Search ----------

    private static void runW2Search(int n, FileWriter writer) throws IOException {
        for (String structureName : new String[]{"DynamicArray", "MyLinkedList"}) {
            Result r = measure(TOTAL_RUNS, () -> {
                IntList list = newStructure(structureName);
                int[] inserted = fill(list, n, SEED);
                Random rnd = new Random(SEED + 2);
                Metrics m = list.getMetrics();
                m.reset();
                m.startTimer();
                for (int i = 0; i < 1_000; i++) {
                    if (i % 2 == 0) {
                        list.contains(inserted[rnd.nextInt(inserted.length)]);
                    } else {
                        list.contains(-1 - rnd.nextInt(1_000_000));
                    }
                }
                m.stopTimer();
                return m;
            });
            writeRow(writer, "W2", "-", structureName, n, r);
        }
    }

    private static void runW3InsertRemove(int n, String variant, FileWriter writer) throws IOException {
        int fixedIndex = "head".equals(variant) ? 0 : n / 2;

        for (String structureName : new String[]{"DynamicArray", "MyLinkedList"}) {
            Result r = measure(TOTAL_RUNS, () -> {
                IntList list = newStructure(structureName);
                fill(list, n, SEED);
                Random rnd = new Random(SEED + 3);
                Metrics m = list.getMetrics();
                m.reset();
                m.startTimer();
                for (int i = 0; i < 1_000; i++) {
                    list.add(fixedIndex, rnd.nextInt());
                }
                for (int i = 0; i < 1_000; i++) {
                    list.remove(fixedIndex);
                }
                m.stopTimer();
                return m;
            });
            writeRow(writer, "W3", variant, structureName, n, r);
        }
    }

    private static void runW4PriorityProcessing(int n, FileWriter writer) throws IOException {
        Result r = measure(TOTAL_RUNS, () -> {
            MinHeap heap = new MinHeap();
            Random rnd = new Random(SEED + 4);
            Metrics m = heap.getMetrics();
            m.startTimer();
            for (int i = 0; i < n; i++) {
                heap.insert(rnd.nextInt());
            }
            int previous = Integer.MIN_VALUE;
            for (int i = 0; i < n; i++) {
                int value = heap.extractMin();
                if (value < previous) {
                    throw new IllegalStateException("Heap returned values out of order!");
                }
                previous = value;
            }
            m.stopTimer();
            return m;
        });
        writeRow(writer, "W4", "-", "MinHeap", n, r);
    }

    private interface MeasuredRun {
        Metrics run();
    }

    private record Result(double medianTimeMs, long medianSteps, long medianMoves, long medianComparisons) {}

    private static Result measure(int totalRuns, MeasuredRun task) {
        double[] times = new double[totalRuns];
        long[] steps = new long[totalRuns];
        long[] moves = new long[totalRuns];
        long[] comparisons = new long[totalRuns];

        for (int i = 0; i < totalRuns; i++) {
            Metrics m = task.run();
            times[i] = m.getElapsedMillis();
            steps[i] = m.getSteps();
            moves[i] = m.getMoves();
            comparisons[i] = m.getComparisons();
        }

        double[] keptTimes = Arrays.copyOfRange(times, 1, totalRuns);
        long[] keptSteps = Arrays.copyOfRange(steps, 1, totalRuns);
        long[] keptMoves = Arrays.copyOfRange(moves, 1, totalRuns);
        long[] keptComparisons = Arrays.copyOfRange(comparisons, 1, totalRuns);

        return new Result(
                medianDouble(keptTimes),
                medianLong(keptSteps),
                medianLong(keptMoves),
                medianLong(keptComparisons)
        );
    }

    private static void writeRow(FileWriter writer, String workload, String variant, String structure, int n, Result r) throws IOException {
        writer.write(String.format(Locale.US, "%s,%s,%s,%d,%.3f,%d,%d,%d%n",
                workload, variant, structure, n, r.medianTimeMs(), r.medianSteps(), r.medianMoves(), r.medianComparisons()));
        System.out.printf("%s | %s | %s | n=%d | %s%n", workload, variant, structure, n, r);
    }

    private static IntList newStructure(String name) {
        return "DynamicArray".equals(name) ? new DynamicArray() : new MyLinkedList();
    }

    private static int[] fill(IntList list, int n, int seed) {
        Random rnd = new Random(seed);
        int[] values = new int[n];
        for (int i = 0; i < n; i++) {
            int v = rnd.nextInt(1_000_000);
            values[i] = v;
            list.add(v);
        }
        return values;
    }

    private static double medianDouble(double[] values) {
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
