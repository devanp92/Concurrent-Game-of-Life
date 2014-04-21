package backend;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import server.ClientConnection;

public class ClientIterationCalculator extends Thread {
	private Grid oldGrid;
	private AtomicReference[] oldCells;
	private ArrayList<Cell> newCells;
	private NextCellCalculator[] calculators;
	private ClientConnection callback;
	
	public ClientIterationCalculator(AtomicReference[] ar, Grid g, ClientConnection conn) throws Exception {
        if (ar == null) {
            throw new NullPointerException("The grid is null");
        }
        this.oldCells = ar;
        this.oldGrid = g;
        this.callback = conn;
    }
	
	@Override
	public void run() {
		try {
			calculateNewIteration();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		callback.sendPartialComponent(newCells);
	}
	
	private void calculateNewIteration() throws Exception {
        initializeCalculators();
        startThreads();
        newCells = joinThreads();
    }
	
	private void initializeCalculators() throws RuntimeException, InterruptedException {
        int numThreads = numThreads();
        calculators = new NextCellCalculator[numThreads];
        //int numCellsPerThread = oldCells.length / numThreads;
        List<AtomicReference[]> listOfSubSets = findSubSetsOfCellsForThread(numThreads);

        for (int i = 0; i < numThreads; i++) {
            AtomicReference[] cells = listOfSubSets.get(i);
            NextCellCalculator calculator = new NextCellCalculator(cells, oldGrid);
            calculators[i] = calculator;
        }
    }


    private ArrayList<Cell> joinThreads() throws InterruptedException {
    	ArrayList<Cell> partialComponent = new ArrayList<Cell>();
        for (NextCellCalculator calculator : calculators) {
            calculator.join();
            for(Cell c : calculator.getNextCells()) {
            	partialComponent.add(c);
            }
        }
        return partialComponent;
    }

    private void startThreads() {
        for (Thread calculator : calculators) {
            calculator.start();
        }
    }

    

    /**
     * Finds subsets of cells from grid depending on how many cells per set
     *
     * @param numThreads How many cells you want per array
     * @return List of arrays that contains an array of cells
     */
    private List<AtomicReference[]> findSubSetsOfCellsForThread(int numThreads) {
    	int size = oldCells.length;
        int numCellsPerThread = size / numThreads;
        List<AtomicReference[]> list = new ArrayList<>();

        for (int i = 0; i < size; i += numCellsPerThread) {
            AtomicReference[] subSet;
            if (list.size() + 1 == numThreads) {
            	subSet = new AtomicReference[size-i];
                System.arraycopy(oldCells, i, subSet, 0, size-i);
                list.add(subSet);
                break;
            } else {
            	subSet = new AtomicReference[numCellsPerThread];
            	System.arraycopy(oldCells, i, subSet, 0, numCellsPerThread);
                list.add(subSet);
            }
        }
        return list;
    }

    private int numThreads() throws RuntimeException {
        int cores = Runtime.getRuntime().availableProcessors();
        if (cores < 1) {
            throw new RuntimeException("Number of processors is less than 1");
        }
        return cores;
    }
}
