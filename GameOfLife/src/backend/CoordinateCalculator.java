package backend;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by devan on 4/5/14.
 */

/**
 * Converts 2D coordinates and 1D coordinates
 */
public abstract class CoordinateCalculator implements Serializable {
	private static final long serialVersionUID = 1L;
	
	int numRows;

    public int convert2DCoordinateTo1D(int x, int y) {
        return x * numRows + y;
    }

    public HashMap<Character, Integer> convert1DCoordinateTo2D(int i) {
        HashMap<Character, Integer> hashMap = new HashMap<>();
        int row, column;

        if (i == 0) {
            row = 0;
            column = 0;
        } else if (i / numRows == 0) {
            row = 0;
            column = i % numRows;
        } else {
            row = i / numRows;
            column = i % numRows;
        }

        hashMap.put('x', row);
        hashMap.put('y', column);

        return hashMap;
    }
}
