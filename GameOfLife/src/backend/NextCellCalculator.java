package backend;

import java.util.concurrent.atomic.AtomicReference;

public class NextCellCalculator extends Thread {
	private Grid originalGrid;
    private AtomicReference[] originalCells;
    private Cell[] nextCells;
    private RuleChecker ruleChecker;

    public NextCellCalculator(AtomicReference[] cells, Grid g) {
        this.originalCells = cells;
        this.originalGrid = g;
        this.ruleChecker = new RuleChecker(g);
        this.nextCells = new Cell[cells.length];
    }

    @Override
    public void run() {
    	int i = 0;
        for (AtomicReference atomicReference : originalCells) {
            Cell cell = (Cell) atomicReference.get();
            Cell nextIterationCell = null;
            try {
                nextIterationCell = ruleChecker.determineCellsNextState(cell);
            } catch (Exception e) {
                e.printStackTrace();
            }
            assert nextIterationCell != null;
            nextCells[i] = nextIterationCell;
            i++;
        }
    }
    
    /**Only call after joining on the Thread*/
    public Cell[] getNextCells() {
    	return nextCells;
    }
}
