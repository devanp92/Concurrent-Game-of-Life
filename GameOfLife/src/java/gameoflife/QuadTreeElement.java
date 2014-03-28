package java.gameoflife;

public abstract class QuadTreeElement {
	int width;
	int height;
	int xOffset;
	int yOffset;
	
	/**Returns true if element <i>e</i> fits in the left half of this QuadTreeElement, false otherwise*/
	public boolean fitsInLeftHalf(QuadTreeElement e) {
		boolean retVal = false;
		if(e.xOffset < xOffset + width) {
			retVal = true;
		}
		return retVal;
	}
	
	/**Returns true if element <i>e</i> fits in the top half of this QuadTreeElement, false otherwise*/
	public boolean fitsInTopHalf(QuadTreeElement e) {
		boolean retVal = false;
		if(e.yOffset < yOffset + height) {
			retVal = true;
		}
		return retVal;
	}
	
	public boolean inRegion(QuadTreeElement e) {
		boolean retVal = true;
		if(e.xOffset < xOffset) {
			retVal = false;
		}
		else if(e.xOffset > xOffset+width) {
			retVal = false;
		}
		else if(e.yOffset < yOffset) {
			retVal = false;
		}
		else if(e.yOffset > yOffset+height) {
			retVal = false;
		}
		return retVal;
	}
}