package cis4930.gameoflife;

public abstract class QuadTreeElement {
	public int x;
	public int y;
	
	public QuadrantID getQuadrantFit(QuadTree qTree) {
		if(!inRegion(qTree)) {
			throw new IndexOutOfBoundsException("Element not in QuadTree region");
		}
		QuadrantID retVal = null;
		if(y < qTree.y + qTree.height/2) {//TOP
			if(x < qTree.x + qTree.width/2) {
				retVal = QuadrantID.TOP_LEFT;
			}
			else {
				retVal = QuadrantID.TOP_RIGHT;
			}
		}
		else {//BOTTOM
			if(x < qTree.x + qTree.width/2) {
				retVal = QuadrantID.BOT_LEFT;
			}
			else {
				retVal = QuadrantID.BOT_RIGHT;
			}
		}
		return retVal;
	}

	public boolean inRegion(QuadTree qTree) {
		boolean retVal = true;
		if(x < qTree.x)
			retVal = false;
		else if(x > qTree.x + qTree.width)
			retVal = false;
		else if(y < qTree.y)
			retVal = false;
		else if(y > qTree.y + qTree.height)
			retVal = false;
		return retVal;
	}
	
	public boolean equals(QuadTreeElement e) {
		return x == e.x && y == e.y;
	}
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}