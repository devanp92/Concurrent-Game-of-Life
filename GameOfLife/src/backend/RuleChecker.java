package backend;

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

    @Override
    public void run() {

    }
}
