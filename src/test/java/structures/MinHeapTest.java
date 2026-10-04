package structures;

import org.junit.jupiter.api.Test;

import java.util.PriorityQueue;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MinHeapTest {

    @Test
    void peekMinThrowsOnEmptyHeap() {
        MinHeap h = new MinHeap();
        assertThrows(IllegalStateException.class, h::peekMin);
    }

    @Test
    void extractMinThrowsOnEmptyHeap() {
        MinHeap h = new MinHeap();
        assertThrows(IllegalStateException.class, h::extractMin);
    }

    @Test
    void handlesSingleElement() {
        MinHeap h = new MinHeap();
        h.insert(42);
        assertEquals(42, h.peekMin());
        assertEquals(42, h.extractMin());
        assertEquals(0, h.size());
    }

    @Test
    void handlesDuplicateValues() {
        MinHeap h = new MinHeap();
        h.insert(5);
        h.insert(5);
        h.insert(5);
        assertEquals(5, h.extractMin());
        assertEquals(5, h.extractMin());
        assertEquals(5, h.extractMin());
        assertEquals(0, h.size());
    }

    @Test
    void peekMinAlwaysReturnsSmallest() {
        MinHeap h = new MinHeap();
        int[] values = {5, 3, 8, 1, 9, 2};
        int runningMin = Integer.MAX_VALUE;
        for (int v : values) {
            h.insert(v);
            runningMin = Math.min(runningMin, v);
            assertEquals(runningMin, h.peekMin());
        }
    }


    @Test
    void heapPropertyHoldsAfterEveryInsert() {
        MinHeap h = new MinHeap();
        Random rnd = new Random(3);
        for (int i = 0; i < 500; i++) {
            h.insert(rnd.nextInt(10_000));
            assertTrue(h.isValid(), "Heap property violated after insert #" + i);
        }
    }

    @Test
    void heapPropertyHoldsAfterEveryExtractMin() {
        MinHeap h = new MinHeap();
        Random rnd = new Random(4);
        for (int i = 0; i < 500; i++) {
            h.insert(rnd.nextInt(10_000));
        }
        while (h.size() > 0) {
            h.extractMin();
            if (h.size() > 0) {
                assertTrue(h.isValid(), "Heap property violated after extractMin");
            }
        }
    }


    @Test
    void extractMinReturnsSortedOrder() {
        MinHeap h = new MinHeap();
        Random rnd = new Random(5);
        int n = 1000;
        for (int i = 0; i < n; i++) {
            h.insert(rnd.nextInt(100_000));
        }
        int previous = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            int value = h.extractMin();
            assertTrue(value >= previous, "Values were not non-decreasing");
            previous = value;
        }
    }

    @Test
    void matchesJavaPriorityQueueOnRandomOperations() {
        Random rnd = new Random(13);
        MinHeap actual = new MinHeap();
        PriorityQueue<Integer> expected = new PriorityQueue<>();

        for (int i = 0; i < 1000; i++) {
            int v = rnd.nextInt(10_000);
            actual.insert(v);
            expected.add(v);
        }

        while (!expected.isEmpty()) {
            assertEquals(expected.poll().intValue(), actual.extractMin());
        }
    }

    @Test
    void buildHeapProducesValidHeap() {
        Random rnd = new Random(17);
        int[] data = new int[500];
        for (int i = 0; i < data.length; i++) data[i] = rnd.nextInt(10_000);

        MinHeap h = new MinHeap();
        h.buildHeap(data);

        assertEquals(data.length, h.size());
        assertTrue(h.isValid());

        int[] sortedExpected = data.clone();
        java.util.Arrays.sort(sortedExpected);
        for (int i = 0; i < data.length; i++) {
            assertEquals(sortedExpected[i], h.extractMin());
        }
    }
}
