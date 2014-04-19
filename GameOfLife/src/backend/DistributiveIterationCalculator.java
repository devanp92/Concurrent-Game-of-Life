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
	//probably should be a method of Grid
	//alternatively inherit from an abstract parent class
	public List<AtomicReference[]> findSubSetsOfCellsForThread(int numClients) {
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
		//TODO: multithread this
		for(AtomicReference[] ar : list) {
			for(Object o : ar) {
				Cell c = (Cell) o;
				newGridToSet.setCell(c);
			}
		}
	}

}
