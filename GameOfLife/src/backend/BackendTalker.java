package backend;

/**
 * Created by devan on 4/9/14.
 */
public class BackendTalker {

    private Grid grid;

    public BackendTalker() {
    }

    /**
     * When user tells how many rows for grid, init it with this
     *
     * @param numRows numRows
     * @throws Exception
     */
    public void initializeGridWithNumRows(int numRows) throws Exception {
        grid = new Grid(numRows);
    }

    /**
     * returning grid
     *
     * @return grid
     */
    public Cell[][] convertGridTo2DArray() {
        return grid.convertGridTo2DArray();
    }

    /**
     * setting the new grid
     *
     * @param cells new grid of cells from client
     */
    public void setGrid(Cell[][] cells) {
        grid.setGrid(cells);
    }

    /**
     * Setting a cells state at its position
     *
     * @param x coordinate
     * @param y coordinate
     */
    public void setCellState(int x, int y, int state) throws Exception {
        Cell cell = grid.getCell(x, y);
        cell.setCellState(state);
    }

    /**
     * Starts threads and calculates the new iteration. Sets the new grid to grid
     * @param grid old grid to calculate the iteration on
     * @throws Exception
     */
    public void startIteration(Grid grid) throws Exception {
        IterationCalculator iterationCalculator = new IterationCalculator(grid);
        iterationCalculator.calculateNewIteration();
        Cell[][] cells = iterationCalculator.joinThreads();
        grid.setGrid(cells);
    }
}