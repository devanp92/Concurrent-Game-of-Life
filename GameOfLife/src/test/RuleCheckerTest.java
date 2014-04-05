package test;

import backend.Grid;
import backend.RuleChecker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by devan on 3/29/14.
 */
public class RuleCheckerTest {
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
        RuleChecker ruleChecker = new RuleChecker();
    /* ToDO  cemented the stamens bellow to enable compilation*/
        /*
        List<List<Cell>> lists = ruleChecker.divideLists(4);
        assertNotNull(lists);
        */
    }
}