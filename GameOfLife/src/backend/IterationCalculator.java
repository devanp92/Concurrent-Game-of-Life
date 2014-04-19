package backend;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by devan on 4/8/14.
 */
public class IterationCalculator {

    private Grid grid;
    private Grid newGridToSet;

    private Calculator[] calculators;

    public IterationCalculator(Grid grid) throws Exception {
        if (grid == null) {
            throw new NullPointerException("The grid is null");
        }
        this.grid = grid;
        this.newGridToSet = new Grid(grid.getNumRows());
    }

    public Cell[][] calculateNewIteration() throws Exception {
        initializeCalculators();
        startThreads();
        newGridToSet.setGrid(joinThreads());
        return joinThreads();
    }


    private Cell[][] joinThreads() throws InterruptedException {
        for (Thread calculator : calculators) {
            calculator.join();
        }
        return newGridToSet.convertGridTo2DArray();
    }

    private void startThreads() {
        for (Thread calculator : calculators) {
            calculator.start();
        }
    }

    public void initializeCalculators() throws RuntimeException, InterruptedException {
        int numThreads = numThreads();
        calculators = new Calculator[numThreads];
        int numCellsPerThread = grid.numRows * grid.numRows / numThreads;
        List<AtomicReference[]> listOfSubSets = findSubSetsOfCellsForThread(numCellsPerThread);

        RuleChecker ruleChecker = new RuleChecker(grid);
        for (int i = 0; i < numThreads; i++) {
            AtomicReference[] cells = listOfSubSets.get(i);
            calculators[i] = new Calculator(cells, ruleChecker);
        }
    }

    /**
     * Finds subsets of cells from grid depending on how many cells per set
     *
     * @param numThreads How many cells you want per array
     * @return List of arrays that contains an array of cells
     */
    public List<AtomicReference[]> findSubSetsOfCellsForThread(int numThreads) {

        int gridSize = (int) Math.pow(grid.numRows, 2);
        int numCellsPerThread = gridSize / numThreads;
        List<AtomicReference[]> list = new ArrayList<>();

        for (int i = 0; i < gridSize; i += numCellsPerThread) {
            AtomicReference[] subSet;
            if (list.size() + 1 == numThreads) {
                subSet = grid.getSubSetOfGrid(i, gridSize);
                list.add(subSet);
                break;
            } else {
                subSet = grid.getSubSetOfGrid(i, i + numCellsPerThread);
                list.add(subSet);
            }
        }
        return list;
    }

    public int numThreads() throws RuntimeException {
        int cores = Runtime.getRuntime().availableProcessors();
        if (cores < 1) {
            throw new RuntimeException("Number of processors is less than 1");
        }
        return cores;
    }

    public Thread[] getCalculators() {
        return calculators;
    }

    //TODO make it extend thread and add method to return new cells
    private class Calculator extends Thread{
        private AtomicReference[] cells;
        private RuleChecker ruleChecker;

        public Calculator(AtomicReference[] cells, RuleChecker ruleChecker) {
            this.cells = cells;
            this.ruleChecker = ruleChecker;
        }

        @Override
        public void run() {
            for (AtomicReference atomicReference : cells) {
                Cell cell = (Cell) atomicReference.get();
                Cell nextIterationCell = null;
                try {
                    nextIterationCell = ruleChecker.determineCellsNextState(cell);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                assert nextIterationCell != null;
                newGridToSet.setCell(nextIterationCell);
            }
        }
        public AtomicReference[] getCells(){
            return cells;
        }
    }

}