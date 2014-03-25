package java.gameoflife;

public class QuadTree extends QuadTreeElement {
	QuadTreeElement topRight = null;
	QuadTreeElement topLeft = null;
	QuadTreeElement botLeft = null;
	QuadTreeElement botRight = null;

	public QuadTree(int width, int height) {
		this.width = width;
		this.height = height;
	}
	public QuadTree(int width, int height, int xOffset, int yOffset) {
		this(width, height);
		this.xOffset = xOffset;
		this.yOffset = yOffset;
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
					topLeft = new QuadTree(width/2, height/2, xOffset, yOffset);
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
					topRight = new QuadTree(width/2, height/2, width/2+xOffset, yOffset);
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
					botLeft = new QuadTree(width/2, height/2, xOffset, height/2+yOffset);
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
					botRight = new QuadTree(width/2, height/2, width/2+xOffset, height/2+yOffset);
					((QuadTree)botRight).insert(oldElement);
				}
			}
		}
		
	}


	public boolean remove(QuadTreeElement x) {
		//TODO
		return false;
	}
}