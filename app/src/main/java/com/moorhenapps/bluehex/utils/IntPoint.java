package com.moorhenapps.bluehex.utils;

public class IntPoint {
    public int x;
    public int y;

    public IntPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public IntPoint set(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }
}
