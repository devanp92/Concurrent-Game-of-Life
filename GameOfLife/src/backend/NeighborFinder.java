package backend;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by devan on 4/5/14.
 */

public class NeighborFinder extends CoordinateCalculator {

    private int CENTER_LEFT_NEIGHBOR;
    private int CENTER_RIGHT_NEIGHBOR;

    private int DOWN_LEFT_NEIGHBOR;
    private int DOWN_RIGHT_NEIGHBOR;
    private int DOWN_CENTER_NEIGHBOR;

    private int UP_LEFT_NEIGHBOR;
    private int UP_RIGHT_NEIGHBOR;
    private int UP_CENTER_NEIGHBOR;

    private Cell cell;
    private AtomicReference[] grid = Grid.getGrid();

    public NeighborFinder(Cell cell) {
        this.cell = cell;
        initializeCellsNeighborPositions();
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
            case MIDDLE:
                neighbors = findNeighborForMiddleCells();
                break;
        }
        return neighbors;
    }

    private List<Cell> findNeighborForTopLeftCornerCell() {
        List<Cell> list = new ArrayList<Cell>();

        Cell rightNeighbor = (Cell) grid[CENTER_RIGHT_NEIGHBOR].get();
        Cell bottomNeighbor = (Cell) grid[DOWN_RIGHT_NEIGHBOR].get();
        Cell diagonalNeighbor = (Cell) grid[DOWN_RIGHT_NEIGHBOR].get();

        list.add(rightNeighbor);
        list.add(bottomNeighbor);
        list.add(diagonalNeighbor);

        return list;
    }

    private List<Cell> findNeighborForTopRightCornerCell() {
        List<Cell> list = new ArrayList<Cell>();

        Cell leftNeighbor = (Cell) grid[CENTER_LEFT_NEIGHBOR].get();
        Cell bottomNeighbor = (Cell) grid[DOWN_CENTER_NEIGHBOR].get();
        Cell diagonalNeighbor = (Cell) grid[DOWN_LEFT_NEIGHBOR].get();

        list.add(leftNeighbor);
        list.add(bottomNeighbor);
        list.add(diagonalNeighbor);

        return list;
    }

    private List<Cell> findNeighborForBottomLeftCornerCell() {
        List<Cell> list = new ArrayList<Cell>();

        Cell topNeighbor = (Cell) grid[UP_CENTER_NEIGHBOR].get();
        Cell diagonalNeighbor = (Cell) grid[UP_RIGHT_NEIGHBOR].get();
        Cell rightNeighbor = (Cell) grid[CENTER_RIGHT_NEIGHBOR].get();


        list.add(topNeighbor);
        list.add(rightNeighbor);
        list.add(diagonalNeighbor);

        return list;
    }

    private List<Cell> findNeighborForBottomRightCornerCell() {
        List<Cell> list = new ArrayList<Cell>();

        Cell leftNeighbor = (Cell) grid[CENTER_LEFT_NEIGHBOR].get();
        Cell diagonalNeighbor = (Cell) grid[UP_LEFT_NEIGHBOR].get();
        Cell topNeighbor = (Cell) grid[UP_CENTER_NEIGHBOR].get();


        list.add(topNeighbor);
        list.add(leftNeighbor);
        list.add(diagonalNeighbor);

        return list;
    }

    private List<Cell> findNeighborForTopBorderCell() {
        List<Cell> list = new ArrayList<Cell>();

        Cell center_left_neighbor = (Cell) grid[CENTER_LEFT_NEIGHBOR].get();
        Cell center_right_neighbor = (Cell) grid[CENTER_RIGHT_NEIGHBOR].get();
        Cell down_left_neighbor = (Cell) grid[DOWN_LEFT_NEIGHBOR].get();
        Cell down_right_neighbor = (Cell) grid[DOWN_RIGHT_NEIGHBOR].get();
        Cell down_center_neighbor = (Cell) grid[DOWN_CENTER_NEIGHBOR].get();

        list.add(center_left_neighbor);
        list.add(center_right_neighbor);
        list.add(down_left_neighbor);
        list.add(down_right_neighbor);
        list.add(down_center_neighbor);

        return list;
    }

    private List<Cell> findNeighborForRightBorderCell() {
        List<Cell> list = new ArrayList<Cell>();

        Cell center_left_neighbor = (Cell) grid[CENTER_LEFT_NEIGHBOR].get();
        Cell down_left_neighbor = (Cell) grid[DOWN_LEFT_NEIGHBOR].get();
        Cell down_center_neighbor = (Cell) grid[DOWN_CENTER_NEIGHBOR].get();
        Cell up_left_neighbor = (Cell) grid[UP_LEFT_NEIGHBOR].get();
        Cell up_center_neighbor = (Cell) grid[UP_CENTER_NEIGHBOR].get();

        list.add(center_left_neighbor);
        list.add(down_left_neighbor);
        list.add(down_center_neighbor);
        list.add(up_left_neighbor);
        list.add(up_center_neighbor);

        return list;
    }

    private List<Cell> findNeighborForLeftBorderCell() {
        List<Cell> list = new ArrayList<Cell>();

        Cell center_right_neighbor = (Cell) grid[CENTER_RIGHT_NEIGHBOR].get();
        Cell down_right_neighbor = (Cell) grid[DOWN_RIGHT_NEIGHBOR].get();
        Cell down_center_neighbor = (Cell) grid[DOWN_CENTER_NEIGHBOR].get();
        Cell up_right_neighbor = (Cell) grid[UP_RIGHT_NEIGHBOR].get();
        Cell up_center_neighbor = (Cell) grid[UP_CENTER_NEIGHBOR].get();

        list.add(center_right_neighbor);
        list.add(down_right_neighbor);
        list.add(down_center_neighbor);
        list.add(up_right_neighbor);
        list.add(up_center_neighbor);

        return list;
    }

    private List<Cell> findNeighborForBottomBorderCell() {
        List<Cell> list = new ArrayList<Cell>();

        Cell center_left_neighbor = (Cell) grid[CENTER_LEFT_NEIGHBOR].get();
        Cell center_right_neighbor = (Cell) grid[CENTER_RIGHT_NEIGHBOR].get();
        Cell up_left_neighbor = (Cell) grid[UP_LEFT_NEIGHBOR].get();
        Cell up_right_neighbor = (Cell) grid[UP_RIGHT_NEIGHBOR].get();
        Cell up_center_neighbor = (Cell) grid[UP_CENTER_NEIGHBOR].get();

        list.add(center_left_neighbor);
        list.add(center_right_neighbor);
        list.add(up_left_neighbor);
        list.add(up_right_neighbor);
        list.add(up_center_neighbor);

        return list;
    }

    private List<Cell> findNeighborForMiddleCells() {
        List<Cell> list = new ArrayList<Cell>();

        Cell center_left_neighbor = (Cell) grid[CENTER_LEFT_NEIGHBOR].get();
        Cell center_right_neighbor = (Cell) grid[CENTER_RIGHT_NEIGHBOR].get();
        Cell down_left_neighbor = (Cell) grid[DOWN_LEFT_NEIGHBOR].get();
        Cell down_right_neighbor = (Cell) grid[DOWN_RIGHT_NEIGHBOR].get();
        Cell down_center_neighbor = (Cell) grid[DOWN_CENTER_NEIGHBOR].get();
        Cell up_left_neighbor = (Cell) grid[UP_LEFT_NEIGHBOR].get();
        Cell up_right_neighbor = (Cell) grid[UP_RIGHT_NEIGHBOR].get();
        Cell up_center_neighbor = (Cell) grid[UP_CENTER_NEIGHBOR].get();

        list.add(center_left_neighbor);
        list.add(center_right_neighbor);
        list.add(down_left_neighbor);
        list.add(down_right_neighbor);
        list.add(down_center_neighbor);
        list.add(up_left_neighbor);
        list.add(up_right_neighbor);
        list.add(up_center_neighbor);

        return list;
    }

    public void initializeCellsNeighborPositions() {
        int CELL_POSITION = super.convert2DCoordinateTo1D(cell.x, cell.y);

        CENTER_LEFT_NEIGHBOR = CELL_POSITION - 1;
        CENTER_RIGHT_NEIGHBOR = CELL_POSITION + 1;

        DOWN_LEFT_NEIGHBOR = CELL_POSITION + numRows - 1;
        DOWN_RIGHT_NEIGHBOR = CELL_POSITION + numRows + 1;
        DOWN_CENTER_NEIGHBOR = CELL_POSITION + numRows;

        UP_CENTER_NEIGHBOR = CELL_POSITION - numRows;
        UP_LEFT_NEIGHBOR = CELL_POSITION - numRows - 1;
        UP_RIGHT_NEIGHBOR = CELL_POSITION - numRows + 1;

    }
}
