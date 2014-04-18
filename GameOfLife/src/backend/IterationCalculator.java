package backend;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by devan on 4/8/14.
 */
public class IterationCalculator {

    private Grid grid;
    private Grid newGridToSet;
    private Thread[] calculators;

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
        calculators = new Thread[numThreads];
        int numCellsPerThread = grid.numRows * grid.numRows / numThreads;
        List<AtomicReference[]> listOfSubSets = findSubSetsOfCellsForThread(numCellsPerThread);

        RuleChecker ruleChecker = new RuleChecker(grid);
        for (int i = 0; i < numThreads; i++) {
            AtomicReference[] cells = listOfSubSets.get(i);
            Calculator calculator = new Calculator(cells, ruleChecker);
            calculators[i] = new Thread(calculator);
        }
    }

    /**
     * Finds subsets of cells from grid depending on how many cells per set
     *
     * @param numThreads How many cells you want per array
     * @return List of arrays that contains an array of cells
     */
    public List<AtomicReference[]> findSubSetsOfCellsForThread(int numThreads) {
//        int numCells = grid.numRows * grid.numRows;
//        for (int i = 0; i < numCells; i += numThreads) {
//            AtomicReference[] subSet = grid.getSubSetOfGrid(i, Math.min(numCells, i + numThreads));
//            list.add(subSet);
//        }
        List<AtomicReference[]> list = new ArrayList<>();
        for (int i = 0; i < Math.pow(grid.numRows, 2); i += numThreads) {
            int a = (int) (i + Math.min(numThreads, Math.pow(grid.numRows, 2) - i));
            AtomicReference[] subSet = grid.getSubSetOfGrid(i, a);
            list.add(subSet);

        }

        if (list.size() > numThreads) {
            List<AtomicReference> l = Arrays.asList(list.get(numThreads));
            ArrayList<AtomicReference> atomicReferences = new ArrayList<>(l);
            for (int i = numThreads + 1; i < list.size(); i++) {
                for (int j = 0; j < list.get(i).length; j++) {
                    atomicReferences.add(list.get(i)[j]);
                }
                list.remove(i);
            }
        }

        return list;


    }
    /*
    for (int i = 0; i < originalList.size(); i += partitionSize) {
    partitions.add(originalList.subList(i,
            i + Math.min(partitionSize, originalList.size() - i)));
}
     */

    public int numThreads() throws RuntimeException {
        int cores = Runtime.getRuntime().availableProcessors();
        if (cores < 1) {
            throw new RuntimeException("Number of processors is less than 1");
        }
        return cores;
    }

    private class Calculator implements Runnable {
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
                int cellIndex = grid.convert2DCoordinateTo1D(nextIterationCell.x, nextIterationCell.y);
                newGridToSet.setCell(cellIndex, nextIterationCell);
            }
        }
    }

}