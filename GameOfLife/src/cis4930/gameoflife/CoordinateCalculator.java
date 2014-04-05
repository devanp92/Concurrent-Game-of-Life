package cis4930.gameoflife;

import java.util.HashMap;

/**
 * Created by devan on 4/5/14.
 */
public abstract class CoordinateCalculator {
    int numRows;

    protected int convert2DCoordinateTo1D(int x, int y) {
        return x * numRows + y;
    }

    public HashMap<Character, Integer> convert1DCoordinateTo2D(int i) {
        HashMap<Character, Integer> hashMap = new HashMap<Character, Integer>();
        int row, column;

        if (i == 0) {
            row = 0;
            column = 0;
        } else if (i / 10 == 0) {
            row = 0;
            column = i - 1;
        } else {
            row = i / numRows;
            column = i % numRows;
        }

        hashMap.put('x', row);
        hashMap.put('y', column);

        return hashMap;
    }
}
