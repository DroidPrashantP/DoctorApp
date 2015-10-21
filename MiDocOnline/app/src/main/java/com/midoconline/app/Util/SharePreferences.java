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

    public void setAuthenticationToken(String auth_token) {
        mSharedPreferences.edit().putString(AUTH_TOKEN, auth_token).commit();
    }
    public String getAuthenticationToken() {
        return mSharedPreferences.getString(AUTH_TOKEN, "");
    }

    public void setKey(String key) {
        mSharedPreferences.edit().putString(KEY, key).commit();
    }
    public String getKey() {
        return mSharedPreferences.getString(KEY, "");
    }

    public void setSecretKey(String key) {
        mSharedPreferences.edit().putString(SECRET, key).commit();
    }
    public String getSecretKey() {
        return mSharedPreferences.getString(SECRET, "");
    }

    public void setCity(String City) {
        mSharedPreferences.edit().putString(CITY, City).commit();
    }
    public String getCity() {
        return mSharedPreferences.getString(CITY, "");
    }

    public void setCountry(String Country) {
        mSharedPreferences.edit().putString(COUNTRY, Country).commit();
    }
    public String getCountry() {
        return mSharedPreferences.getString(COUNTRY, "");
    }

    public void setMobile(String Mobile) {
        mSharedPreferences.edit().putString(MOBILE, Mobile).commit();
    }
    public String getMobile() {
        return mSharedPreferences.getString(MOBILE, "");
    }

    public void setUserThumbnail(String path) {
        mSharedPreferences.edit().putString(IMAGEPATH, path).commit();
    }
    public String getUserThumbnail() {
        return mSharedPreferences.getString(IMAGEPATH, "");
    }

}
