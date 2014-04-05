package cis4930.gameoflife;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by devan on 3/29/14.
 */

public class Grid {

    private static AtomicReference[] grid;

    private int numRows;

    public Grid(int numRows) {
        this.numRows = numRows;
        Grid.grid = initializeGrid(numRows);
    }


    public AtomicReference[] initializeGrid(int numRows) {
        AtomicReference[] cells = new AtomicReference[(int) Math.pow(numRows, 2)];
        for (int i = 0; i < (int) Math.pow(numRows, 2); i++) {

            HashMap<Character, Integer> cellPositionOnGrid = getPositionOfGridFromXAndY(i);

            int x = cellPositionOnGrid.get('X');
            int y = cellPositionOnGrid.get('Y');

            Cell cell = new Cell(x, y);
            cell.setCellCase(x, y, numRows);

            cells[i] = new AtomicReference<Cell>(cell);
        }
        return cells;
    }

    public HashMap<Character, Integer> getPositionOfGridFromXAndY(int i) {
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


    public static AtomicReference[] getGrid() {
        return grid;
    }

    public void setGrid(AtomicReference[] grid) {
        Grid.grid = grid;
    }

    public Cell getCell(int row, int column) {
        AtomicReference atomicReferences = grid[row * numRows + column];
        return (Cell) atomicReferences.get();
    }

    public int getNumRows() {
        return numRows;
    }
}
