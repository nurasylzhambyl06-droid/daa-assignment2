package structures;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MyLinkedListTest {

    @Test
    void getThrowsOnEmptyStructure() {
        MyLinkedList l = new MyLinkedList();
        assertThrows(IndexOutOfBoundsException.class, () -> l.get(0));
    }

    @Test
    void removeThrowsOnEmptyStructure() {
        MyLinkedList l = new MyLinkedList();
        assertThrows(IndexOutOfBoundsException.class, () -> l.remove(0));
    }

    @Test
    void handlesSingleElement() {
        MyLinkedList l = new MyLinkedList();
        l.add(42);
        assertEquals(1, l.size());
        assertEquals(42, l.get(0));
        assertTrue(l.contains(42));
    }

    @Test
    void handlesDuplicateValues() {
        MyLinkedList l = new MyLinkedList();
        l.add(5);
        l.add(5);
        l.add(5);
        assertEquals(3, l.size());
        l.remove(0);
        assertEquals(2, l.size());
        assertTrue(l.contains(5));
    }

    @Test
    void getFirstAndLastIndex() {
        MyLinkedList l = new MyLinkedList();
        for (int i = 0; i < 10; i++) l.add(i);
        assertEquals(0, l.get(0));
        assertEquals(9, l.get(9));
    }

    @Test
    void removingHeadUpdatesHeadCorrectly() {
        MyLinkedList l = new MyLinkedList();
        l.add(1);
        l.add(2);
        l.add(3);
        int removed = l.remove(0);
        assertEquals(1, removed);
        assertEquals(2, l.get(0));
        assertEquals(2, l.size());
    }

    @Test
    void tailPointerStaysCorrectAfterRemovingLast() {
        MyLinkedList l = new MyLinkedList();
        l.add(1);
        l.add(2);
        l.add(3);
        l.remove(2);
        l.add(99);
        assertEquals(3, l.size());
        assertEquals(1, l.get(0));
        assertEquals(2, l.get(1));
        assertEquals(99, l.get(2));
    }

    @Test
    void emptiesCorrectlyAndCanBeRefilled() {
        MyLinkedList l = new MyLinkedList();
        l.add(1);
        l.remove(0);
        assertEquals(0, l.size());
        l.add(5);
        assertEquals(1, l.size());
        assertEquals(5, l.get(0));
    }

    @Test
    void addThrowsOnInvalidIndex() {
        MyLinkedList l = new MyLinkedList();
        l.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> l.add(-1, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> l.add(2, 5));
    }

    @Test
    void addAtIndexShiftsCorrectly() {
        MyLinkedList l = new MyLinkedList();
        l.add(1);
        l.add(2);
        l.add(3);
        l.add(1, 99);
        assertEquals(4, l.size());
        assertEquals(1, l.get(0));
        assertEquals(99, l.get(1));
        assertEquals(2, l.get(2));
        assertEquals(3, l.get(3));
    }

    @Test
    void matchesJavaLinkedListOnRandomOperations() {
        Random rnd = new Random(11);
        MyLinkedList actual = new MyLinkedList();
        List<Integer> expected = new LinkedList<>();

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
        MyLinkedList l = new MyLinkedList();
        for (int i = 0; i < 50; i++) l.add(i);
        l.getMetrics().reset();

        l.get(10);
        assertTrue(l.getMetrics().getSteps() >= 10);
    }
}
