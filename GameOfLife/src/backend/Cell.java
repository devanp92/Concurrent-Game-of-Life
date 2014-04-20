package backend;

import java.io.Serializable;
import java.util.InputMismatchException;

public class Cell extends Coordinate implements Serializable {
	private static final long serialVersionUID = -2777226268857678244L;
	
	private int cellState;
    private CellCase cellCase;

    public Cell(int x, int y) throws Exception {
        this.x = x;
        this.y = y;
        setCellState(0);
    }

    public void setCellCase(int numRows) throws Exception {
        int i = this.x;
        int j = this.y;
        if (i >= numRows || i < 0 || j >= numRows || j < 0) {
            throw new InputMismatchException("i or j have incorrect coordinates");
        }
        int numRowsMinusOne = numRows - 1;
        if ((i == 0 && j == 0)) {
            this.cellCase = CellCase.TOP_LEFT_CORNER;
        } else if (i == numRowsMinusOne && j == 0) {
            this.cellCase = CellCase.BOTTOM_LEFT_CORNER;
        } else if (i == 0 && j == numRowsMinusOne) {
            this.cellCase = CellCase.TOP_RIGHT_CORNER;
        } else if (i == numRowsMinusOne && j == numRowsMinusOne) {
            this.cellCase = CellCase.BOTTOM_RIGHT_CORNER;
        } else if (i == 0 && j > 0 && j <= numRowsMinusOne) {
            this.cellCase = CellCase.TOP_BORDER;
        } else if (i == numRowsMinusOne && j > 0 && j <= numRowsMinusOne) {
            this.cellCase = CellCase.BOTTOM_BORDER;
        } else if (i > 0 && i <= numRowsMinusOne && j == 0) {
            this.cellCase = CellCase.LEFT_BORDER;
        } else if (i > 0 && i <= numRowsMinusOne && j == numRowsMinusOne) {
            this.cellCase = CellCase.RIGHT_BORDER;
        } else {
            this.cellCase = CellCase.MIDDLE;
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
        if(cellState < 0 || cellState > 1){
            throw new IllegalArgumentException("Illegal cell state");
        }
        this.cellState = cellState;
    }

    public int getCellState() {
        return this.cellState;
    }

}
