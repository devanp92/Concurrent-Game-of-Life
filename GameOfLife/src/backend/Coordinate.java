package backend;

import fallback.QuadTreeElement;

import java.io.Serializable;

public abstract class Coordinate implements Serializable {
	private static final long serialVersionUID = 5561271119560005013L;
	
	public int x;
	public int y;
	
	public boolean equals(Object o) {
		if(o instanceof Coordinate) {
			Coordinate c = (Coordinate) o;
			return equals(c);
		}
		else return false;
	}
	public boolean equals(Coordinate e) {
		return x == e.x && y == e.y;
	}
	public int hashCode() {
		return x ^ y;
	}
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
