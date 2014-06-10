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
import android.view.animation.Animation;

import com.coolands.open.PathAnimation;
import com.e.saito.renderline.data.LineEnd;
import com.e.saito.renderline.data.Result;
import com.e.saito.renderline.data.VerticalLine;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by e.saito on 2014/06/06.
 */
public class DrawView extends View {
    public static final int MODE_DRAW = 1;
    public static final int MODE_ANIMATION = MODE_DRAW + 1;
    private int mMode;

    private Context mContext;

    private final float mMarginRate = 10.0f;
    private final float mWithinRangeRate = 0.2f;
    private float mWithinRange;
    private float mIntervalX;

    //private float mMarginX;
  //  private float mMarginY;


    private int mBaseLineNum = 5;
    private int mBaseColor;
    Matrix mMatrix;
    private int mHeight;
    private int mWidth;

    private ArrayList<Path> mMainPath;
    private LinePoints mTmpLine;
    private FloatPoint mTmpStart;
    private int mTmpStartIndex;
    private FloatPoint mTmpEnd;
    private Paint mPaint;

    private ArrayList<VerticalLine> mVerticalLines;
  //  private ArrayList<LinePoints> mHorizontalLines;


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

    //とりあえず本数だけ
    public void setting(int baseLineNum) throws IllegalArgumentException {
        if (baseLineNum < 2 || baseLineNum > 10) {
            throw new IllegalArgumentException("line number must be between 2 and 10");
        }

        mBaseLineNum = baseLineNum;
    }

    public void setMode(int mode){
        mMode = mode;
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w == oldw && h == oldh) {
            return; // nothing to do
        }
        mHeight = h;
        mWidth = w;
        initBase();

    }

    private void initBase(){
        mMainPath.clear();
        //  mMarginX = w / mMarginRate;
        mIntervalX = mWidth / (mBaseLineNum + 1);
        mWithinRange = mWithinRangeRate * mIntervalX;
        mVerticalLines = new ArrayList<VerticalLine>(mBaseLineNum);
        for (int i = 0; i < mBaseLineNum; i++) {
            float posX = mIntervalX * (i + 1);
            VerticalLine line = new VerticalLine(i, posX, 0, mHeight);
            mVerticalLines.add(line);
            Path path = new Path();
            path.moveTo(posX, 0);
            path.lineTo(posX, mHeight);
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
        if(mMode != MODE_DRAW){
            return false;
        }
        float eventX = event.getX();
        float eventY = event.getY();
        Log.d(getClass().getSimpleName(), "onTouchEvent");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (VerticalLine line : mVerticalLines) {
                    if (line.isWithinLine(eventX, mWithinRange)) {
                        if(line.devisions.get(new Float(eventY)) == null){
                            mTmpStart = new FloatPoint(line.x, eventY);
                            mTmpStartIndex = line.index;
                        }
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
                            if (line.index == mTmpStartIndex) {
                                continue;
                            }
                            if (line.isWithinLine(eventX, mWithinRange)) {
                                if(line.devisions.get(new Float(eventY)) == null) {
                                    Path path = new Path();
                                    path.moveTo(mTmpStart.x, mTmpStart.y);
                                    mVerticalLines.get(mTmpStartIndex).devisions.put(mTmpStart.y, new LineEnd(line.index, eventY));
                                    mVerticalLines.get(line.index).devisions.put(eventY, new LineEnd(mTmpStartIndex, mTmpStart.y));
                                    path.lineTo(line.x, eventY);
                                    mMainPath.add(path);
                                }
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
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE); //塗りつぶさない　（FULL→塗りつぶす）
        paint.setStrokeWidth(6);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        return paint;
    }

//    public Result setResult(int index, Result result) {
//        Path path = new Path();
//
//        VerticalLine line = mVerticalLines.get(index);
//        float posY = line.startY;
//        path.moveTo(line.x, posY);
//        Map.Entry<Float, LineEnd> nextPoint = line.getNextPoint(posY);
//
//        while (nextPoint != null) {
//            Log.d("line.index","message:"+line.index);
//            LineEnd nextEnd = nextPoint.getValue();
//            path.lineTo(line.x, posY);//同一線上の分岐点まで
//
//            line = mVerticalLines.get(nextEnd.lineIndex); //次の縦線
//            posY = nextEnd.y;
//            path.lineTo(line.x, posY);//次の線の接続点まで
//            nextPoint = line.getNextPoint(posY);
//        }
//        path.lineTo(line.x, line.endY);
//
//        result.setResultPath(line.index,path);
//        Log.d("result.index","message:"+line.index);
//
//        return result;
//    }

    public Result setResult(int index, Result result) {
        ArrayList<Path> pathList = new ArrayList<Path>();
        Path path = new Path();

        VerticalLine line = mVerticalLines.get(index);
        float posY = line.startY;
        path.moveTo(line.x, posY);
        Map.Entry<Float, LineEnd> nextPoint = line.getNextPoint(posY);

        while (nextPoint != null) {
            Log.d("line.index", "message:" + line.index);
            LineEnd nextEnd = nextPoint.getValue();
            path.lineTo(line.x, posY);//同一線上の分岐点まで

            line = mVerticalLines.get(nextEnd.lineIndex); //次の縦線
            posY = nextEnd.y;
            path.lineTo(line.x, posY);//次の線の接続点まで
            nextPoint = line.getNextPoint(posY);
        }
        path.lineTo(line.x, line.endY);
        pathList.add(path);
        result.setResultPath(line.index, path);
        Log.d("result.index", "message:" + line.index);

        return result;
    }

//    public void drawAmidaAnime(Path path , Animation.AnimationListener listener) {
//        View view = new View(mContext);
//        view.setLayoutParams(new ViewGroup.LayoutParams(6, 6));
//        view.setBackgroundColor(Color.MAGENTA);
//        Log.d("drawAmidaAnime",view.getWidth()+"");
//        PathAnimation animation = new PathAnimation(path);
//        animation.setRepeatCount(1);
//        animation.setDuration(3000);
//        animation.setAnimationListener(listener);
//        view.startAnimation(animation);
//    }

    //Animationはつかわない
    public void drawAmidaAnime(Path path , AmidaListener listener) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {

            }
        };
        View view = new View(mContext);
        view.setLayoutParams(new ViewGroup.LayoutParams(6, 6));
        view.setBackgroundColor(Color.MAGENTA);
        Log.d("drawAmidaAnime",view.getWidth()+"");
        PathAnimation animation = new PathAnimation(path);
        animation.setRepeatCount(1);
        animation.setDuration(3000);
        animation.setAnimationListener(listener);
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

    public interface AmidaListener{
        void onDrawnFinished();
    }

}
