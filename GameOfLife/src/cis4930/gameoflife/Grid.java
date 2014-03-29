package cis4930.gameoflife;

/**
 * Created by devan on 3/29/14.
 */
public class Grid {

    private static Cell[][] grid;
    private int numRows;

    public Grid(int numRows) {
        this.numRows = numRows;
        grid = new Cell[numRows][numRows];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numRows; j++) {
                grid[i][j] = new Cell(i,j);
            }
        }
    }
    public Grid(Cell[][] grid) {
        Grid.grid = grid;
        this.numRows = grid.length;
    }

    public void initializeCell(int row, int column){
        grid[row][column].isAlive = 1;
    }

    public static Cell[][] getGrid() {
        return grid;
    }

    public void setGrid(Cell[][] grid) {
        Grid.grid = grid;
    }
}
