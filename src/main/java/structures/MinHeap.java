package structures;

public class MinHeap {

    private int[] data;
    private int size;
    private final Metrics metrics = new Metrics();

    public MinHeap() {
        this(8);
    }

    public MinHeap(int initialCapacity) {
        data = new int[Math.max(initialCapacity, 1)];
        size = 0;
    }

    public void insert(int x) {
        ensureCapacity(size + 1);
        data[size] = x;
        metrics.incrementMoves();
        bubbleUp(size);
        size++;
    }

    public int peekMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        metrics.incrementSteps();
        return data[0];
    }

    public int extractMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        int min = data[0];
        metrics.incrementSteps();

        size--;
        data[0] = data[size];
        metrics.incrementMoves();

        if (size > 0) {
            bubbleDown(0);
        }
        return min;
    }

    public int size() {
        return size;
    }

    public boolean isValid() {
        for (int i = 1; i < size; i++) {
            int parent = (i - 1) / 2;
            if (data[parent] > data[i]) {
                return false;
            }
        }
        return true;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public void buildHeap(int[] array) {
        ensureCapacity(array.length);
        System.arraycopy(array, 0, data, 0, array.length);
        size = array.length;

        int lastParent = (size / 2) - 1;
        for (int i = lastParent; i >= 0; i--) {
            bubbleDown(i);
        }
    }

    private void bubbleUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            metrics.incrementComparisons();
            if (data[index] >= data[parent]) {
                break;
            }
            swap(index, parent);
            index = parent;
        }
    }

    private void bubbleDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size) {
                metrics.incrementComparisons();
                if (data[left] < data[smallest]) {
                    smallest = left;
                }
            }
            if (right < size) {
                metrics.incrementComparisons();
                if (data[right] < data[smallest]) {
                    smallest = right;
                }
            }
            if (smallest == index) {
                break;
            }
            swap(index, smallest);
            index = smallest;
        }
    }

    private void swap(int i, int j) {
        int tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
        metrics.incrementMoves();
        metrics.incrementMoves();
    }

    private void ensureCapacity(int required) {
        if (required <= data.length) {
            return;
        }
        int newCapacity = Math.max(data.length * 2, required);
        int[] newData = new int[newCapacity];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
    }
}
