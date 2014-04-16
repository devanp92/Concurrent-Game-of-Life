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
    private AtomicReference[] grid;

    public NeighborFinder(Grid grid, Cell cell) {
    	this.grid = grid.getGrid();
        if(cell == null){
            throw new NullPointerException("Cell is null");
        }
        this.cell = cell;
        super.numRows = grid.getNumRows();
        initializeCellsNeighborPositions();
    }

    public List<Cell> findNeighbors() {

        List<Cell> neighbors = null;
        CellCase cellCase = cell.getCellCase();

        switch (cellCase) {
            case TOP_LEFT_CORNER:
                neighbors = findNeighborsForTopLeftCornerCell();
                break;
            case TOP_RIGHT_CORNER:
                neighbors = findNeighborsForTopRightCornerCell();
                break;
            case BOTTOM_LEFT_CORNER:
                neighbors = findNeighborsForBottomLeftCornerCell();
                break;
            case BOTTOM_RIGHT_CORNER:
                neighbors = findNeighborsForBottomRightCornerCell();
                break;
            case TOP_BORDER:
                neighbors = findNeighborsForTopBorderCell();
                break;
            case RIGHT_BORDER:
                neighbors = findNeighborsForRightBorderCell();
                break;
            case LEFT_BORDER:
                neighbors = findNeighborsForLeftBorderCell();
                break;
            case BOTTOM_BORDER:
                neighbors = findNeighborsForBottomBorderCell();
                break;
            case MIDDLE:
                neighbors = findNeighborsForMiddleCells();
                break;
        }
        return neighbors;
    }

    private List<Cell> findNeighborsForTopLeftCornerCell() {
        List<Cell> list = new ArrayList<>();

        Cell rightNeighbor = (Cell) grid[CENTER_RIGHT_NEIGHBOR].get();
        Cell bottomNeighbor = (Cell) grid[DOWN_RIGHT_NEIGHBOR].get();
        Cell diagonalNeighbor = (Cell) grid[DOWN_CENTER_NEIGHBOR].get();

        list.add(rightNeighbor);
        list.add(bottomNeighbor);
        list.add(diagonalNeighbor);

        return list;
    }

    private List<Cell> findNeighborsForTopRightCornerCell() {
        List<Cell> list = new ArrayList<>();

        Cell leftNeighbor = (Cell) grid[CENTER_LEFT_NEIGHBOR].get();
        Cell bottomNeighbor = (Cell) grid[DOWN_CENTER_NEIGHBOR].get();
        Cell diagonalNeighbor = (Cell) grid[DOWN_LEFT_NEIGHBOR].get();

        list.add(leftNeighbor);
        list.add(bottomNeighbor);
        list.add(diagonalNeighbor);

        return list;
    }

    private List<Cell> findNeighborsForBottomLeftCornerCell() {
        List<Cell> list = new ArrayList<>();

        Cell topNeighbor = (Cell) grid[UP_CENTER_NEIGHBOR].get();
        Cell diagonalNeighbor = (Cell) grid[UP_RIGHT_NEIGHBOR].get();
        Cell rightNeighbor = (Cell) grid[CENTER_RIGHT_NEIGHBOR].get();


        list.add(topNeighbor);
        list.add(rightNeighbor);
        list.add(diagonalNeighbor);

        return list;
    }

    private List<Cell> findNeighborsForBottomRightCornerCell() {
        List<Cell> list = new ArrayList<>();

        Cell leftNeighbor = (Cell) grid[CENTER_LEFT_NEIGHBOR].get();
        Cell diagonalNeighbor = (Cell) grid[UP_LEFT_NEIGHBOR].get();
        Cell topNeighbor = (Cell) grid[UP_CENTER_NEIGHBOR].get();


        list.add(topNeighbor);
        list.add(leftNeighbor);
        list.add(diagonalNeighbor);

        return list;
    }

    private List<Cell> findNeighborsForTopBorderCell() {
        List<Cell> list = new ArrayList<>();

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

    private List<Cell> findNeighborsForRightBorderCell() {
        List<Cell> list = new ArrayList<>();

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

    private List<Cell> findNeighborsForLeftBorderCell() {
        List<Cell> list = new ArrayList<>();

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

    private List<Cell> findNeighborsForBottomBorderCell() {
        List<Cell> list = new ArrayList<>();

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

    private List<Cell> findNeighborsForMiddleCells() {
        List<Cell> list = new ArrayList<>();

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

    private void initializeCellsNeighborPositions() {
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
