package com.e.saito.renderline.data;

import android.graphics.Canvas;
import android.graphics.Path;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by e.saito on 2014/06/06.
 */
public class VerticalLine {
    //何番目か
    public int index;
    public float x;
    public VerticalLine(int index, float x, float startY, float endY) {
        this.index = index;
        this.x = x;
        this.startY = startY;
        this.endY = endY;

        yPoints = new TreeSet<DevisionPoint>();
    }


    public Path getBasePath() {
        Path path = new Path();
        path.moveTo(x, startY);
        path.lineTo(x, endY);
        return path;
    }

    public boolean isWithinLine(float x ,float range){
        if(x > this.x -range && x < this.x + range){
            return true;
        }
        return false;
    }

    public float startY;
    public float endY;

    public TreeSet<DevisionPoint> yPoints;
    public TreeMap<Float,endPoint> devisions;
//    /*
//        @param y yの次のポイントを取得する
//     */
//    public FloatPoint getNextPoint(float y){
//        FloatPoint next;
//        Map.Entry<Float,endPoint> point = devisions.higherEntry(y);
//
//    }

    public class endPoint{
        int lineIndex;
        float y;
    }



//    public class DevisionPoint implements Comparable {
//        public float y;
//        public int nextIndex;
//        public float nextY;
//
//
//        public DevisionPoint(float y, int nextIndex, float nextY) {
//            this.y = y;
//            this.nextIndex = nextIndex;
//            this.nextY = nextY;
//        }
//
//        public FloatPoint getOther(FloatPoint start) {
//            if (start.equals(endPoint1)) {
//                return endPoint2;
//            }
//            return endPoint1;
//        }
//
//        @Override
//        public int compareTo(Object another) {
//            float anotherY = ((DevisionPoint) another).y;
//            if (this.y > anotherY) {
//                return 1;
//            } else if (this.y < anotherY) {
//                return -1;
//            }
//            return 0;
//        }
//
//    }

}

