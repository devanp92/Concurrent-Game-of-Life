package cis4930.gameoflife;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Created by devan on 3/29/14.
 */
public class Grid {

    private static AtomicReferenceArray<Cell> grid;

    private int numRows;

    public Grid(int numRows) {
        this.numRows = numRows;
        Cell[] cells = initializeGrid(numRows);
        Grid.grid = new AtomicReferenceArray<Cell>(cells);
    }

    public Grid(AtomicReferenceArray<Cell> grid) {
        Grid.grid = grid;
        this.numRows = grid.length();
    }

    public void setValueOfCell(int index, int deadOrAlive) {
        Cell cell = grid.get(index);
        cell.setLife(deadOrAlive);
    }

    public Cell[] initializeGrid(int numRows) {
        Cell[] cells = new Cell[(int) Math.pow(numRows, 2)];
        for (int i = 0; i < (int) Math.pow(numRows, 2); i++) {

            HashMap<Character, Integer> cellPosition = getPositionOf1DArrayFromXAndY(i);

            int x = cellPosition.get('X');
            int y = cellPosition.get('Y');

            CellCase cellCase = checkCase(x, y);

            cells[i] = new Cell(x, y, cellCase);
        }
        return cells;
    }

    public HashMap<Character, Integer> getPositionOf1DArrayFromXAndY(int i) {
        HashMap<Character, Integer> hashMap = new HashMap<Character, Integer>();
        int row, column;

        if (i == 0) {
            row = 0;
            column = 0;
        } else if (i / 10 == 0) {
            row = 0;
            column = i - 1;
        } else {
            row = i / numRows;
            column = i % numRows;
        }

        hashMap.put('x', row);
        hashMap.put('y', column);

        return hashMap;
    }

    public CellCase checkCase(int i, int j) {
        if ((i == 0 && j == 0) || (i == 0 && j == numRows - 1) || (i == numRows - 1 && j == 0) || (i == numRows - 1 && j == numRows - 1)) {
            return CellCase.CORNER;

        } else if ((j == 0 && i > 0 && i < numRows - 1) || (j == numRows - 1 && i > 0 && i < numRows - 1) || (i == 0 && j > 0 && j < numRows - 1) || ((i == numRows - 1 && j > 0 && j < numRows - 1))) {
            return CellCase.BORDER;

        } else {
            return CellCase.MIDDLE;

        }
    }

    public static AtomicReferenceArray<Cell> getGrid() {
        return grid;
    }

    public void setGrid(AtomicReferenceArray<Cell> grid) {
        Grid.grid = grid;
    }

    public Cell getCell(int row, int column) {
        return (Cell) grid.get(row * numRows + column);
    }

    public int getNumRows() {
        return numRows;
    }
}
