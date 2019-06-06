package com.cs.app;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * author : ChenSen
 * data : 2019/5/27
 * desc:
 */
public class Test {

    static Handler handler = new Handler() {

    };

    Handler handler2 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });


    private static class MyHandler extends Handler {
        WeakReference<Activity> activityWeakReference;

        public MyHandler(WeakReference<Activity> activityWeakReference) {
            this.activityWeakReference = activityWeakReference;
        }


    }

}
