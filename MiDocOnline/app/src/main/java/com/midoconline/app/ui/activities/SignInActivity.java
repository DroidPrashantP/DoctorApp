package com.midoconline.app.ui.activities;

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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.midoconline.app.R;
import com.midoconline.app.Util.SharePreferences;
import com.midoconline.app.Util.StringUtils;
import com.midoconline.app.Util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SignUpActivity.class.getName();
    private EditText mEdtEmail,mEdtPassword;
    private Button mSignInBtn;
    private TextInputLayout mEmailTextInput, mPasswordTextInput;
    private SharePreferences mSharePreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mSharePreferences = new SharePreferences(this);
        IntiView();
        setToolbar();
    }

    private void IntiView() {
        mEdtEmail = (EditText) findViewById(R.id.edt_email);
        mEdtPassword = (EditText) findViewById(R.id.edt_password);
        mEmailTextInput = (TextInputLayout) findViewById(R.id.til_email);
        mPasswordTextInput = (TextInputLayout) findViewById(R.id.til_password);
        mSignInBtn = (Button) findViewById(R.id.btn_sign_in);
        mSignInBtn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == mSignInBtn){
            validation();
        }
    }

    private void validation(){
        if (StringUtils.isNotEmpty(mEdtEmail.getText().toString()) && StringUtils.isNotEmpty(mEdtPassword.getText().toString())){
            Toast.makeText(SignInActivity.this,"Sign in",Toast.LENGTH_LONG).show();
            ExecutePostRequestForKey();
        }else {
            if (!StringUtils.isNotEmpty(mEdtEmail.getText().toString())){
                mEmailTextInput.setError(getString(R.string.email_error));
            }
            if (!StringUtils.isNotEmpty(mEdtPassword.getText().toString())){
                mPasswordTextInput.setError(getString(R.string.password_error));
            }
        }

    }

    public void ExecutePostRequestForKey(){
        Utils.showProgress(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringrequest = new StringRequest(Request.Method.POST,"http://52.74.206.181:8010/tokens/get_key", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (StringUtils.isNotEmpty(response)){
                    Log.d(TAG, response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        String secret_key = obj.getString("secret_key");
                        String key = obj.getString("key");
                        mSharePreferences.setKey(secret_key);
                        mSharePreferences.setSecretKey(key);
                        ExecutePostRequestForLogin(secret_key,key);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.closeProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("email",mEdtEmail.getText().toString().trim());
                params.put("password", mEdtPassword.getText().toString().trim());

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
    public void ExecutePostRequestForLogin(final String secret_key, final String key){

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringrequest = new StringRequest(Request.Method.POST,"http://52.74.206.181:8010/tokens.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utils.closeProgress();
                Log.d(TAG, response);
                if (StringUtils.isNotEmpty(response)){
                    try {
                        JSONObject obj = new JSONObject(response);
                        String status = obj.getString("status");
                        if (status.equalsIgnoreCase("Success")) {
                            mSharePreferences.setLoggedIn(true);
                            mSharePreferences.setUserId(obj.getString("user_id"));
                            mSharePreferences.setAuthenticationToken(obj.getString("authentication_token"));
                            mSharePreferences.setUserEmail(mEdtEmail.getText().toString().trim());
                            Intent intent = new Intent(SignInActivity.this, AnswerEmeregencyCallScreen.class);
                            startActivity(intent);
                            finish();

                        }else if(status.equalsIgnoreCase("Failure")){
                            String message = obj.getString("message");
                            Utils.ShowDialog(message,SignInActivity.this);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.ShowDialog("Please Check your Email or Password",SignInActivity.this);
                Utils.closeProgress();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("secret_key",secret_key);
                params.put("key", key);
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

    public void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
    }
}
