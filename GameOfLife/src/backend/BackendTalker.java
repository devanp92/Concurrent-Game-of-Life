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
    public void setCellState(int x, int y, int state) {
        Cell cell = grid.getCell(x, y);
        int index = grid.convert2DCoordinateTo1D(x, y);
        cell.setCellState(state);
        grid.setCell(index, cell);
    }
}
