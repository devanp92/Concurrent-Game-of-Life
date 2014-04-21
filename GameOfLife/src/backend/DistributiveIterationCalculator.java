package backend;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DistributiveIterationCalculator {
	private Grid grid;
	private Grid newGridToSet;

	public DistributiveIterationCalculator(Grid grid) throws Exception {
		if (grid == null) {
			throw new NullPointerException("The grid is null");
		}
		this.grid = grid;
		this.newGridToSet = new Grid(grid.getNumRows());
	}

	/**
	 * Finds subsets of cells from grid depending on how many cells per set
	 *
	 * @param numClients How many cells you want per array
	 * @return List of arrays that contains an array of cells
	 */
	public List<AtomicReference[]> findSubSetsOfCellsForClients(int numClients) {
		int gridSize = (int) Math.pow(grid.numRows, 2);
		int numCellsPerThread = gridSize / numClients;
		List<AtomicReference[]> list = new ArrayList<>();

		for (int i = 0; i < gridSize; i += numCellsPerThread) {
			AtomicReference[] subSet;
			if (list.size() + 1 == numClients) {
				subSet = grid.getSubSetOfGrid(i, gridSize);
				list.add(subSet);
				break;
			} else {
				subSet = grid.getSubSetOfGrid(i, i + numCellsPerThread);
				list.add(subSet);
			}
		}
		return list;
	}

	public void mergeClientCalculations(List<AtomicReference[]> list) {
		ArrayList<Thread> threadList = new ArrayList<Thread>();
		for(final AtomicReference[] ar : list) {
			Thread t = new Thread() {
				@Override
				public void run() {
					for(Object o : ar) {
						Cell c = (Cell) o;
						newGridToSet.setCell(c);
					}
				}
			};
			t.start();
			threadList.add(t);
		}
		
		for(Thread t : threadList) {
			try {
				t.join();
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
