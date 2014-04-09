package backend;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by devan on 3/29/14.
 */

public class Grid extends CoordinateCalculator {

    private static AtomicReference[] grid;

    public Grid(int numRows) {
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

    public void setGrid(AtomicReference[] grid) {
        Grid.grid = grid;
    }

    public Cell getCell(int row, int column) {
        AtomicReference atomicReferences = grid[super.convert2DCoordinateTo1D(row, column)];
        return (Cell) atomicReferences.get();
    }

    public int getNumRows() {
        return numRows;
    }
}
