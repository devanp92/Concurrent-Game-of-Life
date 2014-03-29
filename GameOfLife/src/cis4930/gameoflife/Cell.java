package cis4930.gameoflife;

public class Cell extends QuadTreeElement {
	boolean isAlive;

	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}

    /**
     * Set if the cell is alive or not.
     * True if it is alive, False if dead.
     * @param isAlive
     */
	public void setLife(boolean isAlive) {
		this.isAlive = isAlive;
	}
}
