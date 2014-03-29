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
	public int size() {
		int retVal = 0;
		for(QuadTreeElement qte : quadrants.values()) {
			if(qte instanceof QuadTree) {
				QuadTree qTree = (QuadTree) qte;
				retVal += qTree.size();
			}
			else {
				retVal += 1;
			}
		}
		return retVal;
	}

	public void insert(QuadTreeElement e) throws IndexOutOfBoundsException {
		if(!e.inRegion(this)) {
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
	
	/**Breadth-first divisioning; produces no more than numOfDivisions divisions*/
	public ArrayList<QuadTreeElement> divideLess(int numOfDivisions) {
		ArrayList<QuadTreeElement> list = new ArrayList<QuadTreeElement>();
		list.add(this);
		int index = 0;
		while(list.size() < numOfDivisions && index < list.size()) {
			QuadTreeElement q = list.get(index);
			if(q instanceof QuadTree) {
				QuadTree qTree = (QuadTree) q;
				if(qTree.quadrants.size()+list.size()-1 <= numOfDivisions) {
					list.remove(index);
					for(QuadTreeElement qte : qTree.quadrants.values()) {
						list.add(qte);
					}
				}
				else {
					index++;
				}
			}
			else {
				index++;
			}
		}
		return list;
	}
	
	/**Breadth-first divisioning; produces at least numOfDivisions divisions*/
	public ArrayList<QuadTreeElement> divideMore(int numOfDivisions) {
		ArrayList<QuadTreeElement> list = new ArrayList<QuadTreeElement>();
		list.add(this);
		int index = 0;
		while(list.size() < numOfDivisions && index < list.size()) {
			QuadTreeElement q = list.get(index);
			if(q instanceof QuadTree) {
				QuadTree qTree = (QuadTree) q;
				list.remove(index);
				for(QuadTreeElement qte : qTree.quadrants.values()) {
					list.add(qte);
				}
			}
			else {
				index++;
			}
		}
		return list;
	}
	
	private boolean hasSubTrees() {
		boolean retVal = false;
		for(QuadTreeElement qte : quadrants.values()) {
			if(qte instanceof QuadTree) {
				retVal = true;
			}
		}
		return retVal;
	}
}