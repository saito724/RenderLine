package com.e.saito.renderline.data;

import java.util.Objects;

/**
 * Created by e.saito on 2014/06/06.
 */
public class FloatPoint {
    public float x;
    public float y;

    public FloatPoint(float x, float y){
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || ! (o instanceof FloatPoint)){
            return false;
        }
        if(((FloatPoint) o).x == this.x
                &&((FloatPoint) o).y == this.y){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return new Float(x + y).intValue();
    }
}
