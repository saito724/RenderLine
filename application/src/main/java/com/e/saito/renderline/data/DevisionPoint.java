package com.e.saito.renderline.data;

/**
 * Created by e.saito on 2014/06/06.
 */
public class DevisionPoint {
    public FloatPoint endPoint1;
    public FloatPoint endPoint2;

    public DevisionPoint(FloatPoint endPoint1, FloatPoint endPoint2){
        this.endPoint1 = endPoint1;
        this.endPoint2 = endPoint2;

    }

    public FloatPoint getOther(FloatPoint start){
        if(start.equals(endPoint1)){
            return endPoint2;
        }
        return  endPoint1;
    }


}
