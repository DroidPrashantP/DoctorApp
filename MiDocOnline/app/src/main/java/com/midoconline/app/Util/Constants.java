package com.midoconline.app.Util;

/**
 * Created by Prashant on 6/10/15.
 */
public interface Constants {
    interface Preferences {
        String PREFERENCES = "midoconline_pref";
        String LOGGED_IN = "logged_in";
    }

    interface UserInfoKeys {
        String SECRET    = "secret_key";
        String KEY = "key";
        String AUTH_TOKEN = "authentication_token";
        String USER_NAME = "user_name";
        String ID = "user_id";
        String EMAIL = "email";
        String SPECIALIST_LICENCE = "specialist_licence";
        String CITY = "city";
        String COUNTRY = "country";
        String MOBILE = "mobile";
        String IMAGEPATH = "user_image_url";
    }

    interface BundleKeys {
        String USERTYPE    = "userType";
        String PATIENT = "Patient";
        String DOCTOR = "Doctor";
    }

    interface URL {
        String BASE_URL    = "http://52.74.206.181:8010/";
        String SIGNUP_URL    = BASE_URL +"tokens/doctor_sign_up";
        String SIGNIN_URL    = BASE_URL +"tokens.json";
        String PAYMENT_OPTION_URL    = BASE_URL +"save_card_details";

    }
}
