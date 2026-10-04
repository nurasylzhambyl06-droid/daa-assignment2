# DAA Assignment 2 — In-Memory Workload Engine

DynamicArray, MyLinkedList and MinHeap implemented from scratch (no
`java.util.ArrayList`, `LinkedList` or `PriorityQueue` in the implementations
themselves — only in tests, as a reference). Includes honest operation
counters (steps/moves/comparisons), a benchmark across 4 workloads, and both
bonus tasks (JOL memory footprint, Floyd's O(n) buildHeap).

## Requirements

- Java 17+
- Maven 3.6+

## Build

```bash
mvn compile
```

## Run tests

```bash
mvn test
```

## Run the main benchmark (W1-W4)

```bash
mvn compile exec:java -Dexec.mainClass="structures.Benchmark"
```

Writes `results/results.csv` with columns:
`workload,variant,structure,n,time_ms,steps,moves,comparisons`

Each case runs 6 times; the first (JVM warm-up) run is discarded and the
median of the remaining 5 is kept.

## Run the bonus tasks

**Task A — memory footprint (JOL):**
```bash
mvn compile exec:java -Dexec.mainClass="structures.MemoryFootprint"
```
Writes `results/memory.csv` with real, JOL-measured byte sizes per structure per n.

**Task B — Floyd's buildHeap vs n inserts:**
```bash
mvn compile exec:java -Dexec.mainClass="structures.BuildHeapComparison"
```
Writes `results/buildheap_comparison.csv`.

## Project layout

```
src/main/java/structures/
├── Metrics.java              — steps/moves/comparisons/time counters, owned per structure instance
├── IntList.java                — shared interface (add/add(index)/remove/get/contains/size)
├── DynamicArray.java            — resizable array, 2x growth
├── MyLinkedList.java            — singly linked list, head+tail pointers
├── MinHeap.java                 — array-based binary min-heap, insert/peekMin/extractMin
│                                   + bonus Floyd buildHeap(int[])
├── Benchmark.java                — runs W1-W4, writes results/results.csv
├── MemoryFootprint.java          — bonus A, writes results/memory.csv
└── BuildHeapComparison.java      — bonus B, writes results/buildheap_comparison.csv

src/test/java/structures/
├── DynamicArrayTest.java
├── MyLinkedListTest.java
└── MinHeapTest.java

results/
├── results.csv
├── memory.csv
├── buildheap_comparison.csv
└── plots/
```

## Git workflow

Branches: `feature/array`, `feature/list`, `feature/heap`, `feature/metrics`,
merged into `main`, tagged `v1.0`.

```bash
git init
git checkout -b feature/array
# add Metrics.java, IntList.java, DynamicArray.java, DynamicArrayTest.java
git add .
git commit -m "feat(array): add DynamicArray with 2x growth and honest counters"

git checkout main 2>/dev/null || git checkout -b main
git merge feature/array

git checkout -b feature/list
# add MyLinkedList.java, MyLinkedListTest.java
git add .
git commit -m "feat(list): add MyLinkedList with head/tail pointers"

git checkout main
git merge feature/list

git checkout -b feature/heap
# add MinHeap.java, MinHeapTest.java
git add .
git commit -m "feat(heap): add MinHeap with insert/extractMin"
git commit -m "feat(heap): add Floyd's O(n) buildHeap (bonus task B)"
git commit -m "test(heap): verify heap property and sorted extraction order"

git checkout main
git merge feature/heap

git checkout -b feature/metrics
# add Benchmark.java, MemoryFootprint.java, BuildHeapComparison.java
git add .
git commit -m "feat(metrics): add benchmark for W1-W4 workloads"
git commit -m "feat(metrics): add JOL memory footprint (bonus task A)"

git checkout main
git merge feature/metrics

git add REPORT.md README.md
git commit -m "docs(report): add complexity table, loop invariant proofs and discussion"

git tag v1.0
git remote add origin <your-repo-url>
git push -u origin main --tags
```

## Submission checklist

- [ ] Source code (Maven project): 3 structures + Metrics + JUnit 5 tests
- [ ] `results/results.csv` from a full benchmark run (W1-W4)
- [ ] `results/plots/` — Time vs n and Steps/Moves/Comparisons vs n, one chart per workload
- [ ] `REPORT.md` filled in completely (complexity table, 2 loop invariant proofs, plots, discussion)
- [ ] Pushed to GitHub, `main` branch, tagged `v1.0`
- [ ] ZIP named `DAA_Assignment2_name_surname_group.zip` uploaded to Moodle with a link to the GitHub repository
- [ ] (Optional) Bonus A: `results/memory.csv` + Memory vs n plot + explanation
- [ ] (Optional) Bonus B: `results/buildheap_comparison.csv` + explanation
