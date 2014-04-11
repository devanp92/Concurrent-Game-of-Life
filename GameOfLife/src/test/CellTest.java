package test;

import backend.Cell;
import backend.CellCase;
import backend.Grid;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by devan on 4/11/14.
 */
public class CellTest {
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

    @Test
    public void testSetCellCaseTLC() throws Exception {
        Cell cell = new Cell(0, 0);
        cell.setCellCase(NUM_ROWS);
        CellCase topLeftCorner = CellCase.TOP_LEFT_CORNER;
        assertEquals(topLeftCorner, cell.getCellCase());
    }

    @Test
    public void testSetCellCaseTB() throws Exception {
        Cell cell = new Cell(0, 1);
        cell.setCellCase(NUM_ROWS);
        CellCase topBorder = CellCase.TOP_BORDER;
        assertEquals(topBorder, cell.getCellCase());
    }

    @Test
    public void testSetCellCaseTRC() throws Exception {
        Cell cell = new Cell(0, 3);
        cell.setCellCase(NUM_ROWS);
        CellCase topRightCorner = CellCase.TOP_RIGHT_CORNER;
        assertEquals(topRightCorner, cell.getCellCase());
    }

    @Test
    public void testSetCellCaseTRB() throws Exception {
        Cell cell = new Cell(1, 3);
        cell.setCellCase(NUM_ROWS);
        CellCase topRightCorner = CellCase.RIGHT_BORDER;
        assertEquals(topRightCorner, cell.getCellCase());
    }

    @Test
    public void testSetCellCaseBRC() throws Exception {
        Cell cell = new Cell(3, 3);
        cell.setCellCase(NUM_ROWS);
        CellCase topRightCorner = CellCase.BOTTOM_RIGHT_CORNER;
        assertEquals(topRightCorner, cell.getCellCase());
    }

    @Test
    public void testSetCellCaseBB() throws Exception {
        Cell cell = new Cell(3, 2);
        cell.setCellCase(NUM_ROWS);
        CellCase topRightCorner = CellCase.BOTTOM_BORDER;
        assertEquals(topRightCorner, cell.getCellCase());
    }

    @Test
    public void testSetCellCaseBLC() throws Exception {
        Cell cell = new Cell(3, 0);
        cell.setCellCase(NUM_ROWS);
        CellCase topRightCorner = CellCase.BOTTOM_LEFT_CORNER;
        assertEquals(topRightCorner, cell.getCellCase());
    }

    @Test
    public void testSetCellCaseLB() throws Exception {
        Cell cell = new Cell(2, 0);
        cell.setCellCase(NUM_ROWS);
        CellCase topRightCorner = CellCase.LEFT_BORDER;
        assertEquals(topRightCorner, cell.getCellCase());
    }

    @Test
    public void testSetCellCaseM() throws Exception {
        Cell cell = new Cell(2, 2);
        cell.setCellCase(NUM_ROWS);
        CellCase topRightCorner = CellCase.MIDDLE;
        assertEquals(topRightCorner, cell.getCellCase());
    }


    @Test
    public void testSetCellState() throws Exception {
        Cell cell = new Cell(2, 0);
        cell.setCellCase(NUM_ROWS);
        cell.setCellState(1);
        assertEquals(1, cell.getCellState());
    }
}
