package structures;

public class MyLinkedList implements IntList {

    private static class Node {
        int value;
        Node next;
        Node(int value) {
            this.value = value;
        }
    }

    private Node head;
    private Node tail;
    private int size;
    private final Metrics metrics = new Metrics();

    @Override
    public void add(int x) {
        Node node = new Node(x);
        if (head == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            metrics.incrementMoves();
            tail = node;
        }
        size++;
    }

    @Override
    public void add(int index, int x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + size);
        }
        if (index == size) {
            add(x);
            return;
        }
        Node node = new Node(x);
        if (index == 0) {
            node.next = head;
            metrics.incrementMoves();
            head = node;
        } else {
            Node prev = traverseTo(index - 1);
            node.next = prev.next;
            metrics.incrementMoves();
            prev.next = node;
            metrics.incrementMoves();
        }
        size++;
    }

    @Override
    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + size);
        }
        int removedValue;
        if (index == 0) {
            removedValue = head.value;
            metrics.incrementSteps();
            head = head.next;
            metrics.incrementMoves();
            if (head == null) {
                tail = null;
            }
        } else {
            Node prev = traverseTo(index - 1);
            Node target = prev.next;
            removedValue = target.value;
            metrics.incrementSteps();
            prev.next = target.next;
            metrics.incrementMoves();
            if (target == tail) {
                tail = prev;
            }
        }
        size--;
        return removedValue;
    }

    @Override
    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + size);
        }
        Node node = traverseTo(index);
        metrics.incrementSteps();
        return node.value;
    }

    @Override
    public boolean contains(int x) {
        Node current = head;
        while (current != null) {
            metrics.incrementSteps();
            metrics.incrementComparisons();
            if (current.value == x) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Metrics getMetrics() {
        return metrics;
    }


    private Node traverseTo(int index) {
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
            metrics.incrementSteps();
        }
        return current;
    }
}
