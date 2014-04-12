package test;

import backend.Cell;
import backend.Grid;
import backend.RuleChecker;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by devan on 4/12/14.
 */
public class RuleCheckerTest {

    private Grid grid;
    private final int NUM_ROWS = 4;
    private RuleChecker ruleChecker;

    @Before
    public void setUp() throws Exception {
        grid = new Grid(NUM_ROWS);
        grid.setCellState(1, 1);
        grid.setCellState(4, 1);
        grid.setCellState(2, 1);
        grid.setCellState(8, 1);
        grid.setCellState(15, 1);
    }

//    @Test
//    public void testDetermineCellsNextState() throws Exception {
//
//    }
//
//    @Test
//    public void testNumOfAliveCellsAroundCurrentCell() throws Exception {
//        Cell cell0 = new Cell(0, 0);
//        Cell cell1 = new Cell(0, 0);
//        Cell cell2 = new Cell(0, 0);
//        Cell cell3 = new Cell(0, 0);
//
//        cell0.setCellState(0);
//        cell1.setCellState(1);
//        cell2.setCellState(1);
//        cell3.setCellState(1);
//
//        List<Cell> cells = new ArrayList<>();
//
//        cells.add(cell0);
//        cells.add(cell1);
//        cells.add(cell2);
//        cells.add(cell3);
//
//        int numAliveCells = new RuleChecker().numOfAliveCellsAroundCurrentCell(cells);
//        assertEquals(3, numAliveCells);
//    }
//
//    @Test
//    public void testSetCurrCellsNextStateDependingOnNumAliveNeighborsDead2() throws Exception {
//        Cell cell = new Cell(0, 0);
//        cell.setCellState(0);
//        ruleChecker = new RuleChecker();
//        ruleChecker.currCell = cell;
//        Cell test = ruleChecker.setCurrCellsNextStateDependingOnNumAliveNeighbors(2);
//        assertEquals(0, test.getCellState());
//    }
//
//    @Test
//    public void testSetCurrCellsNextStateDependingOnNumAliveNeighborsDead3() throws Exception {
//        Cell cell = new Cell(0, 0);
//        cell.setCellState(0);
//        ruleChecker = new RuleChecker();
//        ruleChecker.currCell = cell;
//        Cell test = ruleChecker.setCurrCellsNextStateDependingOnNumAliveNeighbors(3);
//        assertEquals(1, test.getCellState());
//    }
//    @Test
//    public void testSetCurrCellsNextStateDependingOnNumAliveNeighborsAlive2() throws Exception {
//        Cell cell = new Cell(0, 0);
//        cell.setCellState(1);
//        ruleChecker = new RuleChecker();
//        ruleChecker.currCell = cell;
//        Cell test = ruleChecker.setCurrCellsNextStateDependingOnNumAliveNeighbors(2);
//        assertEquals(1, test.getCellState());
//    }
//
//    @Test
//    public void testSetCurrCellsNextStateDependingOnNumAliveNeighborsAlive3() throws Exception {
//        Cell cell = new Cell(0, 0);
//        cell.setCellState(1);
//        ruleChecker = new RuleChecker();
//        ruleChecker.currCell = cell;
//        Cell test = ruleChecker.setCurrCellsNextStateDependingOnNumAliveNeighbors(3);
//        assertEquals(1, test.getCellState());
//    }
//    @Test
//    public void testSetCurrCellsNextStateDependingOnNumAliveNeighborsAlive4() throws Exception {
//        Cell cell = new Cell(0, 0);
//        cell.setCellState(1);
//        ruleChecker = new RuleChecker();
//        ruleChecker.currCell = cell;
//        Cell test = ruleChecker.setCurrCellsNextStateDependingOnNumAliveNeighbors(4);
//        assertEquals(0, test.getCellState());
//    }
//
//
//    @Test
//    public void testSetDeadCellsNewStateWith0() throws Exception {
//        ruleChecker = new RuleChecker();
//        ruleChecker.currCell = new Cell(0, 0);
//        ruleChecker.currCell.setCellState(0);
//        ruleChecker.setDeadCellsNewState(0);
//        int state = ruleChecker.currCell.getCellState();
//        assertEquals(0, state);
//    }
//
//    @Test
//    public void testSetDeadCellsNewStateWith1() throws Exception {
//        ruleChecker = new RuleChecker();
//        ruleChecker.currCell = new Cell(0, 0);
//        ruleChecker.currCell.setCellState(0);
//        ruleChecker.setDeadCellsNewState(1);
//        int state = ruleChecker.currCell.getCellState();
//        assertEquals(0, state);
//    }
//
//    @Test
//    public void testSetDeadCellsNewStateWith2() throws Exception {
//        ruleChecker = new RuleChecker();
//        ruleChecker.currCell = new Cell(0, 0);
//        ruleChecker.currCell.setCellState(0);
//        ruleChecker.setDeadCellsNewState(2);
//        int state = ruleChecker.currCell.getCellState();
//        assertEquals(0, state);
//    }
//
//    @Test
//    public void testSetDeadCellsNewStateWith3() throws Exception {
//        ruleChecker = new RuleChecker();
//        ruleChecker.currCell = new Cell(0, 0);
//        ruleChecker.currCell.setCellState(0);
//        ruleChecker.setDeadCellsNewState(3);
//        int state = ruleChecker.currCell.getCellState();
//        assertEquals(1, state);
//    }
//
//    @Test
//    public void testSetDeadCellsNewStateWith4() throws Exception {
//        ruleChecker = new RuleChecker();
//        ruleChecker.currCell = new Cell(0, 0);
//        ruleChecker.currCell.setCellState(0);
//        ruleChecker.setDeadCellsNewState(4);
//        int state = ruleChecker.currCell.getCellState();
//        assertEquals(0, state);
//    }
//
//    @Test
//    public void testSetAliveCellsNewStateWith0() throws Exception {
//        ruleChecker = new RuleChecker();
//        ruleChecker.currCell = new Cell(0, 0);
//        ruleChecker.currCell.setCellState(1);
//        ruleChecker.setAliveCellsNewState(0);
//        int state = ruleChecker.currCell.getCellState();
//        assertEquals(0, state);
//    }
//
//    @Test
//    public void testSetAliveCellsNewStateWith1() throws Exception {
//        ruleChecker = new RuleChecker();
//        ruleChecker.currCell = new Cell(0, 0);
//        ruleChecker.currCell.setCellState(1);
//        ruleChecker.setAliveCellsNewState(1);
//        int state = ruleChecker.currCell.getCellState();
//        assertEquals(0, state);
//    }
//
//    @Test
//    public void testSetAliveCellsNewStateWith2() throws Exception {
//        ruleChecker = new RuleChecker();
//        ruleChecker.currCell = new Cell(0, 0);
//        ruleChecker.currCell.setCellState(1);
//        ruleChecker.setAliveCellsNewState(2);
//        int state = ruleChecker.currCell.getCellState();
//        assertEquals(1, state);
//    }
//
//    @Test
//    public void testSetAliveCellsNewStateWith3() throws Exception {
//        ruleChecker = new RuleChecker();
//        ruleChecker.currCell = new Cell(0, 0);
//        ruleChecker.currCell.setCellState(1);
//        ruleChecker.setAliveCellsNewState(3);
//        int state = ruleChecker.currCell.getCellState();
//        assertEquals(1, state);
//    }
//
//    @Test
//    public void testSetAliveCellsNewStateWith4() throws Exception {
//        ruleChecker = new RuleChecker();
//        ruleChecker.currCell = new Cell(0, 0);
//        ruleChecker.currCell.setCellState(1);
//        ruleChecker.setAliveCellsNewState(4);
//        int state = ruleChecker.currCell.getCellState();
//        assertEquals(0, state);
//    }
}
