package test;

import backend.Cell;
import backend.Grid;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

/**
 * Created by devan on 4/9/14.
 */
public class GridTest {

    private Grid grid;
    private final int NUM_ROWS = 4;

    @Before
    public void setUp() throws Exception {
        grid = new Grid(NUM_ROWS);
        grid.setCellState(1, 1);
        grid.setCellState(4, 1);
        grid.setCellState(2, 1);
        grid.setCellState(8, 1);
        grid.setCellState(15, 1);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetCell() throws Exception {
        Cell cell = grid.getCell(1, 0);
        assertNotNull(cell);
    }

    @Test
    public void testGetNumRows() throws Exception {
        int numRows = grid.getNumRows();
        assertEquals(4, numRows);
    }

    @Test
    public void testSetCell() throws Exception {
        Cell cell = new Cell(0,1);
        grid.setCell(cell);
        Cell newCell = grid.getCell(1);
        assertEquals(cell, newCell);
    }

    @Test
    public void testGetSubSetsOfGrid() throws Exception {
        AtomicReference[] testGridSubset = grid.getSubSetOfGrid(0,3);
        assertEquals(3, testGridSubset.length);
        Cell[] cells = new Cell[3];
        for (int i = 0; i < 3; i++) {
            cells[i] = new Cell(0,i);
        }
        for(int i = 0; i < 3; i++){
            Cell cell = (Cell) testGridSubset[i].get();
            assertEquals(cell, cells[i]);
        }
    }

    @Test
    public void testConvertGridTo2DArray() throws Exception {
        Cell[][] cell2DArray = grid.convertGridTo2DArray();
        for(int i = 0; i < Math.pow(grid.getNumRows(),2); i++){
            HashMap<Character, Integer> hashMap = grid.convert1DCoordinateTo2D(i);
            int x = hashMap.get('x');
            int y = hashMap.get('y');
            assertEquals(cell2DArray[x][y],grid.getCell(i));
        }
    }

    @Test
    public void testSetGrid() throws Exception {
        Grid testGrid = new Grid(4);
        Grid previousGrid = grid;
        grid.setGrid(testGrid.convertGridTo2DArray());
        assertNotEquals(testGrid, previousGrid);
    }
}
