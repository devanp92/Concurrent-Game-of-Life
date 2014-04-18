package test;

import backend.Cell;
import backend.Grid;
import backend.IterationCalculator;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by devan on 4/14/14.
 */
public class IterationCalculatorTest {
    private Grid grid;
    private final int NUM_ROWS = 4;
    private IterationCalculator iterationCalculator;


    @Before
    public void setUp() throws Exception {
        grid = new Grid(NUM_ROWS);
        grid.setCellState(1, 1);
        grid.setCellState(4, 1);
        grid.setCellState(2, 1);
        grid.setCellState(8, 1);
        grid.setCellState(15, 1);
        iterationCalculator = new IterationCalculator(grid);
    }

    @Test
    public void testCalculateNewIteration() throws Exception {

    }

    @Test
    public void testJoinThreads() throws Exception {

    }

    @Test
    public void testStartThreads() throws Exception {

    }

    @Test
    public void testInitializeCalculators() throws Exception {

    }

    @Test
    public void testFindSubSetsOfCellsForThread() throws Exception {
        List<AtomicReference[]> list = iterationCalculator.findSubSetsOfCellsForThread(5);
        for (AtomicReference[] l : list) {
            for (AtomicReference cell : l) {
                Cell c = (Cell) cell.get();
                System.out.println(c.x + ", " + c.y);
            }
            System.out.println("----------");
            //assertEquals(4, list.get(0).length);
        }
    }


}
