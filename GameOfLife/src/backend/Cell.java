package backend;

public class Cell extends QuadTreeElement{

    int cellState;
    CellCase cellCase;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        setCellState(0);
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
    

    public CellCase setCellCase(int i, int j, int numRows) {
        if ((i == 0 && j == 0) || (i == numRows - 1 && j == 0) || (i == numRows - 1 && j == numRows - 1)) {
            return CellCase.TOP_LEFT_CORNER;
        } else if (i == 0 && j == numRows - 1) {
            return CellCase.BOTTOM_LEFT_CORNER;
        } else if (i == numRows - 1 && j == 0) {
            return CellCase.TOP_RIGHT_CORNER;
        } else if (i == numRows - 1 && j == numRows - 1) {
            return CellCase.BOTTOM_RIGHT_CORNER;
        } else if (j == 0 && i > 0 && i < numRows - 1) {
            return CellCase.TOP_BORDER;
        } else if (j == numRows - 1 && i > 0 && i < numRows - 1) {
            return CellCase.BOTTOM_BORDER;
        } else if (i == 0 && j > 0 && j < numRows - 1) {
            return CellCase.LEFT_BORDER;
        } else if (i == numRows - 1 && j > 0 && j < numRows - 1) {
            return CellCase.RIGHT_BORDER;
        } else {
            return CellCase.MIDDLE;
        }
    }

    public CellCase getCellCase() {
        return cellCase;
    }

}
