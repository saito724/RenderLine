package com.e.saito.renderline.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by e.saito on 2014/06/06.
 */
public class DrawViewFree extends View {
    private Path tmpPath;
    private Paint mPaint;

    public DrawViewFree(Context context) {
        super(context);
        init();
    }

    public DrawViewFree(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public DrawViewFree(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        Log.d("init","init");
        tmpPath = new Path();
        mPaint = getLinePaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!tmpPath.isEmpty()){
            canvas.drawPath(tmpPath,mPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        Log.d(getClass().getSimpleName(),"onTouchEvent");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                tmpPath.moveTo(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                tmpPath.lineTo(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                tmpPath.reset();
                invalidate();
                break;
            default:
                Log.d("onTouchEvent", "message");
        }
        return true;
    }

    private Paint getLinePaint(){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.LTGRAY);
        paint.setStyle(Paint.Style.STROKE); //塗りつぶさない　（FULL→塗りつぶす）
        paint.setStrokeWidth(6);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        return  paint;
    }
}
