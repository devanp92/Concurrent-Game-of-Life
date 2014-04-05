package cis4930.gameoflife;

public class Cell extends QuadTreeElement {

    int isAlive;
    CellCase cellCase;

    public Cell(int x, int y) {
		this.x = x;
		this.y = y;
        setLife(0);
	}

    /**
     * Set if the cell is alive or not.
     * @param isAlive 1 if it is alive, 0 if dead.
     */
	public void setLife(int isAlive) {
		this.isAlive = isAlive;
	}

    public CellCase setCellCase(int i, int j, int numRows) {
        if ((i == 0 && j == 0) || (i == 0 && j == numRows - 1) || (i == numRows - 1 && j == 0) || (i == numRows - 1 && j == numRows - 1)) {
            return CellCase.CORNER;

        } else if ((j == 0 && i > 0 && i < numRows - 1) || (j == numRows - 1 && i > 0 && i < numRows - 1) || (i == 0 && j > 0 && j < numRows - 1) || ((i == numRows - 1 && j > 0 && j < numRows - 1))) {
            return CellCase.BORDER;

        } else {
            return CellCase.MIDDLE;
        }
    }
    public CellCase getCellCase() {
        return cellCase;
    }

}
