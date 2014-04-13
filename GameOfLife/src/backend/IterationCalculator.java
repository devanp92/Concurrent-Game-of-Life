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

    private Thread[] calculators;

    public IterationCalculator(Grid grid) throws Exception {
        if (grid == null) {
            throw new NullPointerException("The grid is null");
        }
        this.grid = grid;
        this.newGridToSet = new Grid(grid.getNumRows());
    }


    public void calculateNewIteration() throws Exception {
        initializeCalculators();
        startThreads();
        grid.setGrid(joinThreads());
    }

    public Cell[][] joinThreads() throws InterruptedException {
        for(Thread calculator: calculators){
            calculator.join();
        }
        return grid.convertGridTo2DArray();
    }

    private void startThreads(){
        for(Thread calculator : calculators){
             calculator.start();
        }
    }

    private void initializeCalculators() throws RuntimeException, InterruptedException {
        int numThreads = numThreads();
        calculators = new Thread[numThreads];
        List<AtomicReference[]> listOfSubSets = findSubSetsOfCellsForThread(numThreads, grid.numRows * grid.numRows);

        RuleChecker ruleChecker = new RuleChecker();
        for (int i = 0; i < numThreads; i++) {
            AtomicReference[] cells = listOfSubSets.get(i);
            Calculator calculator = new Calculator(cells, ruleChecker);
            calculators[i] = new Thread(calculator);
        }
    }

    private List<AtomicReference[]> findSubSetsOfCellsForThread(int numThreads, int numCells) {
        List<AtomicReference[]> list = new ArrayList<>();
        for (int i = 0; i < numCells; i += numThreads) {
            AtomicReference[] subSet = grid.getSubSetOfGrid(i, Math.min(numCells, i + numThreads));
            list.add(subSet);
        }
        return list;
    }

    private int numThreads() throws RuntimeException {
        int cores = Runtime.getRuntime().availableProcessors();
        if (cores < 1) {
            throw new RuntimeException("Number of processors is less than 1");
        }
        return cores;
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