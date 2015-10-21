package com.midoconline.app.ui.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.midoconline.app.beans.SignUpBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = SignUpActivity.class.getName();
    private EditText mEdtName, mEdtEmail,mEdtPassword,mEdtSpecialityLicence,mEdtCity,mEdtMobile, mEdtCountry, mEdtGender;
    private Button mRequestApproval;
    private TextInputLayout mEmailTextInput, mNameTextInput, mPasswordTextInput, mSpecialityTextInput,mCityTextInput, mMobileTextInput, mCountryTextInput, mGenderTextInput;
    private SignUpBean mSignUpBean;
    private LinearLayout mMainView;
    private SharePreferences mSharePreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mSharePreferences = new SharePreferences(this);
        intiView();
        setToolbar();
    }

    private void intiView() {
        mMainView = (LinearLayout) findViewById(R.id.MainLayoutWrapper);
        mEdtName = (EditText) findViewById(R.id.edt_name);
        mEdtEmail = (EditText) findViewById(R.id.edt_email);
        mEdtPassword = (EditText) findViewById(R.id.edt_password);
        mEdtSpecialityLicence = (EditText) findViewById(R.id.edt_speciality_licence);
        mEdtCity = (EditText) findViewById(R.id.edt_city);
        mEdtMobile = (EditText) findViewById(R.id.edt_mobile);
        mEdtCountry = (EditText) findViewById(R.id.edt_country);
//        mEdtGender = (EditText) findViewById(R.id.edt_birthday);
        mRequestApproval = (Button) findViewById(R.id.btn_request_approval);
        mRequestApproval.setOnClickListener(this);
        mEmailTextInput = (TextInputLayout) findViewById(R.id.til_email);
        mNameTextInput = (TextInputLayout) findViewById(R.id.til_name);
        mPasswordTextInput = (TextInputLayout) findViewById(R.id.til_password);
        mSpecialityTextInput = (TextInputLayout) findViewById(R.id.til_speciality_licence);
        mCityTextInput = (TextInputLayout) findViewById(R.id.til_city);
        mMobileTextInput = (TextInputLayout) findViewById(R.id.til_mobile);
        mCountryTextInput = (TextInputLayout) findViewById(R.id.til_country);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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

    @Override
    public void onClick(View v) {
        if (v == mRequestApproval){
            validation();
        }
    }

    private void validation(){
        if (StringUtils.isNotEmpty(mEdtName.getText().toString())&& StringUtils.isNotEmpty(mEdtEmail.getText().toString()) && StringUtils.isNotEmpty(mEdtPassword.getText().toString())&& StringUtils.isNotEmpty(mEdtSpecialityLicence.getText().toString())&& StringUtils.isNotEmpty(mEdtCity.getText().toString()) && StringUtils.isNotEmpty(mEdtCountry.getText().toString()) && StringUtils.isNotEmpty(mEdtMobile.getText().toString())){
            mSignUpBean = new SignUpBean(mEdtName.getText().toString(),mEdtCity.getText().toString(),mEdtEmail.getText().toString(),mEdtCountry.getText().toString(),mEdtMobile.getText().toString(),mEdtPassword.getText().toString(),mEdtSpecialityLicence.getText().toString());
            if(NetworkManager.isConnectedToInternet(this)) {
                Utils.showProgress(this);
                ExecutePostRequest();
            }else {
                Utils.ShowSnackBar(mMainView,getString(R.string.no_internet_connection));
            }

        }else {
            if (!StringUtils.isNotEmpty(mEdtName.getText().toString())){
                mNameTextInput.setError(getString(R.string.name_error));
            }
            if (!StringUtils.isNotEmpty(mEdtEmail.getText().toString())){
                mEmailTextInput.setError(getString(R.string.email_error));
            }
            if (!StringUtils.isNotEmpty(mEdtPassword.getText().toString())){
                mPasswordTextInput.setError(getString(R.string.password_error));
            }
            if (!StringUtils.isNotEmpty(mEdtSpecialityLicence.getText().toString())){
                mSpecialityTextInput.setError(getString(R.string.speciality_licence_error));
            }
            if (!StringUtils.isNotEmpty(mEdtCity.getText().toString())){
                mCityTextInput.setError(getString(R.string.city_error));
            }
            if (!StringUtils.isNotEmpty(mEdtMobile.getText().toString())){
                mMobileTextInput.setError(getString(R.string.mobile_error));
            }
            if (!StringUtils.isNotEmpty(mEdtCountry.getText().toString())){
                mCountryTextInput.setError(getString(R.string.country_error));
            }

        }

    }

    public void ExecutePostRequest(){

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringrequest = new StringRequest(Request.Method.POST,"http://52.74.206.181:8010/tokens/doctor_sign_up", new Response.Listener<String>() {
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
                params.put("username",mSignUpBean.getName());
                params.put("email",mSignUpBean.getEmail());
                params.put("password", mSignUpBean.getPassword());
                params.put("licence",mSignUpBean.getSpecialize());
                params.put("city",mSignUpBean.getcity());
                params.put("country",mSignUpBean.getcountry());
//                params.put("dob",mSignUpBean.getBirthday());
                params.put("mobile_no",mSignUpBean.getMobilenumber());

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
        if (StringUtils.isNotEmpty(response)){
            try {
                JSONObject obj = new JSONObject(response);
                String status = obj.getString("status");
                if (status.equalsIgnoreCase("Success")){
                    mSharePreferences.setLoggedIn(true);
                    mSharePreferences.setUserEmail(mSignUpBean.getEmail());
                    mSharePreferences.setUserSpecialistLicence(mSignUpBean.getSpecialize());
                    mSharePreferences.setUserName(mSignUpBean.getName());
                    mSharePreferences.setUserId(obj.getString("user_id"));
                    mSharePreferences.setAuthenticationToken(obj.getString("authentication_token"));
                    mSharePreferences.setKey(obj.getString("key"));
                    mSharePreferences.setSecretKey(obj.getString("secret_key"));
                    mSharePreferences.setCity(mSignUpBean.getcity());
                    mSharePreferences.setCountry(mSignUpBean.getcountry());
                    mSharePreferences.setMobile(mSignUpBean.getMobilenumber());

                    Intent intent = new Intent(SignUpActivity.this, AnswerEmeregencyCallScreen.class);
                    startActivity(intent);
                    finish();

                }else if(status.equalsIgnoreCase("Failure")){
                    String message = obj.getString("message");
                    Utils.ShowDialog(message, SignUpActivity.this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ImageView nav_drawer = (ImageView) findViewById(R.id.nav_drawer);
        nav_drawer.setImageResource(R.drawable.ic_back);
        nav_drawer.setColorFilter(getResources().getColor(R.color.blue_dark), android.graphics.PorterDuff.Mode.MULTIPLY);
        nav_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
