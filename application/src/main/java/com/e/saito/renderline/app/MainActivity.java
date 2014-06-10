package com.e.saito.renderline.app;

import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.e.saito.renderline.data.Result;
import com.e.saito.renderline.view.DrawView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class MainActivity extends BaseActivity {
    private DrawView mDrawView;
    private LinearLayout mBrnAera;
    private int mLineNum = 5; //後で設定できるようにする
    private ArrayList<Result> mResults;
    private boolean mIsAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResults = new ArrayList<Result>(mLineNum);

        for (int i = 0; i < mLineNum ; i++) {
            //仮
            mResults.add(new Result("結果" + i));
        }

        mDrawView = (DrawView)findViewById(R.id.drawView);
        try {
            mDrawView.setting(mLineNum);
        }catch (IllegalArgumentException e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        mIsAnimation = false;
        mDrawView.setMode(DrawView.MODE_DRAW);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mBrnAera = (LinearLayout)findViewById(R.id.btn_area);
        int width =  mDrawView.getWidth();
        int size = (int)(width/mLineNum * 0.9);

        for (int i = 0; i < mLineNum ; i++) {
            ImageButton btn = (ImageButton)getLayoutInflater().inflate(R.layout.btn_img,null);
            ViewGroup.LayoutParams params  = new ViewGroup.LayoutParams(size,size);
            int drawId = getResources().getIdentifier("number3_" + (i+1),"drawable",getPackageName());
            btn.setBackgroundResource(drawId);
            btn.setTag(new Integer(i));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawView.setMode(DrawView.MODE_ANIMATION);

                    int i = (Integer)v.getTag();
                    Log.d("onClickstart", i+"");
                    startAmida(i);
                }
            });

            mBrnAera.addView(btn,params);

        }


    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void startAmida(int i){
        if(mIsAnimation){
            return;
        }
        final Result result = mDrawView.setResult(i,mResults.get(i));

        mDrawView.drawAmidaAnime(result.path,new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d("drawAmidaAnime","onAnimationStart");
                mIsAnimation = true;

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("drawAmidaAnime","onAnimationEnd");
               Toast.makeText(MainActivity.this,result.message,Toast.LENGTH_SHORT).show();
                mIsAnimation = false;

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
