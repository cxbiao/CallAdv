package com.bryan.calladv;

import android.app.Application;

import com.bryan.calladv.utils.PhoneLocationUtils;


/**
 * Author：Cxb on 2016/3/17 11:16
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new Runnable() {
            @Override
            public void run() {
               PhoneLocationUtils.copyDB(getApplicationContext());
            }
        }).start();
    }
}
