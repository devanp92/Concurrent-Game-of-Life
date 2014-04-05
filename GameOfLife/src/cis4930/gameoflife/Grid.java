package cis4930.gameoflife;

import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Created by devan on 3/29/14.
 */
public class Grid {

    private static AtomicReferenceArray grid;

    private int numRows;

    public Grid(int numRows) {
        this.numRows = numRows;
        Cell[][] cells = new Cell[numRows][numRows];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numRows; j++) {
                if ((i == 0 && j == 0) || (i == 0 && j == numRows - 1) || (i == numRows - 1 && j == 0) || (i == numRows - 1 && j == numRows - 1)) {
                    cells[i][j] = new Cell(i, j, CellCase.CORNER);
                } else if((i == 0 && j > 0) || (i > 0 && j == 0) || (i == numRows - 1 && j > 0) || (i < numRows - 1 && j == numRows - 1))


            }
        }
        Grid.grid = new AtomicReferenceArray(cells);
    }

    public Grid(AtomicReferenceArray grid) {
        Grid.grid = grid;
        this.numRows = grid.length();
    }

    public void initializeCell(int row, int column) {
        int position = row * numRows + column;
        Cell cell = (Cell) grid.get(position);
        cell.setLife(1);
    }

    public static AtomicReferenceArray getGrid() {
        return grid;
    }

    public void setGrid(AtomicReferenceArray grid) {
        Grid.grid = grid;
    }

    public Cell getCell(int row, int column) {
        return (Cell) grid.get(row * numRows + column);
    }

    public int getNumRows() {
        return numRows;
    }
}
