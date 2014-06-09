package com.e.saito.renderline.data;

/**
 * Created by e.saito on 2014/06/06.
 */
public class DevisionPoint implements Comparable {
    public float y;
    public FloatPoint endPoint;


    public DevisionPoint(FloatPoint endPoint1, FloatPoint endPoint2) {
//        this.endPoint1 = endPoint1;
//        this.endPoint2 = endPoint2;

    }

//    public FloatPoint getOther(FloatPoint start) {
//        if (start.equals(endPoint1)) {
//            return endPoint2;
//        }
//        return endPoint1;
//    }

    @Override
    public int compareTo(Object another) {
        float anotherY = ((DevisionPoint) another).y;
        if (this.y > anotherY) {
            return 1;
        } else if (this.y < anotherY) {
            return -1;
        }
        return 0;
    }

}
