package structures;

public class Metrics {

    private long steps = 0;
    private long moves = 0;
    private long comparisons = 0;

    private long startTimeNanos;
    private long elapsedNanos;

    public void startTimer() {
        startTimeNanos = System.nanoTime();
    }

    public void stopTimer() {
        elapsedNanos += System.nanoTime() - startTimeNanos;
    }

    public double getElapsedMillis() {
        return elapsedNanos / 1_000_000.0;
    }

    public void incrementSteps() {
        steps++;
    }

    public void incrementSteps(long n) {
        steps += n;
    }

    public void incrementMoves() {
        moves++;
    }

    public void incrementComparisons() {
        comparisons++;
    }

    public long getSteps() {
        return steps;
    }

    public long getMoves() {
        return moves;
    }

    public long getComparisons() {
        return comparisons;
    }

    public void reset() {
        steps = 0;
        moves = 0;
        comparisons = 0;
        elapsedNanos = 0;
    }

    @Override
    public String toString() {
        return String.format("time=%.3fms, steps=%d, moves=%d, comparisons=%d",
                getElapsedMillis(), steps, moves, comparisons);
    }
}
