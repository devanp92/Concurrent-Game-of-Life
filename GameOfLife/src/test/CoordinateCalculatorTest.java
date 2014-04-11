package test;

import backend.Grid;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by devan on 4/11/14.
 */
public class CoordinateCalculatorTest {
    Grid grid;

    @Before
    public void setUp() throws Exception {
        grid = new Grid(10);
    }

    @Test
    public void testConvert2DCoordinateTo1D() throws Exception {
        int testCoordinate = grid.convert2DCoordinateTo1D(5,5);
        assertEquals(55, testCoordinate);
    }

    @Test
    public void testConvert1DCoordinateTo2D() throws Exception {
        Map map = grid.convert1DCoordinateTo2D(55);
        int x = (int) map.get('x');
        int y = (int) map.get('y');

        assertEquals(5, x);
        assertEquals(5, y);

    }
}
