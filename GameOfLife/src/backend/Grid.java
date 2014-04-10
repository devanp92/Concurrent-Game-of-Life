package backend;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by devan on 3/29/14.
 */

public class Grid extends CoordinateCalculator {

    private static AtomicReference[] grid;

    public Grid(int numRows) throws Exception {
        if(numRows < 2){
            throw new Exception("Grid has less than 2 rows");
        }
        this.numRows = numRows;
        Grid.grid = initializeGrid(numRows);
        super.numRows = numRows;
    }


    private AtomicReference[] initializeGrid(int numRows) {
        AtomicReference[] cells = new AtomicReference[(int) Math.pow(numRows, 2)];
        for (int i = 0; i < (int) Math.pow(numRows, 2); i++) {

            HashMap<Character, Integer> cellPositionOnGrid = super.convert1DCoordinateTo2D(i);

            int x = cellPositionOnGrid.get('x');
            int y = cellPositionOnGrid.get('y');

            Cell cell = new Cell(x, y);
            cell.setCellCase(x, y, numRows);

            cells[i] = new AtomicReference<Cell>(cell);
        }
        return cells;
    }

    public static AtomicReference[] getGrid() {
        return grid;
    }

    public Cell getCell(int row, int column) {
        AtomicReference atomicReferences = grid[super.convert2DCoordinateTo1D(row, column)];
        return (Cell) atomicReferences.get();
    }

    public int getNumRows() {
        return numRows;
    }

    public void setCell(int index, Cell cell){
        AtomicReference<Cell> cellAtomicReference = new AtomicReference<Cell>(cell);
        grid[index] = cellAtomicReference;
    }

    public AtomicReference[] getSubSetOfGrid(int start, int end){
        return Arrays.copyOfRange(grid, start, end);
    }

    /**
     * Get grid in 2D of cells
     * @return Cell[][]
     */
    public Cell[][] convertGridTo2DArray(){
        Cell[][] cells = new Cell[(int) Math.sqrt(numRows)][];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numRows; j++) {
                AtomicReference atomic = grid[convert2DCoordinateTo1D(i,j)];
                cells[i][j] = (Cell) atomic.get();
            }
        }
        return cells;
    }

    /**
     * setting new grid
     * @param cells grid to atomic reference
     */
    public void setGrid(Cell[][] cells){
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                setCell(convert2DCoordinateTo1D(i, j), cells[i][j]);
            }
        }
    }
}
