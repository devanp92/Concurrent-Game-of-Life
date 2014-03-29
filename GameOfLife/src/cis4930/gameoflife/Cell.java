package cis4930.gameoflife;

public class Cell extends QuadTreeElement {
	int isAlive;

	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}

    /**
     * Set if the cell is alive or not.
     * @param isAlive 1 if it is alive, 0 if dead.
     */
	public void setLife(int isAlive) {
		this.isAlive = isAlive;
	}
}
