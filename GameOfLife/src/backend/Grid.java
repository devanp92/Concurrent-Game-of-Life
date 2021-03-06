package backend;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by devan on 3/29/14.
 */

public class Grid extends CoordinateCalculator implements Serializable {
	private static final long serialVersionUID = 7022186285671636304L;
	
	private AtomicReference[] grid;


    public Grid(int numRows) throws Exception {
        if (numRows < 2 || numRows >= 80) {
            throw new IllegalArgumentException("numRows is out of bounds");
        }
        this.numRows = numRows;
        /*Grid.*/grid = initializeGrid(numRows);
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

    public Cell getCell(int x, int y) {
        if (x < 0 || x >= numRows || y < 0 || y >= numRows) {
            throw new IllegalArgumentException("Row or column is out of bounds");
        }
        AtomicReference atomicReferences = grid[super.convert2DCoordinateTo1D(x, y)];
        return (Cell) atomicReferences.get();
    }

    public Cell getCell(int index1D) {
        if (index1D < 0 || index1D >= Math.pow(numRows, 2)) {
            throw new IllegalArgumentException("Index is either below 0 or higher than number of cells");
        }
        AtomicReference atomicReferences = grid[index1D];
        return (Cell) atomicReferences.get();
    }

    public int getNumRows() {
        return numRows;
    }

    public void setCell(Cell cell){

        if (cell != null) {
            grid[convert2DCoordinateTo1D(cell.x, cell.y)] = new AtomicReference<>(cell);
            try {
				cell.setCellCase(getNumRows());
			}
			catch(Exception e) {
				e.printStackTrace();
			}
        }
        
    }

    public void setCellState(int index, int cellState) throws Exception {
        if (cellState < 0 || cellState > 1) {
            throw new Exception("Incorrect cell state. Must be either 0 or 1");
        }
        if (index < 0 || index >= Math.pow(numRows, 2)) {
            throw new IllegalArgumentException("Index is either below 0 or higher than number of cells");
        }
        Cell cell = getCell(index);
        cell.setCellState(cellState);
    }

    public void setGrid(Cell[][] cells) {
        if(cells == null){
            throw new NullPointerException("Grid is null");
        }
        for (Cell[] cell : cells) {
            for (int j = 0; j < cells[0].length; j++) {
                setCell(cell[j]);
            }
        }
    }

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

    public AtomicReference[] getSubSetOfGrid(int start, int end) {
        return Arrays.copyOfRange(grid, start, end);
    }

    public /*static*/ AtomicReference[] getGrid() {
        return grid;
    }
    
    public void printGrid() {
    	for(int i = 0;i<getNumRows();i++) {
    		for(int j = 0;j<getNumRows();j++) {
    			System.out.print(getCell(j, i).getCellState());
    		}
    		System.out.print("\n");
    	}
    }
    
}
