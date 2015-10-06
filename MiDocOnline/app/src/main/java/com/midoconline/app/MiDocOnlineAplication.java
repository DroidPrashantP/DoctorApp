package com.midoconline.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by Prashant on 6/10/15.
 */
public class MiDocOnlineAplication extends Application {
    public static final String TAG = MiDocOnlineAplication.class.getSimpleName();
    private static Context mContext;

    public MiDocOnlineAplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static Context getAppContext() {
        return MiDocOnlineAplication.mContext;
    }

}
