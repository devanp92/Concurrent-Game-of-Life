package backend;

public class QuadTreeTest {
	public static void main(String[] args) {
		QuadTree q = new QuadTree(4,4);
		q.insert(new Cell(1,1));
		q.insert(new Cell(2,1));
		q.insert(new Cell(1,2));
		q.insert(new Cell(2,2));
		
		QuadTree a = new QuadTree(2,2,0,0);
		a.insert(new Cell(1,1));
		QuadTree b = new QuadTree(2,2,2,0);
		b.insert(new Cell(2,1));
		QuadTree c = new QuadTree(2,2,0,2);
		c.insert(new Cell(1,2));
		QuadTree d = new QuadTree(2,2,2,2);
		d.insert(new Cell(2,2));
		
		QuadTreeIteration qti = q.getNextIteration();
		QuadTree next = qti.getQuadTree();
		
		QuadTreeIteration qtiMerge = QuadTreeIteration.mergeQuadTreeIteration(b.getNextIteration(), 
																			  a.getNextIteration(),
																			  c.getNextIteration(),
																			  d.getNextIteration());
		QuadTree nextMerged = qtiMerge.getQuadTree();
		
		System.out.println(q.size() + ": " + q.getItemList());
		System.out.println(next.size() + ": " + next.getItemList());
		System.out.println(nextMerged.size() + ": " + nextMerged.getItemList());
		
		
	}

}
