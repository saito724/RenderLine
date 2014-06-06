package com.e.saito.renderline.app;

import android.app.Application;
import android.util.Log;

/**
 * Created by e.saito on 2014/06/06.
 */
public class MyApplication extends Application {
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d("MyApplication", "onLowMemory");
    }
}
