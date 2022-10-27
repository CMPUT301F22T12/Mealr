package com.example.a301project.application;

import android.app.Application;
import android.content.Context;

/**
 * @ClassName: MyApplication
 * @Description: java类作用描述
 * @Author: ping
 * @CreateDate: 2022/10/27 6:08 下午
 */
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
