package backend;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by devan on 3/29/14.
 */

public class Grid extends CoordinateCalculator implements Serializable{

    private static AtomicReference[] grid;
    private static final long serialVersionUID = 1L;


    public Grid(int numRows) throws Exception {
        if (numRows < 2) {
            throw new Exception("Grid has less than 2 rows");
        }
        this.numRows = numRows;
        Grid.grid = initializeGrid(numRows);
        super.numRows = numRows;
    }


    private AtomicReference[] initializeGrid(int numRows) throws Exception {
        AtomicReference[] cells = new AtomicReference[(int) Math.pow(numRows, 2)];
        for (int i = 0; i < (int) Math.pow(numRows, 2); i++) {

            HashMap<Character, Integer> cellPositionOnGrid = super.convert1DCoordinateTo2D(i);

            int x = cellPositionOnGrid.get('x');
            int y = cellPositionOnGrid.get('y');

            Cell cell = new Cell(x, y);
            cell.setCellCase(numRows);
            cells[i] = new AtomicReference<>(cell);
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

    public Cell getCell(int index1D) {
        AtomicReference atomicReferences = grid[index1D];
        return (Cell) atomicReferences.get();
    }

    public int getNumRows() {
        return numRows;
    }

    public void setCell(int index, Cell cell) {
        AtomicReference<Cell> cellAtomicReference = new AtomicReference<>(cell);
        grid[index] = cellAtomicReference;
    }

    public void setCellState(int index, int cellState) throws Exception {
        if (cellState < 0 || cellState > 1) {
            throw new Exception("Incorrect cell state. Must be either 0 or 1");
        }
        Cell cell = getCell(index);
        cell.setCellState(cellState);
    }

    public AtomicReference[] getSubSetOfGrid(int start, int end) {
        return Arrays.copyOfRange(grid, start, end);
    }

    /**
     * Get grid in 2D of cells
     *
     * @return Cell[][]
     */
    public Cell[][] convertGridTo2DArray() {
        Cell[][] cells = new Cell[numRows][numRows];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numRows; j++) {
                AtomicReference atomic = grid[convert2DCoordinateTo1D(i, j)];
                Cell cell = (Cell) atomic.get();
                cells[i][j] = cell;
            }
        }
        return cells;
    }

    /**
     * setting new grid
     *
     * @param cells grid to atomic reference
     */
    public void setGrid(Cell[][] cells) {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                setCell(convert2DCoordinateTo1D(i, j), cells[i][j]);
            }
        }
    }
}
