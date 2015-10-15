package com.midoconline.app.ui.activities;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.midoconline.app.R;




public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);

       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
            // This method will be executed once the timer is over
               // Start your app main activity
               Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
               startActivity(i);
               // close this activity
               finish();
           }
       },3000);

    }
}
