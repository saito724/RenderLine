package com.e.saito.renderline.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.coolands.open.PathAnimation;
import com.e.saito.renderline.app.R;
import com.e.saito.renderline.data.VerticalLine;

import java.util.ArrayList;

/**
 * Created by e.saito on 2014/06/06.
 */
public class DrawView extends View {
    private static final int MODE_DRAW = 1;
    private static final int MODE_ANIMATION = MODE_DRAW + 1;
    private int mMode;

    private Context mContext;

    private final float mMarginRate = 10.0f;
    private final float mWithinRangeRate = 0.2f;
    private float mWithinRange;
    private float mIntervalX;

    //private float mMarginX;
    private float mMarginY;

    private int mBaseLineNum = 5;
    private int mBaseColor;
    Matrix mMatrix;


    private ArrayList<Path> mMainPath;
    private LinePoints mTmpLine;
    private FloatPoint mTmpStart;
    private FloatPoint mTmpEnd;
    private Paint mPaint;

    private ArrayList<VerticalLine> mVerticalLines;
    private ArrayList<LinePoints> mHorizontalLines;


    public DrawView(Context context) {
        super(context);
        init(context);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        Log.d("init", "init");
        mContext = context;
        mPaint = getLinePaint();
        mMainPath = new ArrayList<Path>();
        mMatrix = new Matrix();
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
        mIntervalX = w / (mBaseLineNum + 1);
        mWithinRange = mWithinRangeRate * mIntervalX;

        mVerticalLines = new ArrayList<VerticalLine>(mBaseLineNum);
        for (int i = 0; i < mBaseLineNum; i++) {
            float posX = mIntervalX * (i + 1);
            VerticalLine line = new VerticalLine(i, posX, mMarginY, h - mMarginY);
            mVerticalLines.add(line);
            Path path = new Path();
            path.moveTo(posX, mMarginY);
            path.lineTo(posX, h - mMarginY);
            Log.d("posX", posX + "");
            mMainPath.add(path);
        }

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        for (VerticalLine line : mVerticalLines) {
//            mPaint.setColor(mBaseColor);
//            canvas.drawLine(line.x, line.startY, line.x, line.endY, mPaint);
//        }
        if (!mMainPath.isEmpty()) {
            mPaint.setColor(Color.GREEN);
            for (Path path : mMainPath) {
                canvas.drawPath(path, mPaint);
            }
        }


        if (mTmpStart != null && mTmpEnd != null) {
            mPaint.setColor(Color.RED);
            canvas.drawLine(mTmpStart.x, mTmpStart.y, mTmpEnd.x, mTmpEnd.y, mPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        Log.d(getClass().getSimpleName(), "onTouchEvent");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (VerticalLine line : mVerticalLines) {
                    if (line.isWithinLine(eventX, mWithinRange)) {
                        mTmpStart = new FloatPoint(line.x, eventY);
                        break;
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                mTmpEnd = new FloatPoint(eventX, eventY);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (mTmpStart != null) {
                    for (VerticalLine line : mVerticalLines) {
                        if (line.isWithinLine(eventX, mWithinRange)) {
                            if (line.x == mTmpStart.x) {
                                continue;
                            }
                            if (line.isWithinLine(eventX, mWithinRange)) {
                                float distX  = Math.abs(mTmpStart.x - line.x);
                                int num = (int)(distX / mIntervalX);
                                float distY  = Math.abs(mTmpStart.y - eventY);
                                for (int i = 0; i < num; i++) {
                                    Log.d("","");
                                }


                                Path path = new Path();
                                path.moveTo(mTmpStart.x, mTmpStart.y);
                                path.lineTo(line.x, eventY);
                                mMainPath.add(path);
                                break;
                            }
                        }
                    }
                }
                mTmpStart = null;
                mTmpEnd = null;
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


    private void DrawAmidaAnime(Path path) {
        View view = new View(mContext);
        view.setLayoutParams(new ViewGroup.LayoutParams(6, 6));
        view.setBackgroundResource(mContext.getResources().getColor(R.color.Red));
        PathAnimation animation = new PathAnimation(path);
        animation.setRepeatCount(1);
        animation.setDuration(1000);
        view.startAnimation(animation);
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
