package backend;

import java.io.Serializable;

public abstract class Coordinate implements Serializable {
	private static final long serialVersionUID = 5561271119560005013L;
	
	public int x;
	public int y;
	
	public boolean equals(Object o) {
		if(o instanceof QuadTreeElement) {
			QuadTreeElement qte = (QuadTreeElement) o;
			return equals(qte);
		}
		else return false;
	}
	public boolean equals(QuadTreeElement e) {
		return x == e.x && y == e.y;
	}
	public int hashCode() {
		return x ^ y;
	}
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
