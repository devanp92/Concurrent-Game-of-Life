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


    public void calculateNewIteration() throws Exception {
        initializeCalculators();
    }

    private void initializeCalculators() throws Exception {
        int numThreads = numThreads();
        calculators = new Calculator[numThreads];
        List<AtomicReference[]> listOfSubSets = findSubSetsOfCellsForThread(numThreads, grid.numRows * grid.numRows);

        for (int i = 0; i < numThreads; i++) {
            AtomicReference[] cells = listOfSubSets.get(i);
            calculators[i] = new Calculator(cells, new RuleChecker());
        }
    }

    private List<AtomicReference[]> findSubSetsOfCellsForThread(int numThreads, int numCells) {
        List<AtomicReference[]> list = new ArrayList<AtomicReference[]>();
        for (int i = 0; i < numCells; i += numThreads) {
            AtomicReference[] subSet = grid.getSubSetOfGrid(i, Math.min(numCells, i + numThreads));
            list.add(subSet);
        }
        return list;
    }

    private int numThreads() throws Exception {
        int cores = Runtime.getRuntime().availableProcessors();
        if (cores < 1) {
            throw new Exception("Number of processors is less than 1");
        }
        return cores;
    }

    public Calculator[] getCalculators() {
        return calculators;
    }

    public class Calculator implements Runnable {
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