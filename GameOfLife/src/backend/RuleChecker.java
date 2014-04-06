package backend;

import java.util.List;


public class RuleChecker extends CoordinateCalculator implements Runnable {

    private Cell currCell;

    public RuleChecker(Cell cellToCheck) {
        this.currCell = cellToCheck;
    }

    //TODO add rule checking
/*
Any live cell with fewer than two live neighbours dies, as if caused by under-population.
Any live cell with two or three live neighbours lives on to the next generation.
Any live cell with more than three live neighbours dies, as if by overcrowding.
Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
*/
    private void checkRules() {
        NeighborFinder neighborFinder = new NeighborFinder(currCell);
        int isCurrCellAliveOrDead = currCell.getCellState();
        int sum = numberOfAliveCellsAroundCurrentCell(neighborFinder.findNeighbors());

        int isCurrCellAliveOrDeadAfterCalc = 0;
        switch (isCurrCellAliveOrDead) {
            case 0:
                //TODO check if it will become alive or not
                isCurrCellAliveOrDeadAfterCalc = deadCellsNewState(sum);
                break;
            case 1:
                //TODO determine if it stays alive or dies
                isCurrCellAliveOrDeadAfterCalc = aliveCellsNewState(sum);
                break;

        }
    }

    private int deadCellsNewState(int sum) {
        return 0;
    }

    private int aliveCellsNewState(int sum) {
        return 0;
    }

    private int numberOfAliveCellsAroundCurrentCell(List<Cell> listOfNeighbors) {
        int sum = 0;
        for (Cell cell : listOfNeighbors) {
            sum += cell.getCellState();
        }
        return sum;
    }


    @Override
    public void run() {

    }
}
