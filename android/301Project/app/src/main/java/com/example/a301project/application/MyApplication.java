package com.example.a301project.application;

import android.app.Application;
import android.content.Context;


public class MyApplication extends Application {

    static Context context;


    public static Context getContext() {
        return context;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();




    }

}
