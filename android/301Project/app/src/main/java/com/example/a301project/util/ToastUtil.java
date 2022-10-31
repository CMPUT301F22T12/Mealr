/**
 *
 */
package com.example.a301project.util;

import android.widget.Toast;

import com.example.a301project.application.MyApplication;


public class ToastUtil {
    /**
     * 弹出Toast
     */
    public static void show(String info) {
        Toast.makeText(MyApplication.getContext(), info, Toast.LENGTH_LONG).show();
    }

    public static void show(int info) {
        Toast.makeText(MyApplication.getContext(), info, Toast.LENGTH_LONG).show();
    }

}
