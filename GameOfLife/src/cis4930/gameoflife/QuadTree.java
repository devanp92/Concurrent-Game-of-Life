package cis4930.gameoflife;

import java.util.ArrayList;
import java.util.HashMap;

public class QuadTree extends QuadTreeElement {
	HashMap<QuadrantID, QuadTreeElement> quadrants = new HashMap<QuadrantID, QuadTreeElement>();
	
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
	
	private boolean isEmpty() {
		return quadrants.isEmpty();
	}
	private boolean contains(QuadTreeElement e) {
		boolean retVal = false;
		QuadrantID qID = e.getQuadrantFit(this);
		QuadTreeElement qTreeEl = quadrants.get(qID);
		if(qTreeEl != null && qTreeEl instanceof QuadTree) {
			QuadTree qTree = (QuadTree) qTreeEl; 
			retVal = qTree.contains(e);
		}
		return retVal;
	}

	public void insert(QuadTreeElement e) throws IndexOutOfBoundsException {
		if(!inRegion(e)) {
			throw new IndexOutOfBoundsException("Element not in QuadTree region");
		}
		
		QuadrantID qID = e.getQuadrantFit(this);
		QuadTreeElement qte = quadrants.get(qID);
		if(qte != null) {
			if(qte instanceof QuadTree) {
				QuadTree qTree = (QuadTree) qte;
				qTree.insert(e);
			}
			else {
				QuadTree newQTree = new QuadTree(qID.getHalfWidth(width),
						qID.getHalfHeight(height),
						x + qID.getXOffset(width),
						y + qID.getYOffset(height));
				newQTree.insert(qte);
				newQTree.insert(e);
				quadrants.put(qID, newQTree);
			}
		}
		else {
			quadrants.put(qID, e);
		}
	}


	public boolean remove(QuadTreeElement x) {
		return remove(x, null, null);
	}
	private boolean remove(QuadTreeElement x, QuadTree parent, QuadrantID locationInParent) {
		boolean found = false;
		
		QuadrantID qID = x.getQuadrantFit(this);
		QuadTreeElement qte = quadrants.get(qID);
		if(qte != null) {
			if(qte instanceof QuadTree) {
				QuadTree qTree = (QuadTree) qte;
				found = qTree.remove(x, this, qID);
			}
			else {
				if(x.equals(qte)) {
					found = true;
					quadrants.remove(qID);
				}
				if(isEmpty()) {
					parent.quadrants.remove(locationInParent);
				}
			}
		}
		return found;
	}
	
	public ArrayList<QuadTreeElement> getItemList() {
		ArrayList<QuadTreeElement> itemList = new ArrayList<QuadTreeElement>();
		return getItemList(itemList);
	}
	private ArrayList<QuadTreeElement> getItemList(ArrayList<QuadTreeElement> itemList) {		
		for(QuadTreeElement qte : quadrants.values()) {
			if(qte instanceof QuadTree) {
				QuadTree qTree = (QuadTree) qte;
				itemList = qTree.getItemList();
			}
			else {
				itemList.add(qte);
			}
		}
		return itemList;
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