package com.moorhenapps.bluehex.utils;

public class FloatPoint {
    public float x;
    public float y;

    public FloatPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public FloatPoint set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }
}
