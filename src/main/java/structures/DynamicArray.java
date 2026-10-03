package structures;

public class DynamicArray implements IntList {

    private int[] data;
    private int size;
    private final Metrics metrics = new Metrics();

    public DynamicArray() {
        this(4);
    }

    public DynamicArray(int initialCapacity) {
        data = new int[Math.max(initialCapacity, 1)];
        size = 0;
    }

    @Override
    public void add(int x) {
        ensureCapacity(size + 1);
        data[size] = x;
        metrics.incrementMoves();
        size++;
    }

    @Override
    public void add(int index, int x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + size);
        }
        ensureCapacity(size + 1);

        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
            metrics.incrementMoves();
        }
        data[index] = x;
        metrics.incrementMoves();
        size++;
    }

    @Override
    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + size);
        }
        int removed = data[index];
        metrics.incrementSteps();

        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
            metrics.incrementMoves();
        }
        size--;
        return removed;
    }

    @Override
    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + size);
        }
        metrics.incrementSteps();
        return data[index];
    }

    @Override
    public boolean contains(int x) {
        for (int i = 0; i < size; i++) {
            metrics.incrementSteps();
            metrics.incrementComparisons();
            if (data[i] == x) {
                return true;
            }
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


    private void ensureCapacity(int required) {
        if (required <= data.length) {
            return;
        }
        int newCapacity = data.length * 2;
        int[] newData = new int[newCapacity];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
            metrics.incrementMoves();
        }
        data = newData;
    }
}
