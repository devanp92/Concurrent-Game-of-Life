package test;

import backend.Cell;
import backend.CellCase;
import backend.Grid;
import backend.NeighborFinder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

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

//    @Test
//    public void testFindNeighborForTopLeftCornerCell() throws Exception {
//        cell = new Cell(0, 0);
//        neighborFinder = new NeighborFinder(cell);
//        List<Cell> list = neighborFinder.findNeighborsForTopLeftCornerCell();
//        Cell cell0 = list.get(0);
//        Cell cell1 = list.get(1);
//        Cell cell2 = list.get(2);
//        assertEquals(CellCase.TOP_BORDER, cell0.getCellCase());
//        assertEquals(0, cell0.x);
//        assertEquals(1, cell0.y);
//        assertEquals(CellCase.LEFT_BORDER, cell1.getCellCase());
//        assertEquals(1, cell1.x);
//        assertEquals(0, cell1.y);
//        assertEquals( , cell2.getCellCase());
//        assertEquals( , cell2.x);
//        assertEquals( , cell2.y);
//    }
//
//    @Test
//    public void testFindNeighborForTopRightCornerCell() throws Exception {
//        cell = new Cell(0,3);
//        neighborFinder = new NeighborFinder(cell);
//        List<Cell> list = neighborFinder.findNeighborsForTopRightCornerCell();
//        Cell cell0 = list.get(0);
//        Cell cell1 = list.get(1);
//        Cell cell2 = list.get(2);
//        assertEquals( , cell0.getCellCase());
//        assertEquals( , cell0.x);
//        assertEquals( , cell0.y);
//        assertEquals( , cell1.getCellCase());
//        assertEquals( , cell1.x);
//        assertEquals( , cell1.y);
//        assertEquals( , cell2.getCellCase());
//        assertEquals( , cell2.x);
//        assertEquals( , cell2.y);
//    }
//
//    @Test
//    public void testFindNeighborForBottomLeftCornerCell() throws Exception {
//        cell = new Cell(3,0);
//        neighborFinder = new NeighborFinder(cell);
//        List<Cell> list = neighborFinder.findNeighborsForBottomLeftCornerCell();
//        Cell cell0 = list.get(0);
//        Cell cell1 = list.get(1);
//        Cell cell2 = list.get(2);
//        assertEquals( , cell0.getCellCase());
//        assertEquals( , cell0.x);
//        assertEquals( , cell0.y);
//        assertEquals( , cell1.getCellCase());
//        assertEquals( , cell1.x);
//        assertEquals( , cell1.y);
//        assertEquals( , cell2.getCellCase());
//        assertEquals( , cell2.x);
//        assertEquals( , cell2.y);
//    }
//
//    @Test
//    public void testFindNeighborForBottomRightCornerCell() throws Exception {
//        cell = new Cell(3,3);
//        neighborFinder = new NeighborFinder(cell);
//        List<Cell> list = neighborFinder.findNeighborsForBottomRightCornerCell();
//        Cell cell0 = list.get(0);
//        Cell cell1 = list.get(1);
//        Cell cell2 = list.get(2);
//        assertEquals( , cell0.x);
//        assertEquals( , cell0.y);
//        assertEquals( , cell0.getCellCase());
//        assertEquals( , cell1.x);
//        assertEquals( , cell1.y);
//        assertEquals( , cell1.getCellCase());
//        assertEquals( , cell2.x);
//        assertEquals( , cell2.y);
//        assertEquals( , cell2.y);
//    }
//
//    @Test
//    public void testFindNeighborForTopBorderCell() throws Exception {
//        cell = new Cell(0,0);
//        neighborFinder = new NeighborFinder(cell);
//
//    }
//
//    @Test
//    public void testFindNeighborForRightBorderCell() throws Exception {
//        cell = new Cell(0,0);
//        neighborFinder = new NeighborFinder(cell);
//
//    }
//
//    @Test
//    public void testFindNeighborForLeftBorderCell() throws Exception {
//        cell = new Cell(0,0);
//        neighborFinder = new NeighborFinder(cell);
//
//    }
//
//    @Test
//    public void testFindNeighborForBottomBorderCell() throws Exception {
//        cell = new Cell(0,0);
//        neighborFinder = new NeighborFinder(cell);
//
//    }
//
//    @Test
//    public void testFindNeighborForMiddleCells() throws Exception {
//        cell = new Cell(0,0);
//        neighborFinder = new NeighborFinder(cell);
//
//    }
//
    @Test
    public void testInitializeCellsNeighborPositions() throws Exception {


    }
}
