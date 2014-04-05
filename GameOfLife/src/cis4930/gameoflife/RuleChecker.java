package cis4930.gameoflife;

import java.util.concurrent.atomic.AtomicReference;


public class RuleChecker extends CoordinateCalculator implements Runnable {

    private AtomicReference[] grid = Grid.getGrid();
    private AtomicReference<Cell> atomicCell;

    public RuleChecker(AtomicReference<Cell> cellToCheck) {
        this.atomicCell = cellToCheck;
    }

    public void checkCase() {
        Cell cell = atomicCell.get();
        CellCase cellCase = cell.getCellCase();

        switch (cellCase) {
            case TOP_LEFT_CORNER:
                checkTopLeftCorner(cell);
                break;
            case TOP_RIGHT_CORNER:
                checkTopRightCorner(cell);
                break;
            case BOTTOM_LEFT_CORNER:
                checkBottomLeftCorner(cell);
                break;
            case BOTTOM_RIGHT_CORNER:
                checkBottomRightCorner(cell);
                break;
            case TOP_BORDER:
                checkTopBorder(cell);
                break;
            case RIGHT_BORDER:
                checkRightBorder(cell);
                break;
            case LEFT_BORDER:
                checkLeftBorder(cell);
                break;
            case BOTTOM_BORDER:
                checkBottomBorder(cell);
                break;
        }
    }

    private void checkTopLeftCorner(Cell cell) {
        int sumOfNeighbors = 0;
        Cell rightNeighbor = (Cell) grid[2].get();
        Cell bottomNeighbor = (Cell) grid[numRows].get();
        //Cell

    }

    private void checkTopRightCorner(Cell cell) {
        int sumOfNeighbors = 0;
    }

    private void checkBottomLeftCorner(Cell cell) {
        int sumOfNeighbors = 0;
    }

    private void checkBottomRightCorner(Cell cell) {
        int sumOfNeighbors = 0;
    }

    private void checkTopBorder(Cell cell) {
        int sumOfNeighbors = 0;
    }

    private void checkRightBorder(Cell cell) {
        int sumOfNeighbors = 0;
    }

    private void checkLeftBorder(Cell cell) {
        int sumOfNeighbors = 0;
    }

    private void checkBottomBorder(Cell cell) {
        int sumOfNeighbors = 0;
    }

    private void checkMiddle(Cell cell) {

    }


//                Cell cell = cells[i][j];
//                if (cell.getX() == 0) {
//                    //left side
//                } else if (cell.getX() == cells.length - 1) {
//                    //right side
//                } else if (cell.getY() == 0) {
//                    //top
//                } else if (cell.getY() == cells.length - 1) {
//                    //bottom
//                } else if (cell.getX() == 0 && cell.getY() == 0) {
//                    //top left
//                } else if (cell.getX() == 0 && cell.getY() == cells.length - 1) {
//                    //bottom left
//                } else if (cell.getX() == cells.length - 1 && cell.getY() == 0) {
//                    //top right
//                } else if (cell.getX() == cells.length - 1 && cell.getY() == cells.length - 1) {
//                    //bottom right
//                } else {
//                    //in middle
//                }
//            }
//        }
//        return null;
//    }

    @Override
    public void run() {

    }
}
