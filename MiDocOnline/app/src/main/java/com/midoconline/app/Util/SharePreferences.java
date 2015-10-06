package com.midoconline.app.Util;

import android.content.Context;

/**
 * Created by Prashant on 6/10/15.
 */
public class SharePreferences implements Constants.Preferences,Constants.UserInfoKeys{
    private static android.content.SharedPreferences mSharedPreferences;

    public SharePreferences(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREFERENCES,
                Context.MODE_PRIVATE);
    }

    public boolean isLoggedIn() {
        return mSharedPreferences.getBoolean(LOGGED_IN, false);
    }

    // Login preferences
    public void setLoggedIn(boolean loggedIn) {
        mSharedPreferences.edit().putBoolean(LOGGED_IN, loggedIn).commit();
    }

    public void setUserId(String userId) {
        mSharedPreferences.edit().putString(ID, userId).commit();
    }
    public String getUserId() {
        return mSharedPreferences.getString(ID, "");
    }

    public void setUserName(String userName) {
        mSharedPreferences.edit().putString(USER_NAME, userName).commit();
    }
    public String getUserName() {
        return mSharedPreferences.getString(USER_NAME, "");
    }

    public void setUserEmail(String userEmail) {
        mSharedPreferences.edit().putString(EMAIL, userEmail).commit();
    }
    public String getUserEmail() {
        return mSharedPreferences.getString(EMAIL, "");
    }

    public void setUserSpecialistLicence(String userSpecialistLicence) {
        mSharedPreferences.edit().putString(SPECIALIST_LICENCE, userSpecialistLicence).commit();
    }
    public String getUserSpecialistLicence() {
        return mSharedPreferences.getString(SPECIALIST_LICENCE, "");
    }

}
