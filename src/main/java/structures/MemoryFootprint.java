package structures;

import org.openjdk.jol.info.GraphLayout;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;


public class MemoryFootprint {

    private static final int[] SIZES = {100, 1_000, 10_000, 100_000};

    public static void main(String[] args) throws IOException {
        try (FileWriter writer = new FileWriter("results/memory.csv")) {
            writer.write("structure,n,bytes,mb\n");

            for (int n : SIZES) {
                measure("DynamicArray", buildDynamicArray(n), n, writer);
                measure("MyLinkedList", buildLinkedList(n), n, writer);
                measure("MinHeap", buildHeap(n), n, writer);
            }
        }
        System.out.println("Done. Results written to results/memory.csv");
    }

    private static void measure(String name, Object structure, int n, FileWriter writer) throws IOException {
        long bytes = GraphLayout.parseInstance(structure).totalSize();
        double mb = bytes / (1024.0 * 1024.0);
        writer.write(String.format(Locale.US, "%s,%d,%d,%.4f%n", name, n, bytes, mb));
        System.out.printf("%s | n=%d | %d bytes (%.4f MB)%n", name, n, bytes, mb);
    }

    private static DynamicArray buildDynamicArray(int n) {
        DynamicArray a = new DynamicArray();
        for (int i = 0; i < n; i++) a.add(i);
        return a;
    }

    private static MyLinkedList buildLinkedList(int n) {
        MyLinkedList l = new MyLinkedList();
        for (int i = 0; i < n; i++) l.add(i);
        return l;
    }

    private static MinHeap buildHeap(int n) {
        MinHeap h = new MinHeap();
        for (int i = 0; i < n; i++) h.insert(i);
        return h;
    }
}
