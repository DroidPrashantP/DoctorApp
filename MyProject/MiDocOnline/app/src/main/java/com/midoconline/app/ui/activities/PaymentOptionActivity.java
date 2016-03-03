package com.midoconline.app.ui.activities;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.internal.Utility;
import com.midoconline.app.R;
import com.midoconline.app.Util.Constants;
import com.midoconline.app.Util.NetworkManager;
import com.midoconline.app.Util.SharePreferences;
import com.midoconline.app.Util.StringUtils;
import com.midoconline.app.Util.Utils;
import com.midoconline.app.beans.SignUpBean;
import com.midoconline.app.services.IncomeCallListenerService;
import com.midoconline.app.ui.activities.util.ErrorDialogFragment;
import com.midoconline.app.ui.interfaces.PaymentForm;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class PaymentOptionActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = PaymentOptionActivity.class.getName();
    private EditText mEdtCardNumber;
    private EditText mEdtCVV;
    private Button mBtnPay;
    private RelativeLayout mainContainer;
    public SharePreferences mSharePreferences;
    private String stripTokenID;


    /*
    * Change this to your publishable key.
    *
    * You can get your key here: https://manage.stripe.com/account/apikeys
    */
//    public static final String PUBLISHABLE_KEY = "sk_test_0mwlAmQVrlBfQJ5sHlgmXQQy";  // test key
      public static final String PUBLISHABLE_KEY = "pk_test_rtY8DogK3CGi5lBmPTZVA7AZ"; // publishable test key
//    public static final String PUBLISHABLE_KEY = "sk_live_LQlW1Ljf8PpqTPGeLtPveDvs"; //  live key
//    public static final String PUBLISHABLE_KEY = "pk_live_cM3pk51kT0hLVYa0SIBGB8BB";  // live publishable key

      private Spinner mMonthSpinner;
      private Spinner mYearSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        setContentView(R.layout.activity_payment_option);
        mSharePreferences = new SharePreferences(this);
        initiView();
        setToolbar();
    }

    private void initiView() {
        mainContainer = (RelativeLayout) findViewById(R.id.mainContainer);
        mEdtCardNumber = (EditText) findViewById(R.id.edt_cardnumber);
        mEdtCVV = (EditText) findViewById(R.id.edt_cvv);
        mMonthSpinner = (Spinner) findViewById(R.id.expMonth);
        mYearSpinner = (Spinner) findViewById(R.id.expYear);

        mMonthSpinner.setAdapter(new MyAdapter(this, R.layout.custom_spinner_view, getResources().getStringArray(R.array.month_array)));
        mYearSpinner.setAdapter(new MyAdapter(this, R.layout.custom_spinner_view, getResources().getStringArray(R.array.year_array)));

        mBtnPay = (Button) findViewById(R.id.btn_pay);
        mBtnPay.setOnClickListener(this);
        mainContainer.setBackgroundResource(R.drawable.bg);

    }

    public void saveCreditCard() {
        int ExpMonth =  getInteger(mMonthSpinner);
        int ExpYear =   getInteger(mYearSpinner);

        Card card = new Card(
                mEdtCardNumber.getText().toString(),
                ExpMonth,
                ExpYear,
                mEdtCVV.getText().toString());

        boolean validation = card.validateCard();
        if (validation) {
            startProgress();
            new Stripe().createToken(
                    card,
                    PUBLISHABLE_KEY,
                    new TokenCallback() {
                        public void onSuccess(Token token) {
                            String endingIn = getResources().getString(R.string.endingIn);
                            stripTokenID = token.getId();
                            Log.d("Card number", stripTokenID);
                            finishProgress();
                            mSharePreferences.setStripKey(stripTokenID);
//                            if (NetworkManager.isConnectedToInternet(PaymentOptionActivity.this)) {
//                                Utils.showProgress(PaymentOptionActivity.this);
                                ExecutePaymentRequest();
//                            }else {
//                                Utils.ShowDialog(getString(R.string.no_internet_connection),PaymentOptionActivity.this);
//                            }
                        }

                        public void onError(Exception error) {
                            handleError(error.getLocalizedMessage());
                            finishProgress();
                        }
                    });
        } else if (!card.validateNumber()) {
            handleError("The card number that you entered is invalid");
        } else if (!card.validateExpiryDate()) {
            handleError("The expiration date that you entered is invalid");
        } else if (!card.validateCVC()) {
            handleError("The CVC code that you entered is invalid");
        } else {
            handleError("The card details that you entered are invalid");
        }
    }

    @Override
    public void onClick(View v) {

        if (v == mBtnPay){
            saveCreditCard();
        }

    }

    public void startIncomeCallListenerService(String login, String password, int startServiceVariant){
        Intent tempIntent = new Intent(this, IncomeCallListenerService.class);
        PendingIntent pendingIntent = createPendingResult(Constants.LOGIN_TASK_CODE, tempIntent, 0);
        Intent intent = new Intent(this, IncomeCallListenerService.class);
        intent.putExtra(Constants.USER_LOGIN, login);
        intent.putExtra(Constants.USER_PASSWORD, password);
        intent.putExtra(Constants.START_SERVICE_VARIANT, startServiceVariant);
        intent.putExtra(Constants.PARAM_PINTENT, pendingIntent);
        startService(intent);
    }
    public void ExecutePaymentRequest(){
        Utils.showProgress(PaymentOptionActivity.this);
        String login = mSharePreferences.getQBEmail();
        String password ="password" ;
        Utils.showProgress(PaymentOptionActivity.this);
        startIncomeCallListenerService(login, password, Constants.LOGIN);
        Utils.closeProgress();

//        RequestQueue queue = Volley.newRequestQueue(this);
//        StringRequest stringrequest = new StringRequest(Request.Method.POST, Constants.URL.CHARGE_PAYMENT_URL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Utils.closeProgress();
//                Log.d(TAG, response);
//                ParseChargePaymentReponse(response);
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Utils.closeProgress();
//                Log.d(TAG,""+ error.toString());
//                NetworkResponse response = error.networkResponse;
//                if (response != null) {
//                    try {
//                        String res = new String(response.data,
//                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
//                        JSONObject obj = new JSONObject(res);
//                        Log.d(TAG,""+ obj.toString());
//                        if (obj.has("message")){
//                            String message = obj.getString("message");
//                        }
//                    } catch (UnsupportedEncodingException e1) {
//                        e1.printStackTrace();
//                    } catch (JSONException e2) {
//                        e2.printStackTrace();
//                    }
//                }
//            }
//        }){
//            @Override
//            protected Map<String,String> getParams(){
//                Map<String,String> params = new HashMap<String, String>();
//                params.put("key",mSharePreferences.getKey());
//                params.put("authentication_token",mSharePreferences.getAuthenticationToken());
//                params.put("email", mSharePreferences.getUserEmail());
//                params.put("stripeToken",stripTokenID);
//                params.put("amount","1000");
//
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders()  {
//                Map<String,String> params = new HashMap<String, String>();
//                return params;
//            }
//        };
//        queue.add(stringrequest);
    }
    private void ParseChargePaymentReponse(String response) {
        if (StringUtils.isNotEmpty(response)){
            try {
                JSONObject mainObject = new JSONObject(response);
                String status = mainObject.getString("status");
                String msg = mainObject.getString("message");
                if (status.equalsIgnoreCase("success")){
                    ShowDialog(msg);
                }else {
                    Utils.ShowSnackBar(mainContainer,msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


//    private void ParseSaveCardReponse(String response) {
//        if (StringUtils.isNotEmpty(response)){
//            try {
//                JSONObject mainObject = new JSONObject(response);
//                String status = mainObject.getString("status");
//                String msg = mainObject.getString("message");
//                if (status.equalsIgnoreCase("success")){
//                    Utils.ShowSnackBar(mainContainer,msg);
//                    finish();
//                }else {
//                    Utils.ShowSnackBar(mainContainer,msg);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }

    private void startProgress() {
        Utils.showProgress(this);
    }

    private void finishProgress() {
        Utils.closeProgress();
    }

    private void handleError(String error) {
        DialogFragment fragment = ErrorDialogFragment.newInstance(R.string.validationErrors, error);
        fragment.show(getSupportFragmentManager(), "error");
    }

    private Integer getInteger(Spinner spinner) {
        try {
            return Integer.parseInt(spinner.getSelectedItem().toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    public void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ImageView nav_drawer = (ImageView) findViewById(R.id.nav_drawer);
        nav_drawer.setImageResource(R.drawable.back_arrow);
        nav_drawer.setColorFilter(getResources().getColor(R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
        nav_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public class MyAdapter extends ArrayAdapter<String> {
        private String[] spinnervalue;
        public MyAdapter(Context ctx, int txtViewResourceId, String[] objects) {
            super(ctx, txtViewResourceId, objects);
            this.spinnervalue = objects;
        }
        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
            return getCustomView(position, cnvtView, prnt);
        }
        @Override
        public View getView(int pos, View cnvtView, ViewGroup prnt) {
            return getCustomView(pos, cnvtView, prnt);
        }
        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.custom_spinner_view, parent, false);
            TextView main_text = (TextView) mySpinner .findViewById(R.id.speciality_textView);
            main_text.setText(spinnervalue[position]);
            return mySpinner;
        }
    }

    public void ShowDialog(String msg) {

        final Dialog mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.layout_custom_logout_dialog);
        mDialog.setCancelable(true);
        mDialog.show();
        TextView message = (TextView) mDialog.findViewById(R.id.text);
        message.setText(msg);
        LinearLayout mainWrapper = (LinearLayout) mDialog.findViewById(R.id.mainWrapper);
        mDialog.findViewById(R.id.cancel_logout_btn).setVisibility(View.GONE);
        Button okBtn = (Button) mDialog.findViewById(R.id.logout_yes_btn);
        okBtn.setText("Ok");
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Intent intent = new Intent(PaymentOptionActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

    }
}
