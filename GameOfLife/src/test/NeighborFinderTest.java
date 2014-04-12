package test;

import backend.Cell;
import backend.CellCase;
import backend.Grid;
import backend.NeighborFinder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by devan on 4/11/14.
 */
public class NeighborFinderTest {
    private Grid grid;
    private final int NUM_ROWS = 4;
    private Cell cell;
    private NeighborFinder neighborFinder;

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
    public void testFindNeighbors() throws Exception {

    }

    @Test
    public void testFindNeighborForTopLeftCornerCell() throws Exception {
        cell = new Cell(0, 0);
        neighborFinder = new NeighborFinder(cell, NUM_ROWS);
        List<Cell> list = neighborFinder.findNeighborsForTopLeftCornerCell();
        Cell cell0 = list.get(0);
        Cell cell1 = list.get(1);
        Cell cell2 = list.get(2);

        assertNotNull(list);
        assertEquals(3, list.size());
        assertEquals(CellCase.TOP_BORDER, cell0.getCellCase());
        assertEquals(0, cell0.x);
        assertEquals(1, cell0.y);
        assertEquals(CellCase.MIDDLE, cell1.getCellCase());
        assertEquals(1, cell1.x);
        assertEquals(1, cell1.y);
        assertEquals(CellCase.LEFT_BORDER, cell2.getCellCase());
        assertEquals(1, cell2.x);
        assertEquals(0, cell2.y);
    }

    @Test
    public void testFindNeighborForTopRightCornerCell() throws Exception {
        cell = new Cell(0, 3);
        neighborFinder = new NeighborFinder(cell, NUM_ROWS);
        List<Cell> list = neighborFinder.findNeighborsForTopRightCornerCell();

        Cell cell0 = new Cell(0, 2);
        cell0.setCellCase(NUM_ROWS);
        Cell cell1 = new Cell(1, 3);
        cell1.setCellCase(NUM_ROWS);
        Cell cell2 = new Cell(1, 2);
        cell2.setCellCase(NUM_ROWS);

        assertNotNull(list);
        assertEquals(3, list.size());
        assertTrue(list.contains(cell0));
        assertTrue(list.contains(cell1));
        assertTrue(list.contains(cell2));


    }

    @Test
    public void testFindNeighborForBottomLeftCornerCell() throws Exception {
        cell = new Cell(3, 0);
        neighborFinder = new NeighborFinder(cell, NUM_ROWS);
        List<Cell> list = neighborFinder.findNeighborsForBottomLeftCornerCell();
        Cell cell0 = new Cell(2, 0);
        cell0.setCellCase(NUM_ROWS);
        Cell cell1 = new Cell(3, 1);
        cell1.setCellCase(NUM_ROWS);
        Cell cell2 = new Cell(2, 1);
        cell2.setCellCase(NUM_ROWS);

        assertNotNull(list);
        assertEquals(3, list.size());
        assertTrue(list.contains(cell0));
        assertTrue(list.contains(cell1));
        assertTrue(list.contains(cell2));


    }

    @Test
    public void testFindNeighborForBottomRightCornerCell() throws Exception {
        cell = new Cell(3, 3);
        neighborFinder = new NeighborFinder(cell, NUM_ROWS);
        List<Cell> list = neighborFinder.findNeighborsForBottomRightCornerCell();

        Cell cell0 = new Cell(2, 3);
        cell0.setCellCase(NUM_ROWS);
        Cell cell1 = new Cell(3, 2);
        cell1.setCellCase(NUM_ROWS);
        Cell cell2 = new Cell(2, 2);
        cell2.setCellCase(NUM_ROWS);

        assertNotNull(list);
        assertEquals(3, list.size());
        assertTrue(list.contains(cell0));
        assertTrue(list.contains(cell1));
        assertTrue(list.contains(cell2));
    }


    @Test
    public void testFindNeighborForTopBorderCell() throws Exception {
        cell = new Cell(0, 1);
        neighborFinder = new NeighborFinder(cell, NUM_ROWS);
        List<Cell> list = neighborFinder.findNeighborsForTopBorderCell();

        Cell cell0 = new Cell(0, 0);
        cell0.setCellCase(NUM_ROWS);
        Cell cell1 = new Cell(0, 2);
        cell1.setCellCase(NUM_ROWS);
        Cell cell2 = new Cell(1, 0);
        cell2.setCellCase(NUM_ROWS);
        Cell cell3 = new Cell(1, 2);
        cell2.setCellCase(NUM_ROWS);
        Cell cell4 = new Cell(1, 1);
        cell2.setCellCase(NUM_ROWS);

        assertNotNull(list);
        assertEquals(5, list.size());
        assertTrue(list.contains(cell0));
        assertTrue(list.contains(cell1));
        assertTrue(list.contains(cell2));
        assertTrue(list.contains(cell3));
        assertTrue(list.contains(cell4));


    }


    @Test
    public void testFindNeighborForRightBorderCell() throws Exception {
        cell = new Cell(2, 3);
        neighborFinder = new NeighborFinder(cell, NUM_ROWS);
        List<Cell> list = neighborFinder.findNeighborsForRightBorderCell();

        Cell cell0 = new Cell(2, 2);
        cell0.setCellCase(NUM_ROWS);
        Cell cell1 = new Cell(3, 2);
        cell1.setCellCase(NUM_ROWS);
        Cell cell2 = new Cell(3, 3);
        cell2.setCellCase(NUM_ROWS);
        Cell cell3 = new Cell(1, 2);
        cell2.setCellCase(NUM_ROWS);
        Cell cell4 = new Cell(1, 3);
        cell2.setCellCase(NUM_ROWS);

        assertNotNull(list);
        assertEquals(5, list.size());
        assertTrue(list.contains(cell0));
        assertTrue(list.contains(cell1));
        assertTrue(list.contains(cell2));
        assertTrue(list.contains(cell3));
        assertTrue(list.contains(cell4));
    }

    @Test
    public void testFindNeighborForLeftBorderCell() throws Exception {
        cell = new Cell(2, 0);
        neighborFinder = new NeighborFinder(cell, NUM_ROWS);
        List<Cell> list = neighborFinder.findNeighborsForLeftBorderCell();

        Cell cell0 = new Cell(1, 0);
        cell0.setCellCase(NUM_ROWS);
        Cell cell1 = new Cell(3, 0);
        cell1.setCellCase(NUM_ROWS);
        Cell cell2 = new Cell(1, 1);
        cell2.setCellCase(NUM_ROWS);
        Cell cell3 = new Cell(2, 1);
        cell2.setCellCase(NUM_ROWS);
        Cell cell4 = new Cell(3, 1);
        cell2.setCellCase(NUM_ROWS);

        assertNotNull(list);
        assertEquals(5, list.size());
        assertTrue(list.contains(cell0));
        assertTrue(list.contains(cell1));
        assertTrue(list.contains(cell2));
        assertTrue(list.contains(cell3));
        assertTrue(list.contains(cell4));
    }

    @Test
    public void testFindNeighborForBottomBorderCell() throws Exception {
        cell = new Cell(3, 1);
        neighborFinder = new NeighborFinder(cell, NUM_ROWS);
        List<Cell> list = neighborFinder.findNeighborsForBottomBorderCell();

        Cell cell0 = new Cell(3, 0);
        cell0.setCellCase(NUM_ROWS);
        Cell cell1 = new Cell(3, 2);
        cell1.setCellCase(NUM_ROWS);
        Cell cell2 = new Cell(2, 0);
        cell2.setCellCase(NUM_ROWS);
        Cell cell3 = new Cell(2, 2);
        cell2.setCellCase(NUM_ROWS);
        Cell cell4 = new Cell(2, 1);
        cell2.setCellCase(NUM_ROWS);

        assertNotNull(list);
        assertEquals(5, list.size());
        assertTrue(list.contains(cell0));
        assertTrue(list.contains(cell1));
        assertTrue(list.contains(cell2));
        assertTrue(list.contains(cell3));
        assertTrue(list.contains(cell4));
    }

    @Test
    public void testFindNeighborForMiddleCells() throws Exception {
        cell = new Cell(2, 1);
        neighborFinder = new NeighborFinder(cell, NUM_ROWS);
        List<Cell> list = neighborFinder.findNeighborsForMiddleCells();

        Cell cell0 = new Cell(1, 0);
        cell0.setCellCase(NUM_ROWS);
        Cell cell1 = new Cell(1, 1);
        cell1.setCellCase(NUM_ROWS);
        Cell cell2 = new Cell(1, 2);
        cell2.setCellCase(NUM_ROWS);
        Cell cell3 = new Cell(2, 0);
        cell2.setCellCase(NUM_ROWS);
        Cell cell4 = new Cell(2, 2);
        cell2.setCellCase(NUM_ROWS);
        Cell cell5 = new Cell(3, 0);
        cell2.setCellCase(NUM_ROWS);
        Cell cell6 = new Cell(3, 1);
        cell2.setCellCase(NUM_ROWS);
        Cell cell7 = new Cell(3, 2);
        cell2.setCellCase(NUM_ROWS);

        assertNotNull(list);
        assertEquals(8, list.size());
        assertTrue(list.contains(cell0));
        assertTrue(list.contains(cell1));
        assertTrue(list.contains(cell2));
        assertTrue(list.contains(cell3));
        assertTrue(list.contains(cell4));
        assertTrue(list.contains(cell5));
        assertTrue(list.contains(cell6));
        assertTrue(list.contains(cell7));
    }
}
