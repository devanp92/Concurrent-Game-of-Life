package cis4930.gameoflife;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by devan on 3/29/14.
 */
public class RuleChecking implements Runnable {

    private Cell[][] grid = Grid.getGrid();


    public List<Cell[][]> divideLists(int numThreads) {
        List<Cell[][]> listOfSubListsSplitByNumThreads = new ArrayList<Cell[][]>();
        int numCellsInTotalGrid = (int) Math.pow(grid.length, 2);
        int beginningLength = 0;
        for (int i = 1; i <= numThreads; i++) {
            int endLength = i / numThreads * numCellsInTotalGrid;
            Cell[][] list = Arrays.copyOfRange(grid, beginningLength, endLength);
            listOfSubListsSplitByNumThreads.add(list);
            beginningLength = endLength;
        }
        return listOfSubListsSplitByNumThreads;
    }

    public Cell[][] applyRule(Cell[][] cells){
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                Cell cell = cells[i][j];
            }
        }
        return null;
    }

    @Override
    public void run() {

    }
}
