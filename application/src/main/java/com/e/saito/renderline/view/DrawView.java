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

import java.util.ArrayList;

/**
 * Created by e.saito on 2014/06/06.
 */
public class DrawView extends View {
    private static final int MODE_DRAW = 1;
    private static final int MODE_ANIMATION = MODE_DRAW + 1;
    private int mMode;

    private float mMarginRate = 10.0f;
    //private float mMarginX;
    private float mMarginY;

    private int mBaseLineNum;
    private int mBaseColor;


    private Path tmpPath;
    private FloatPoint mStartPoint;
    private FloatPoint mEndPoint;
    private Paint mPaint;
    private ArrayList<LinePoints> mBaseLineList;


    public DrawView(Context context) {
        super(context);
        init();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        Log.d("init", "init");
        tmpPath = new Path();
        mPaint = getLinePaint();
    }

    public void Setting(int baseLineNum, int baseColor) throws IllegalArgumentException {
        if (baseLineNum < 2 || baseLineNum > 10) {
            throw new IllegalArgumentException("line number must be between 2 and 10");
        }
        mBaseLineNum = baseLineNum;
        mBaseColor = baseColor;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w == oldw && h == oldh) {
            return; // nothing to do
        }
      //  mMarginX = w / mMarginRate;
        mMarginY = h / mMarginRate;
        float intervalX = w / (mBaseLineNum + 1);

        mBaseLineList = new ArrayList<LinePoints>(mBaseLineNum);
        for (int i = 0; i < mBaseLineNum; i++) {
            float posX = intervalX * (i+1);
            LinePoints points = new LinePoints(posX,mMarginY, posX, h - mMarginY);
            mBaseLineList.add(points);
        }

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (LinePoints points : mBaseLineList) {
            mPaint.setColor(mBaseColor);
            canvas.drawLine(points.start.x, points.start.y, points.end.x, points.end.y, mPaint);
        }

        if (!tmpPath.isEmpty()) {
            mPaint.setColor(Color.RED);
            canvas.drawPath(tmpPath, mPaint);
        }

        if (mStartPoint != null && mEndPoint != null) {
            mPaint.setColor(Color.BLUE);
            canvas.drawLine(mStartPoint.x, mStartPoint.y, mEndPoint.x, mEndPoint.y, mPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        Log.d(getClass().getSimpleName(), "onTouchEvent");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartPoint = new FloatPoint(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                mEndPoint = new FloatPoint(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (tmpPath.isEmpty()) {
                    tmpPath.moveTo(x, y);
                } else {
                    tmpPath.lineTo(x, y);
                }
                mEndPoint = null;
                mStartPoint = null;
                invalidate();
                break;
            default:
                Log.d("onTouchEvent", "message");
        }
        return true;
    }

    private Paint getLinePaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mBaseColor);
        paint.setStyle(Paint.Style.STROKE); //塗りつぶさない　（FULL→塗りつぶす）
        paint.setStrokeWidth(6);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        return paint;
    }

    class FloatPoint {
        float x;
        float y;

        FloatPoint(float x, float y) {
            this.x = x;
            this.y = y;
        }

    }

    class LinePoints {
        FloatPoint start;
        FloatPoint end;

        public LinePoints(float startX, float startY, float endX, float endY) {
            this(new FloatPoint(startX, startY), new FloatPoint(endX, endY));

        }

        public LinePoints(FloatPoint start, FloatPoint end) {
            this.start = start;
            this.end = end;

        }
    }
}
