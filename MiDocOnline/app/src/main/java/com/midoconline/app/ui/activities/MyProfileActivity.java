package com.midoconline.app.ui.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.midoconline.app.R;
import com.midoconline.app.Util.NetworkManager;
import com.midoconline.app.Util.SharePreferences;
import com.midoconline.app.Util.StringUtils;
import com.midoconline.app.Util.Utils;
import com.midoconline.app.Util.widget.CircularNetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyProfileActivity extends AppCompatActivity {
    private static final String TAG = MyProfileActivity.class.getName();
    private SharePreferences mSharePreferences;
    CircularNetworkImageView mUserImage;
    private EditText mEdtEmail,mEdtSpeciality, mEdtMobile;
    private TextView mTxtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        mTxtName = (TextView) findViewById(R.id.txt_name);
        mEdtEmail = (EditText) findViewById(R.id.edt_email);
        mEdtSpeciality = (EditText) findViewById(R.id.edt_speciality);
        mEdtMobile = (EditText) findViewById(R.id.edt_phone);
        setToolbar();
        mSharePreferences = new SharePreferences(this);
        mUserImage = (CircularNetworkImageView) findViewById(R.id.user_image);
        mUserImage.setDefaultImageResId(R.drawable.profile);

        if (NetworkManager.isConnectedToInternet(this)){
            ExecutePostRequest();
        }else {
            Utils.ShowDialog("Please check your net connection!",this);
        }

    }

    private void setView() {
        mTxtName.setText(mSharePreferences.getUserName());
        mEdtEmail.setText(mSharePreferences.getUserEmail());
        mEdtSpeciality.setText(mSharePreferences.getUserSpecialistLicence());
        mEdtMobile.setText(mSharePreferences.getUserName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ImageView nav_drawer = (ImageView) findViewById(R.id.nav_drawer);
        nav_drawer.setImageResource(R.drawable.ic_back);
        nav_drawer.setColorFilter(getResources().getColor(R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY);
        nav_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void ExecutePostRequest(){
        String url = "http://52.74.206.181:8010/user_profile?key="+mSharePreferences.getKey()+"&authentication="+mSharePreferences.getAuthenticationToken()+"&id="+mSharePreferences.getUserId();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringrequest = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utils.closeProgress();
                Log.d(TAG, response);
                ParseReponse(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.closeProgress();
                Log.d(TAG,""+ error.toString());
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("key",mSharePreferences.getKey());
                params.put("authentication",mSharePreferences.getAuthenticationToken());
                params.put("id", mSharePreferences.getUserId());

                return params;
            }

            @Override
            public Map<String, String> getHeaders()  {
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }
        };
        queue.add(stringrequest);
    }

    private void ParseReponse(String response) {
        Utils.closeProgress();
        Log.d(TAG, response);
        if (StringUtils.isNotEmpty(response)){
            try {
                JSONObject obj = new JSONObject(response);
                String status = obj.getString("status");
                if (status.equalsIgnoreCase("Success")) {
                    mSharePreferences.setUserName(obj.getString("full_name"));
                    mSharePreferences.setUserSpecialistLicence(obj.getString("specility"));
                    mSharePreferences.setUserEmail(obj.getString("email"));
                    mSharePreferences.setMobile(obj.getString("phone_no"));
                    mSharePreferences.setUserThumbnail(obj.getString("user_image_url"));
                    setView();

                }else if(status.equalsIgnoreCase("Failure")){
                  Utils.closeProgress();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else {
            Utils.closeProgress();
        }
    }
}
