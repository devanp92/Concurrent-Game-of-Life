package server;

import backend.Cell;

public interface UICallback {
	public void updateGame();
	public void updateCell(Cell c);
}
