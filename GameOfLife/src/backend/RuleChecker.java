package backend;

import java.util.List;


public class RuleChecker extends CoordinateCalculator {

    private Cell currCell;


    public void determineCellsNextState(Cell cellToCheck) {
        this.currCell = cellToCheck;

        NeighborFinder neighborFinder = new NeighborFinder(currCell);
        int numAliveNeighbors = numOfAliveCellsAroundCurrentCell(neighborFinder.findNeighbors());

        setCurrCellsNextStateDependingOnNumAliveNeighbors(numAliveNeighbors);
     }

    private int numOfAliveCellsAroundCurrentCell(List<Cell> listOfNeighbors) {
        int numAliveNeighbors = 0;
        for (Cell cell : listOfNeighbors) {
            numAliveNeighbors += cell.getCellState();
        }
        return numAliveNeighbors;
    }

    private void setCurrCellsNextStateDependingOnNumAliveNeighbors(int numAliveNeighbors){
        int isCurrCellAliveOrDead = currCell.getCellState();
        switch (isCurrCellAliveOrDead) {
            case 0:
                setDeadCellsNewState(numAliveNeighbors);
                break;
            case 1:
                setAliveCellsNewState(numAliveNeighbors);
                break;
        }
    }

    private void setDeadCellsNewState(int numAliveNeighbors) {
        if (numAliveNeighbors == 3) {
            currCell.setCellState(1);
        }
    }

    private void setAliveCellsNewState(int numAliveNeighbors) {
        if (numAliveNeighbors == 2 || numAliveNeighbors == 3) {
            currCell.setCellState(1);
        } else {
            currCell.setCellState(0);
        }
    }

}
