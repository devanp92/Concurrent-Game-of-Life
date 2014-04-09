package backend;

public class QuadTreeTest {
	public static void main(String[] args) {
		QuadTree q = new QuadTree(4,4);
		q.insert(new Cell(1,1));
		q.insert(new Cell(2,1));
		q.insert(new Cell(1,2));
		q.insert(new Cell(2,2));

		QuadTree nw = new QuadTree(2,2,0,0);
		nw.insert(new Cell(1,1));
		QuadTree ne = new QuadTree(2,2,2,0);
		ne.insert(new Cell(2,1));
		QuadTree sw = new QuadTree(2,2,0,2);
		sw.insert(new Cell(1,2));
		QuadTree se = new QuadTree(2,2,2,2);
		se.insert(new Cell(2,2));

		QuadTreeIteration qti = q.getNextIteration();
		QuadTree next = qti.getQuadTree();

		QuadTreeIteration qtiMerge = QuadTreeIteration.mergeQuadTreeIteration(ne.getNextIteration(),
																			  nw.getNextIteration(),
																			  sw.getNextIteration(),
																			  se.getNextIteration());
		QuadTree nextMerged = qtiMerge.getQuadTree();

		System.out.println(q.size() + ": " + q.getItemList());
		System.out.println(next.size() + ": " + next.getItemList());
		System.out.println(nextMerged.size() + ": " + nextMerged.getItemList());


	}

}
