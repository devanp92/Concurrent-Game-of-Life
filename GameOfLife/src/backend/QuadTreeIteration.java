package backend;

import java.util.HashMap;

public class QuadTreeIteration {
	private QuadTree prevQuadTree = null;
	private QuadTree qTree;
	
	public QuadTree getQuadTree() {
		QuadTree nextQuadTree = new QuadTree(qTree);
		for(QuadTreeElement qte : innerBorder.keySet()) {
			if(isLegalInsertion(qte, innerBorder.get(qte))) {
				nextQuadTree.insert(qte);
			}
		}
		return nextQuadTree;
	}
	
	private HashMap<QuadTreeElement, Integer> innerBorder = new HashMap<QuadTreeElement, Integer>();
	private HashMap<QuadTreeElement, Integer> outerBorder = new HashMap<QuadTreeElement, Integer>();

	public QuadTreeIteration(QuadTree qTree) {
		prevQuadTree = qTree;
		this.qTree = new QuadTree(qTree.width, qTree.height, qTree.x, qTree.y);
	}
	/**Inserts items into appropriate innerEdges, outerEdges, innerCorners, or outerCorners*/
	public void insertBorder(HashMap<QuadTreeElement, Integer> items) {
		for(QuadTreeElement qte : items.keySet()) {
			if(qte.x == qTree.x-1) {								//OUTER_EDGES
				outerBorder.put(qte, items.get(qte));
			}
			else if(qte.x == qTree.x+qTree.width) {
				outerBorder.put(qte, items.get(qte));
			}
			else if(qte.y == qTree.y-1) {
				outerBorder.put(qte, items.get(qte));
			}
			else if(qte.y == qTree.y+qTree.height) {
				outerBorder.put(qte, items.get(qte));
			}
			else if(qte.x == qTree.x) {								//INNER_EDGES
				innerBorder.put(qte, items.get(qte));
			}
			else if(qte.x == qTree.x+qTree.width-1) {
				innerBorder.put(qte, items.get(qte));
			}
			else if(qte.y == qTree.y) {
				innerBorder.put(qte, items.get(qte));
			}
			else if(qte.y == qTree.y+qTree.height-1) {
				innerBorder.put(qte, items.get(qte));
			}
			else {													//INNER_QUADTREE
				insertIfLegal(qte, items.get(qte));
			}
		}
	}
	
	private void insertIfLegal(QuadTreeElement qte, int neighborCount) {
		if(isLegalInsertion(qte,neighborCount)) {
			qTree.insert(qte);
		}
	}
	
	private boolean isLegalInsertion(QuadTreeElement qte, int neighborCount) {
		boolean retVal = false;
		if(prevQuadTree.contains(qte)) {
			if(2 <= neighborCount && neighborCount <= 3) {
				retVal = true;
			}
		}
		else {
			if(neighborCount == 3) retVal = true;
		}
		return retVal;
	}
	
	//TODO: doesn't exactly work properly yet
	public static QuadTreeIteration mergeQuadTreeIteration(	QuadTreeIteration NE,
															QuadTreeIteration NW, 
															QuadTreeIteration SW, 
															QuadTreeIteration SE) {
		QuadTree qTree = new QuadTree(NE.prevQuadTree, NW.prevQuadTree, SW.prevQuadTree, SE.prevQuadTree);
		QuadTreeIteration merge = new QuadTreeIteration(qTree);
		QuadTreeIteration[] quads = {NE, NW, SW, SE};
		HashMap<QuadTreeElement, Integer> mergedCountList = new HashMap<QuadTreeElement, Integer>();
		for(int i = 0;i<quads.length;i++) {
			for(int j = 0;j<quads.length;j++) {
				if(i == j) continue;
				for(QuadTreeElement qte : quads[i].innerBorder.keySet()) {
					if(!mergedCountList.containsKey(qte)) {
						mergedCountList.put(qte, quads[i].innerBorder.get(qte));
					}
					if(quads[j].outerBorder.containsKey(qte)) {
						mergedCountList.put(qte, mergedCountList.get(qte) + quads[j].outerBorder.get(qte));
					}
				}				
			}
		}
		System.out.println(mergedCountList);
		merge.insertBorder(mergedCountList);
		
		return merge;
	}
}
