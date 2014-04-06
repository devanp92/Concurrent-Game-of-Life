package backend;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by devan on 4/5/14.
 */
public class NeighborFinder extends CoordinateCalculator {
    Cell cell;
    AtomicReference[] grid = Grid.getGrid();

    public NeighborFinder(Cell cell) {
        this.cell = cell;
    }

    public List<Cell> findNeighbors() {

        List<Cell> neighbors = null;
        CellCase cellCase = cell.getCellCase();

        switch (cellCase) {
            case TOP_LEFT_CORNER:
                neighbors = findNeighborForTopLeftCornerCell();
                break;
            case TOP_RIGHT_CORNER:
                neighbors = findNeighborForTopRightCornerCell();
                break;
            case BOTTOM_LEFT_CORNER:
                neighbors = findNeighborForBottomLeftCornerCell();
                break;
            case BOTTOM_RIGHT_CORNER:
                neighbors = findNeighborForBottomRightCornerCell();
                break;
            case TOP_BORDER:
                neighbors = findNeighborForTopBorderCell();
                break;
            case RIGHT_BORDER:
                neighbors = findNeighborForRightBorderCell();
                break;
            case LEFT_BORDER:
                neighbors = findNeighborForLeftBorderCell();
                break;
            case BOTTOM_BORDER:
                neighbors = findNeighborForBottomBorderCell();
                break;
        }
        return neighbors;
    }

    private List<Cell> findNeighborForTopLeftCornerCell() {
        List<Cell> list = new ArrayList<Cell>();

        Cell rightNeighbor = (Cell) grid[2].get();
        Cell bottomNeighbor = (Cell) grid[numRows].get();

        list.add(rightNeighbor);
        list.add(bottomNeighbor);

        return list;
    }

    private List<Cell> findNeighborForTopRightCornerCell() {
        List<Cell> list = new ArrayList<Cell>();

        Cell leftNeighbor = (Cell) grid[numRows - 2].get();
        Cell bottomNeighbor = (Cell) grid[2 * numRows - 1].get();

        list.add(leftNeighbor );
        list.add(bottomNeighbor);

        return list;
    }

    private List<Cell> findNeighborForBottomLeftCornerCell() {
        List<Cell> list = new ArrayList<Cell>();
//
//        Cell topNeighbor = (Cell) grid[numRows - 2].get();
//        Cell rightNeighbor = (Cell) grid[2 * numRows - 1].get();
//
//        list.add(leftNeighbor );
//        list.add(bottomNeighbor);

        return list;
    }

    private List<Cell> findNeighborForBottomRightCornerCell() {
        List<Cell> list = new ArrayList<Cell>();

        return list;
    }

    private List<Cell> findNeighborForTopBorderCell() {
        List<Cell> list = new ArrayList<Cell>();

        return list;
    }

    private List<Cell> findNeighborForRightBorderCell() {
        List<Cell> list = new ArrayList<Cell>();

        return list;
    }

    private List<Cell> findNeighborForLeftBorderCell() {
        List<Cell> list = new ArrayList<Cell>();

        return list;
    }

    private List<Cell> findNeighborForBottomBorderCell() {
        List<Cell> list = new ArrayList<Cell>();

        return list;
    }
}
