package com.e.saito.renderline.data;

import android.graphics.Path;

/**
 * Created by e.saito on 2014/06/10.
 */
public class Result {
    public int endIndex;
    public Path path;
    public String message;

    public Result(String message) {
        this.message = message;
    }

    public void setResultPath(int endIndex, Path path){
        this.endIndex = endIndex;
        this.path = path;

    }
}
