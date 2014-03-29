package cis4930.gameoflife;

/**
 * Created by devan on 3/29/14.
 */
public class Grid {
    private Cell[][] grid;
    private int numRows;

    public Grid(int numRows) {
        this.numRows = numRows;
        grid = new Cell[numRows][numRows];
    }
    public Grid(Cell[][] grid) {
        this.grid = grid;
        this.numRows = grid.length;
    }

    public void initializeCell(int row, int column){
        grid[row][column].isAlive = 1;
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public void setGrid(Cell[][] grid) {
        this.grid = grid;
    }
}
