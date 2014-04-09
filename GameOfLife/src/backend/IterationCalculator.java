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
    private List<Calculator> calculators;


    public IterationCalculator(Grid grid) {
        if (grid == null) {
            throw new NullPointerException("The grid is null");
        }
        this.grid = grid;
        this.newGridToSet = new Grid(grid.getNumRows());
    }

    public void calculateNewIteration() throws Exception {
        int numThreads = numThreads();




    }
    //TODO
//    private List<Calculator> initializeCalculators(int numThreads){
//        calculators = new ArrayList<Calculator>();
//        for (int i = 0; i < numThreads; i++) {
//            calculators.get(i) = new Calculator()
//        }
//    }
//
//    private AtomicReference<Cell>[] findSubSetOfCellsForThread(int numThreads, int threadNumber){
//
//    }

    private int numThreads() throws Exception {
        int cores = Runtime.getRuntime().availableProcessors();
        if (cores < 1) {
            throw new Exception("Number of processors is less than 1");
        }
        return cores;
    }

    private void setNewGrid(AtomicReference[] cells) {
        Grid.setGrid(cells);
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
            for(AtomicReference atomicReference : cells){
                Cell cell = (Cell) atomicReference.get();
                Cell nextIterationCell = ruleChecker.determineCellsNextState(cell);
                int cellIndex = grid.convert2DCoordinateTo1D(nextIterationCell.x, nextIterationCell.y);
                newGridToSet.setCell(cellIndex, nextIterationCell);
            }
        }
    }

}