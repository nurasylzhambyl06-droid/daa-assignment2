package structures;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DynamicArrayTest {

    @Test
    void getThrowsOnEmptyStructure() {
        DynamicArray a = new DynamicArray();
        assertThrows(IndexOutOfBoundsException.class, () -> a.get(0));
    }

    @Test
    void removeThrowsOnEmptyStructure() {
        DynamicArray a = new DynamicArray();
        assertThrows(IndexOutOfBoundsException.class, () -> a.remove(0));
    }

    @Test
    void handlesSingleElement() {
        DynamicArray a = new DynamicArray();
        a.add(42);
        assertEquals(1, a.size());
        assertEquals(42, a.get(0));
        assertTrue(a.contains(42));
        assertFalse(a.contains(99));
    }

    @Test
    void handlesDuplicateValues() {
        DynamicArray a = new DynamicArray();
        a.add(5);
        a.add(5);
        a.add(5);
        assertEquals(3, a.size());
        assertTrue(a.contains(5));
        a.remove(0);
        assertEquals(2, a.size());
        assertTrue(a.contains(5));
    }

    @Test
    void getFirstAndLastIndex() {
        DynamicArray a = new DynamicArray();
        for (int i = 0; i < 10; i++) a.add(i);
        assertEquals(0, a.get(0));
        assertEquals(9, a.get(9));
    }

    @Test
    void addThrowsOnInvalidIndex() {
        DynamicArray a = new DynamicArray();
        a.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> a.add(-1, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> a.add(2, 5));
    }

    @Test
    void getThrowsOnInvalidIndex() {
        DynamicArray a = new DynamicArray();
        a.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> a.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> a.get(-1));
    }

    @Test
    void addAtIndexShiftsCorrectly() {
        DynamicArray a = new DynamicArray();
        a.add(1);
        a.add(2);
        a.add(3);
        a.add(1, 99);
        assertEquals(4, a.size());
        assertEquals(1, a.get(0));
        assertEquals(99, a.get(1));
        assertEquals(2, a.get(2));
        assertEquals(3, a.get(3));
    }

    @Test
    void removeAtIndexShiftsCorrectly() {
        DynamicArray a = new DynamicArray();
        for (int i = 0; i < 5; i++) a.add(i);
        int removed = a.remove(2);
        assertEquals(2, removed);
        assertEquals(4, a.size());
        assertEquals(0, a.get(0));
        assertEquals(1, a.get(1));
        assertEquals(3, a.get(2));
        assertEquals(4, a.get(3));
    }

    @Test
    void growsBeyondInitialCapacity() {
        DynamicArray a = new DynamicArray(2);
        for (int i = 0; i < 100; i++) a.add(i);
        assertEquals(100, a.size());
        for (int i = 0; i < 100; i++) assertEquals(i, a.get(i));
    }

    @Test
    void matchesArrayListOnRandomOperations() {
        Random rnd = new Random(7);
        DynamicArray actual = new DynamicArray();
        List<Integer> expected = new ArrayList<>();

        for (int op = 0; op < 2000; op++) {
            int choice = rnd.nextInt(4);
            if (choice == 0 || expected.isEmpty()) {
                int value = rnd.nextInt(1000);
                actual.add(value);
                expected.add(value);
            } else if (choice == 1) {
                int index = rnd.nextInt(expected.size() + 1);
                int value = rnd.nextInt(1000);
                actual.add(index, value);
                expected.add(index, value);
            } else if (choice == 2) {
                int index = rnd.nextInt(expected.size());
                assertEquals(expected.remove(index).intValue(), actual.remove(index));
            } else {
                int value = rnd.nextInt(1000);
                assertEquals(expected.contains(value), actual.contains(value));
            }
            assertEquals(expected.size(), actual.size());
        }

        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).intValue(), actual.get(i));
        }
    }

    @Test
    void metricsAreCountedInsideMethods() {
        DynamicArray a = new DynamicArray();
        for (int i = 0; i < 50; i++) a.add(i);
        a.getMetrics().reset();

        a.get(10);
        assertEquals(1, a.getMetrics().getSteps());

        a.contains(10);
        assertTrue(a.getMetrics().getComparisons() > 0);
    }
}
