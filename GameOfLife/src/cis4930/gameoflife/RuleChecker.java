package cis4930.gameoflife;

import java.util.concurrent.atomic.AtomicReference;


public class RuleChecker implements Runnable {

    private AtomicReference[] grid = Grid.getGrid();

    public void checkCase(Cell cell) {

        CellCase cellCase = cell.getCellCase();

        switch (cellCase) {
            case BORDER:
                checkBorder(cell);
                break;
            case CORNER:
                checkCorner(cell);
                break;
            case MIDDLE:
                checkMiddle(cell);
                break;

        }
    }

    private void checkMiddle(Cell cell) {
        
    }

    private void checkCorner(Cell cell) {

    }

    private void checkBorder(Cell cell) {

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
