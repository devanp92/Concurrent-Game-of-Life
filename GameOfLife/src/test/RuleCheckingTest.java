package test;

import cis4930.gameoflife.Cell;
import cis4930.gameoflife.Grid;
import cis4930.gameoflife.RuleChecking;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created by devan on 3/29/14.
 */
public class RuleCheckingTest {
    Grid grid;
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testDivideLists() throws Exception {
        grid = new Grid(4);
        RuleChecking ruleChecking = new RuleChecking();
    /* ToDO  cemented the stamens bellow to enable compilation*/
        /*
        List<List<Cell>> lists = ruleChecking.divideLists(4);
        assertNotNull(lists);
        */
    }
}