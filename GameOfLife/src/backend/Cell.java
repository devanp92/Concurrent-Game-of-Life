package backend;

import java.util.InputMismatchException;

public class Cell extends QuadTreeElement {

    private int cellState;
    private CellCase cellCase;

    public Cell(int x, int y) throws Exception {
        this.x = x;
        this.y = y;
        setCellState(0);
    }

    public CellCase setCellCase(int i, int j, int numRows) throws Exception {
        if (i >= numRows || i < 0 || j >= numRows || j < 0) {
            throw new InputMismatchException("i or j have incorrect coordinates");
        }
        int numRowsMinusOne = numRows - 1;
        if ((i == 0 && j == 0)) {
            return CellCase.TOP_LEFT_CORNER;
        } else if (i == 0 && j == numRowsMinusOne) {
            return CellCase.BOTTOM_LEFT_CORNER;
        } else if (i == numRowsMinusOne && j == 0) {
            return CellCase.TOP_RIGHT_CORNER;
        } else if (i == numRowsMinusOne && j == numRowsMinusOne) {
            return CellCase.BOTTOM_RIGHT_CORNER;
        } else if (j == 0 && i > 0 && i < numRowsMinusOne) {
            return CellCase.TOP_BORDER;
        } else if (j == numRowsMinusOne && i > 0 && i < numRowsMinusOne) {
            return CellCase.BOTTOM_BORDER;
        } else if (i == 0 && j > 0 && j < numRowsMinusOne) {
            return CellCase.LEFT_BORDER;
        } else if (i == numRowsMinusOne && j > 0 && j < numRowsMinusOne) {
            return CellCase.RIGHT_BORDER;
        } else {
            return CellCase.MIDDLE;
        }
    }

    public CellCase getCellCase() {
        return cellCase;
    }

    /**
     * Set if the cell is alive or not.
     *
     * @param cellState 1 if it is alive, 0 if dead.
     */
    public void setCellState(int cellState) {
        this.cellState = cellState;
    }

    public int getCellState() {
        return this.cellState;
    }

}
