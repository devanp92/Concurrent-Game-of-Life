package cis4930.gameoflife;

import java.util.concurrent.atomic.AtomicReferenceArray;


public class RuleChecker implements Runnable {

    private AtomicReferenceArray grid = Grid.getGrid();


//    for (int i = 0; i < cells.length; i++) {
//            for (int j = 0; j < cells[0].length; j++) {
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