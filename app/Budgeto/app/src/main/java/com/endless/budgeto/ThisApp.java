package com.endless.budgeto;

import android.app.Application;
import android.content.Context;

/**
 * Created by Eric on 2016-09-11.
 */
public class ThisApp extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        ThisApp.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return ThisApp.context;
    }
}
