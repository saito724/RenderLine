package com.e.saito.myliblary.debug.java;

/**
 * Created by e.saito on 2014/06/06.
 */
public class ClassDebug {

    /*
    WARNING Generating caller location information is extremely slow and should be avoided unless execution speed is not an issue.
     */
    public static String getClassName(){
       return   Thread.currentThread().getStackTrace()[0].getClassName();
    }

    public static String getMethodName(){
        return   Thread.currentThread().getStackTrace()[0].getMethodName();
    }

    public static String getFileName(){
        return   Thread.currentThread().getStackTrace()[0].getFileName();
    }

    public static int getLineNumber(){
        return   Thread.currentThread().getStackTrace()[0].getLineNumber();
    }
}
