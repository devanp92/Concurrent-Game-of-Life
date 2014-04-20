package server;

import backend.Cell;

public interface UICallback {
	public void updateGame();
	public void updateCell(Cell c);
	public void updatePausePlay(NetworkMessage nm);
	public void updateIterationDelay(int delay);
	public void setStatus(String newStatus, String txtFill);
}
