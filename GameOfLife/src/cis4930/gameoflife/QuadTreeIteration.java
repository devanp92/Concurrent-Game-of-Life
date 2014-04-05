package cis4930.gameoflife;

import java.util.HashMap;

public class QuadTreeIteration {
	private QuadTree prevQuadTree = null;
	private QuadTree qTree;
	private enum Edge {
		NORTH, EAST, SOUTH, WEST;
	}
	private enum Corner {
		NORTHEAST, SOUTHEAST, SOUTHWEST, NORTHWEST;
	}
	private HashMap<Edge, HashMap<QuadTreeElement, Integer>> innerEdges = new HashMap<Edge, HashMap<QuadTreeElement, Integer>>();
	private HashMap<Edge, HashMap<QuadTreeElement, Integer>> outerEdges = new HashMap<Edge, HashMap<QuadTreeElement, Integer>>();
	//private HashMap<Corner, Integer> innerCorners = new HashMap<Corner, Integer>();
	//private HashMap<Corner, Integer> outerCorners = new HashMap<Corner, Integer>();
	private HashMap<Corner, HashMap<QuadTreeElement, Integer>> innerCorners = new HashMap<Corner, HashMap<QuadTreeElement, Integer>>();
	private HashMap<Corner, HashMap<QuadTreeElement, Integer>> outerCorners = new HashMap<Corner, HashMap<QuadTreeElement, Integer>>();
	

	public QuadTreeIteration(QuadTree qTree) {
		prevQuadTree = qTree;
		qTree = new QuadTree(qTree.width, qTree.height, qTree.x, qTree.y);
	}
	/**Inserts items into appropriate innerEdges, outerEdges, innerCorners, or outerCorners*/
	public void insertBorder(HashMap<QuadTreeElement, Integer> items) {
		for(QuadTreeElement qte : items.keySet()) {
			HashMap<QuadTreeElement, Integer> cornerPiece = new HashMap<QuadTreeElement, Integer>();
			cornerPiece.put(qte, 1);
			if(qte.x == qTree.x && qte.y == qTree.y) {					//INNER_CORNERS
				innerCorners.put(Corner.NORTHWEST, cornerPiece);
			}
			else if(qte.x == qTree.x && qte.y == qTree.y+qTree.height) {
				innerCorners.put(Corner.SOUTHWEST, cornerPiece);
			}
			else if(qte.x == qTree.x+qTree.width && qte.y == qTree.y) {
				innerCorners.put(Corner.NORTHEAST, cornerPiece);
			}
			else if(qte.x == qTree.x+qTree.width && qte.y == qTree.y+qTree.height) {
				innerCorners.put(Corner.SOUTHEAST, cornerPiece);
			}
			else if(qte.x == qTree.x-1 && qte.y == qTree.y-1) {			//OUTER_CORNERS
				outerCorners.put(Corner.NORTHWEST, cornerPiece);
			}
			else if(qte.x == qTree.x-1 && qte.y == qTree.y+qTree.height+1) {
				outerCorners.put(Corner.SOUTHWEST, cornerPiece);
			}
			else if(qte.x == qTree.x+qTree.width+1 && qte.y == qTree.y-1) {
				outerCorners.put(Corner.NORTHEAST, cornerPiece);
			}
			else if(qte.x == qTree.x+qTree.width+1 && qte.y == qTree.y+qTree.height+1) {
				outerCorners.put(Corner.SOUTHEAST, cornerPiece);
			}
			else if(qte.x == qTree.x) {									//INNER_EDGES
				if(!innerEdges.containsKey(Edge.WEST)) {
					innerEdges.put(Edge.WEST, new HashMap<QuadTreeElement, Integer>());
				}
				innerEdges.get(Edge.WEST).put(qte, items.get(qte));
			}
			else if(qte.x == qTree.x+qTree.width) {
				if(!innerEdges.containsKey(Edge.EAST)) {
					innerEdges.put(Edge.EAST, new HashMap<QuadTreeElement, Integer>());
				}
				innerEdges.get(Edge.EAST).put(qte, items.get(qte));
			}
			else if(qte.y == qTree.y) {
				if(!innerEdges.containsKey(Edge.NORTH)) {
					innerEdges.put(Edge.NORTH, new HashMap<QuadTreeElement, Integer>());
				}
				innerEdges.get(Edge.NORTH).put(qte, items.get(qte));
			}
			else if(qte.y == qTree.y+qTree.height) {
				if(!innerEdges.containsKey(Edge.SOUTH)) {
					innerEdges.put(Edge.SOUTH, new HashMap<QuadTreeElement, Integer>());
				}
				innerEdges.get(Edge.SOUTH).put(qte, items.get(qte));
			}
			else if(qte.x == qTree.x-1) {								//OUTER_EDGES
				if(!outerEdges.containsKey(Edge.WEST)) {
					outerEdges.put(Edge.WEST, new HashMap<QuadTreeElement, Integer>());
				}
				outerEdges.get(Edge.WEST).put(qte, items.get(qte));
			}
			else if(qte.x == qTree.x+qTree.width+1) {
				if(!outerEdges.containsKey(Edge.EAST)) {
					outerEdges.put(Edge.EAST, new HashMap<QuadTreeElement, Integer>());
				}
				outerEdges.get(Edge.EAST).put(qte, items.get(qte));
			}
			else if(qte.y == qTree.y-1) {
				if(!outerEdges.containsKey(Edge.NORTH)) {
					outerEdges.put(Edge.NORTH, new HashMap<QuadTreeElement, Integer>());
				}
				outerEdges.get(Edge.NORTH).put(qte, items.get(qte));
			}
			else if(qte.y == qTree.y+qTree.height+1) {
				if(!outerEdges.containsKey(Edge.SOUTH)) {
					outerEdges.put(Edge.SOUTH, new HashMap<QuadTreeElement, Integer>());
				}
				outerEdges.get(Edge.SOUTH).put(qte, items.get(qte));
			}
			else {
				if(prevQuadTree.contains(qte)) {
					if(2 <= items.get(qte) && items.get(qte) <= 3) {
						qTree.insert(qte);
					}
				}
				else {
					if(items.get(qte) == 3) qTree.insert(qte);
				}
			}
		}
	}
	
	private void insertIfLegal(QuadTreeElement qte, int neighborCount) {
		if(prevQuadTree.contains(qte)) {
			if(2 <= neighborCount && neighborCount <= 3) {
				qTree.insert(qte);
			}
		}
		else {
			if(neighborCount== 3) qTree.insert(qte);
		}
	}
	
	private void insertAllIfLegal(HashMap<QuadTreeElement, Integer> x) {
		for(QuadTreeElement qte : x.keySet()) {
			insertIfLegal(qte, x.get(qte));
		}
	}
	
	public static QuadTreeIteration mergeQuadTreeIteration(	QuadTreeIteration NE,
															QuadTreeIteration NW, 
															QuadTreeIteration SW, 
															QuadTreeIteration SE) {
		QuadTree qTree = new QuadTree(NE.prevQuadTree, NW.prevQuadTree, SW.prevQuadTree, SE.prevQuadTree);
		QuadTreeIteration merge = new QuadTreeIteration(qTree);
		//Set OUTER_CORNERS of mergedIteration
		if(NE.outerCorners.containsKey(Corner.NORTHEAST)) {
			merge.outerCorners.put(Corner.NORTHEAST, NE.outerCorners.get(Corner.NORTHEAST));
		}
		if(NW.outerCorners.containsKey(Corner.NORTHWEST)) {
			merge.outerCorners.put(Corner.NORTHWEST, NW.outerCorners.get(Corner.NORTHWEST));
		}
		if(SW.outerCorners.containsKey(Corner.SOUTHWEST)) {
			merge.outerCorners.put(Corner.SOUTHWEST, SW.outerCorners.get(Corner.SOUTHWEST));
		}
		if(SE.outerCorners.containsKey(Corner.SOUTHEAST)) {
			merge.outerCorners.put(Corner.SOUTHEAST, SE.outerCorners.get(Corner.SOUTHEAST));
		}
		//Set INNER_CORNERS of mergedIteration
		//count in each quadrant
		int[] count = new int[4];
		//QuadTreeElement qte = NW.innerCorners.get(Corner.SOUTHEAST).keySet()
		//count[0] += NW.innerCorners.get(Corner.SOUTHEAST).get(0);
		//count[0] += SE.outerCorners.get(Corner.NORTHWEST).get(0);
		//if(NE.outerEdges.containsKey(key))
		
		if(NE.outerCorners.containsKey(Corner.NORTHEAST)) {
			merge.outerCorners.put(Corner.NORTHEAST, NE.outerCorners.get(Corner.NORTHEAST));
		}
		if(NW.outerCorners.containsKey(Corner.NORTHWEST)) {
			merge.outerCorners.put(Corner.NORTHWEST, NW.outerCorners.get(Corner.NORTHWEST));
		}
		if(SW.outerCorners.containsKey(Corner.SOUTHWEST)) {
			merge.outerCorners.put(Corner.SOUTHWEST, SW.outerCorners.get(Corner.SOUTHWEST));
		}
		if(SE.outerCorners.containsKey(Corner.SOUTHEAST)) {
			merge.outerCorners.put(Corner.SOUTHEAST, SE.outerCorners.get(Corner.SOUTHEAST));
		}
		
		//MERGEABLE INNER-OUTER EDGES
		//format: QUADRANT edge
		HashMap<QuadTreeElement, Integer> NWwest = mergeEdge(NW.innerEdges.get(Edge.WEST), NE.outerEdges.get(Edge.EAST));
		HashMap<QuadTreeElement, Integer> NEeast = mergeEdge(NE.innerEdges.get(Edge.EAST), NW.outerEdges.get(Edge.WEST));
		HashMap<QuadTreeElement, Integer> SWwest = mergeEdge(SW.innerEdges.get(Edge.WEST), SE.outerEdges.get(Edge.EAST));
		HashMap<QuadTreeElement, Integer> SWeast = mergeEdge(SE.innerEdges.get(Edge.EAST), SW.outerEdges.get(Edge.WEST));
		HashMap<QuadTreeElement, Integer> NWsouth = mergeEdge(NW.innerEdges.get(Edge.SOUTH), SW.outerEdges.get(Edge.NORTH));
		HashMap<QuadTreeElement, Integer> SWnorth = mergeEdge(SW.innerEdges.get(Edge.NORTH), NW.outerEdges.get(Edge.SOUTH));
		HashMap<QuadTreeElement, Integer> NEsouth = mergeEdge(NE.innerEdges.get(Edge.SOUTH), SE.outerEdges.get(Edge.NORTH));
		HashMap<QuadTreeElement, Integer> SEnorth = mergeEdge(SE.innerEdges.get(Edge.NORTH), NE.outerEdges.get(Edge.SOUTH));
		
		merge.insertAllIfLegal(NWwest);
		merge.insertAllIfLegal(NEeast);
		merge.insertAllIfLegal(SWwest);
		merge.insertAllIfLegal(SWeast);
		merge.insertAllIfLegal(NWsouth);
		merge.insertAllIfLegal(SWnorth);
		merge.insertAllIfLegal(NEsouth);
		merge.insertAllIfLegal(SEnorth);
		
		
		
		return merge;
	}
	
	private static HashMap<QuadTreeElement, Integer> mergeEdge(HashMap<QuadTreeElement, Integer> a, HashMap<QuadTreeElement, Integer> b) {
		HashMap<QuadTreeElement, Integer> retVal = new HashMap<QuadTreeElement, Integer>();
		for(QuadTreeElement qte : a.keySet()) {
			int val = a.get(qte);
			if(b.containsKey(qte)) {
				val += b.get(qte);
			}
			retVal.put(qte, val);
		}
		for(QuadTreeElement qte : a.keySet()) {
			if(!a.containsKey(qte)) {
				 retVal.put(qte, b.get(qte));
			}
		}
		return retVal;
	}
}
