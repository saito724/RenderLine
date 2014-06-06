package com.e.saito.renderline.util;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by e.saito on 2014/06/06.
 */
public class Lender {

    public static void DrawLinePath(Paint paint, Canvas canvas ,ArrayList<Point> points) {
        if(points.size() < 1){
            return;
        }
        Path path = new Path();
        path.moveTo(points.get(0).x, points.get(0).y);

        for (int i = 1; i < points.size(); i++) {
            path.lineTo(points.get(i).x, points.get(i).y);
        }
        canvas.drawPath(path,paint);
    }




}
