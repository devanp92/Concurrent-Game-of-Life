package test;

import backend.Cell;
import backend.Grid;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

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

//    @Test
//    public void testGetGrid() throws Exception {
//
//    }

    @Test
    public void testGetCell() throws Exception {
        Cell cell = grid.getCell(1, 0);
        assertNotNull(cell);
    }

//    @Test
//    public void testGetNumRows() throws Exception {
//
//    }
//
//    @Test
//    public void testSetCell() throws Exception {
//
//    }
//
//    @Test
//    public void testGetSubSetOfGrid() throws Exception {
//
//    }
//
//    @Test
//    public void testConvertGridTo2DArray() throws Exception {
//
//    }
//
//    @Test
//    public void testSetGrid() throws Exception {
//
//    }
}
