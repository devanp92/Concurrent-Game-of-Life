package test;

import backend.DistributiveIterationCalculator;
import backend.Grid;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertNotNull;

/**
 * Created by devan on 4/19/14.
 */
public class DistributiveIterationCalculatorTest {
    private static final int NUM_ROWS = 4;
    Grid grid;
    DistributiveIterationCalculator distributiveIterationCalculator;

    @Before
    public void setUp() throws Exception {
        grid = new Grid(NUM_ROWS);
        distributiveIterationCalculator = new DistributiveIterationCalculator(grid);
    }

    @Test
    public void testMergeClientCalculations() throws Exception {
//        List<AtomicReference[]> list = distributiveIterationCalculator.findSubSetsOfCellsForThread(5);
//        assertNotNull(list);

    }
}
