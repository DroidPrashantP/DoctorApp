package com.midoconline.app.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.midoconline.app.R;
import com.midoconline.app.Util.Constants;
import com.midoconline.app.Util.NetworkManager;
import com.midoconline.app.Util.SharePreferences;
import com.midoconline.app.Util.StringUtils;
import com.midoconline.app.Util.Utils;
import com.midoconline.app.beans.SignUpBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaymentOptionActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = PaymentOptionActivity.class.getName();
    private TextView mTxtDate;
    private EditText mEdtEmail;
    private EditText mEdtCardNumber;
    private EditText mEdtCVV;
    private EditText mEdtMobileNumber;
    private CheckBox mChkRememberMe;
    private CheckBox mChkKeepMeSignIn;
    private ImageView mBackwardPage;
    private ImageView mForwardPage;
    private Button mBtnPay;
    private RelativeLayout mainContainer;
    public SharePreferences mSharePreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_option);
        mSharePreferences = new SharePreferences(this);
        initiView();
    }

    private void initiView() {
        mainContainer = (RelativeLayout) findViewById(R.id.mainContainer);
        mEdtEmail = (EditText) findViewById(R.id.edt_email);
        mEdtCardNumber = (EditText) findViewById(R.id.edt_cardnumber);
        mEdtCVV = (EditText) findViewById(R.id.edt_cvv);
        mEdtMobileNumber = (EditText) findViewById(R.id.edt_mobilenumber);
        mChkRememberMe = (CheckBox) findViewById(R.id.chk_remember_me);
        mChkKeepMeSignIn = (CheckBox) findViewById(R.id.chk_keep_me_signin);
        mBackwardPage = (ImageView) findViewById(R.id.backward_arrow);
        mForwardPage = (ImageView) findViewById(R.id.forward_arrow);
        mBtnPay = (Button) findViewById(R.id.btn_pay);
        mBtnPay.setOnClickListener(this);
        mainContainer.setBackgroundResource(R.drawable.bg);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_payment_option, menu);
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

        if (v == mBtnPay){
            validation();
        }

    }

    private void validation(){
        if (StringUtils.isNotEmpty(mEdtEmail.getText().toString())&& StringUtils.isNotEmpty(mEdtCardNumber.getText().toString()) && StringUtils.isNotEmpty(mEdtCVV.getText().toString())&& StringUtils.isNotEmpty(mEdtMobileNumber.getText().toString())){
            if(NetworkManager.isConnectedToInternet(this)) {
                Utils.showProgress(this);
                ExecutePostRequest();
            }else {
                Utils.ShowSnackBar(mainContainer,getString(R.string.no_internet_connection));
            }

        }else {
            if (!StringUtils.isNotEmpty(mEdtEmail.getText().toString())){
                Utils.ShowSnackBar(mainContainer,getString(R.string.email_error));
            }
            if (!StringUtils.isNotEmpty(mEdtCardNumber.getText().toString())){
                Utils.ShowSnackBar(mainContainer, "Please fill card number");
            }
            if (!StringUtils.isNotEmpty(mEdtCVV.getText().toString())){
                Utils.ShowSnackBar(mainContainer, "Enter Cvv number");
            }
            if (!StringUtils.isNotEmpty(mEdtMobileNumber.getText().toString())){
                Utils.ShowSnackBar(mainContainer, getString(R.string.mobile_error));
            }


        }
    }

    public void ExecutePostRequest(){

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringrequest = new StringRequest(Request.Method.GET, Constants.URL.PAYMENT_OPTION_URL, new Response.Listener<String>() {
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
                params.put("card_number",mEdtEmail.getText().toString().trim());
                params.put("validity",mEdtEmail.getText().toString().trim());
                params.put("cvv", mEdtEmail.getText().toString().trim());
                params.put("card_type",mEdtEmail.getText().toString().trim());
//                params.put("authentication_token",mSharePreferences.getAuthenticationToken());
//                params.put("key",mSharePreferences.getKey());

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
                JSONObject mainObject = new JSONObject(response);
                String status = mainObject.getString("status");
                String msg = mainObject.getString("message");
                if (status.equalsIgnoreCase("success")){
                    Utils.ShowSnackBar(mainContainer,msg);
                }else {
                    Utils.ShowSnackBar(mainContainer,msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
