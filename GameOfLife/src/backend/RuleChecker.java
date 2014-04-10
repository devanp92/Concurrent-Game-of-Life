package backend;

import java.util.List;


public class RuleChecker extends CoordinateCalculator {

    private Cell currCell;


    public Cell determineCellsNextState(Cell cellToCheck) throws Exception {
        this.currCell = cellToCheck;

        NeighborFinder neighborFinder = new NeighborFinder(currCell);
        int numAliveNeighbors = numOfAliveCellsAroundCurrentCell(neighborFinder.findNeighbors());

           return setCurrCellsNextStateDependingOnNumAliveNeighbors(numAliveNeighbors);
    }

    private int numOfAliveCellsAroundCurrentCell(List<Cell> listOfNeighbors) {
        int numAliveNeighbors = 0;
        for (Cell cell : listOfNeighbors) {
            numAliveNeighbors += cell.getCellState();
        }
        return numAliveNeighbors;
    }

    private Cell setCurrCellsNextStateDependingOnNumAliveNeighbors(int numAliveNeighbors) throws Exception {
        int isCurrCellAliveOrDead = currCell.getCellState();
        Cell cell = null;
        switch (isCurrCellAliveOrDead) {
            case 0:
                cell = setDeadCellsNewState(numAliveNeighbors);
                break;
            case 1:
                cell = setAliveCellsNewState(numAliveNeighbors);
                break;
        }
        return cell;
    }

    private Cell setDeadCellsNewState(int numAliveNeighbors) throws Exception {
        Cell cell = currCell;
        if (numAliveNeighbors == 3) {
            cell.setCellState(1);
        }
        return cell;
    }

    private Cell setAliveCellsNewState(int numAliveNeighbors) throws Exception {
        Cell cell = currCell;
        if (numAliveNeighbors == 2 || numAliveNeighbors == 3) {
            cell.setCellState(1);
        } else {
            cell.setCellState(0);
        }
        return cell;
    }
}
