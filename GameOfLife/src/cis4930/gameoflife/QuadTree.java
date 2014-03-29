package cis4930.gameoflife;

public class QuadTree extends QuadTreeElement {
	QuadTreeElement topRight = null;
	QuadTreeElement topLeft = null;
	QuadTreeElement botLeft = null;
	QuadTreeElement botRight = null;
	
	int width;
	int height;

	public QuadTree(int width, int height) {
		this.width = width;
		this.height = height;
	}
	public QuadTree(int width, int height, int xOffset, int yOffset) {
		this(width, height);
		this.x = xOffset;
		this.y = yOffset;
	}

	public void insert(QuadTreeElement e) throws IndexOutOfBoundsException {
		if(!inRegion(e)) {
			throw new IndexOutOfBoundsException("Element not in QuadTree region");
		}
		if(fitsInTopHalf(e)) {
			if(fitsInLeftHalf(e)) {
				//TOPLEFT
				if(topLeft == null) {
					topLeft = e;
				}
				else if(topLeft instanceof QuadTree) {
					QuadTree qTree = (QuadTree) topLeft;
					qTree.insert(e);
				}
				else {
					QuadTreeElement oldElement = topLeft;
					topLeft = new QuadTree(width/2, height/2, x, y);
					((QuadTree)topLeft).insert(oldElement);
				}
			}
			else {
				//TOPRIGHT
				if(topRight == null) {
					topRight = e;
				}
				else if(topRight instanceof QuadTree) {
					QuadTree qTree = (QuadTree) topRight;
					qTree.insert(e);
				}
				else {
					QuadTreeElement oldElement = topRight;
					topRight = new QuadTree(width-width/2, height/2, width-width/2+x, y);
					((QuadTree)topRight).insert(oldElement);
				}
			}
		}
		else {
			if(fitsInLeftHalf(e)) {
				//BOTLEFT
				if(botLeft == null) {
					botLeft = e;
				}
				else if(botLeft instanceof QuadTree) {
					QuadTree qTree = (QuadTree) botLeft;
					qTree.insert(e);
				}
				else {
					QuadTreeElement oldElement = botLeft;
					botLeft = new QuadTree(width/2, height-height/2, x, height-height/2+y);
					((QuadTree)botLeft).insert(oldElement);
				}
			}
			else {
				//BOTRIGHT
				if(botRight == null) {
					botRight = e;
				}
				else if(botRight instanceof QuadTree) {
					QuadTree qTree = (QuadTree) botRight;
					qTree.insert(e);
				}
				else {
					QuadTreeElement oldElement = botRight;
					botRight = new QuadTree(width-width/2, height-height/2, width-width/2+x, height-height/2+y);
					((QuadTree)botRight).insert(oldElement);
				}
			}
		}
		
	}


	public boolean remove(QuadTreeElement x) {
		return remove(x, null);
	}
	private boolean remove(QuadTreeElement x, QuadTree parent) {
		boolean found = false;
		if(x.equals(topLeft)) {
			found = true;
			topLeft = null;
		}
		else if(x.equals(topRight)) {
			found = true;
			topRight = null;
		}
		else if(x.equals(botLeft)) {
			found = true;
			botLeft = null;
		}
		else if(x.equals(botRight)) {
			found = true;
			botRight = null;
		}
		
		if(found) {
			//clean up if necessary
		}
		else {
			
		}
		return found;
	}
	
	
	
	/**Returns true if element <i>e</i> fits in the left half of this QuadTreeElement, false otherwise*/
	public boolean fitsInLeftHalf(QuadTreeElement e) {
		boolean retVal = false;
		if(e.x < x + width) {
			retVal = true;
		}
		return retVal;
	}
	
	/**Returns true if element <i>e</i> fits in the top half of this QuadTreeElement, false otherwise*/
	public boolean fitsInTopHalf(QuadTreeElement e) {
		boolean retVal = false;
		if(e.y < y + height) {
			retVal = true;
		}
		return retVal;
	}
	
	public boolean inRegion(QuadTreeElement e) {
		boolean retVal = true;
		if(e.x < x) {
			retVal = false;
		}
		else if(e.x > x+width) {
			retVal = false;
		}
		else if(e.y < y) {
			retVal = false;
		}
		else if(e.y > y+height) {
			retVal = false;
		}
		return retVal;
	}
}