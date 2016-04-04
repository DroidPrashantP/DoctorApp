package com.midoconline.app;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;

/**
 * Created by Prashant on 6/10/15.
 */
public class MiDocOnlineApplication extends Application {
    public static final String TAG = MiDocOnlineApplication.class.getSimpleName();
    private static Context mContext;

    public MiDocOnlineApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        MiDocOnlineApplication.mContext = getApplicationContext();

    }

    public static Context getAppContext() {
        return MiDocOnlineApplication.mContext;
    }

}
