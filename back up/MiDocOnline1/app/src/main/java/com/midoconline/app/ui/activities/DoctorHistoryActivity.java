package com.midoconline.app.ui.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.midoconline.app.R;
import com.midoconline.app.Util.Constants;
import com.midoconline.app.Util.HttpsTrustManager;
import com.midoconline.app.Util.SharePreferences;
import com.midoconline.app.Util.StringUtils;
import com.midoconline.app.Util.Utils;
import com.midoconline.app.Util.WrappingLinearLayoutManager;
import com.midoconline.app.beans.History;
import com.midoconline.app.beans.HistoryHandler;
import com.midoconline.app.services.IncomeCallListenerService;
import com.midoconline.app.ui.adapters.HistoryRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DoctorHistoryActivity extends AppCompatActivity {

    private RelativeLayout mMainLayout;
    private SharePreferences mSharePreferences;
    private ArrayList<History> historyArrayList = new ArrayList<History>();
    private RecyclerView recentPostRecyclerView;
    private HistoryRecyclerView rpAdapter;
    private TextView totalAmountText;
    private Double totalAmount;
    private TextView emptyTextView;
    private TextView textAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setToolbar();
        mSharePreferences = new SharePreferences(this);
        // totalAmountText = (TextView) findViewById(R.id.totalAmountText);
        textAccount = (TextView) findViewById(R.id.text_account);
        mMainLayout = (RelativeLayout) findViewById(R.id.mainContainer);
        emptyTextView = (TextView) findViewById(R.id.emptyTextView);
        recentPostRecyclerView = (RecyclerView) findViewById(R.id.history_recyclerView);
        String accountType = mSharePreferences.getUserType().equalsIgnoreCase(Constants.BundleKeys.DOCTOR) ? "Doctor History" : "Patient History";
        textAccount.setText(accountType);
        mMainLayout.setBackgroundResource(R.drawable.bg);
        getDoctorHistroy();
    }

    private void getDoctorHistroy() {
        Utils.showProgress(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        HttpsTrustManager.allowAllSSL();
        String url = mSharePreferences.getUserType().equalsIgnoreCase(Constants.BundleKeys.DOCTOR) ? Constants.URL.DOCOTR_HISTORY : Constants.URL.PATIENT_HISTORY;
        url = url + "?authentication_token=" + mSharePreferences.getAuthenticationToken() + "&key=" + mSharePreferences.getKey();
        StringRequest stringrequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utils.closeProgress();
                Log.d("Response", response);
                parseHistorResponse(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.closeProgress();
                Log.e("Error", error.toString());
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        JSONObject obj = new JSONObject(res);
                        //   {"message":"Another account is already using this email address","status":"Failure"}
                        Log.e("Error", obj.toString());
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        queue.add(stringrequest);
    }

    private void parseHistorResponse(String response) {
        if (StringUtils.isNotEmpty(response)) {
            try {
                String status = new JSONObject(response).getString("status");
                JSONArray historyArray = new JSONObject(response).getJSONArray("history");
                if (historyArray.length() > 0) {
                    recentPostRecyclerView.setVisibility(View.VISIBLE);
                    emptyTextView.setVisibility(View.GONE);
                    for (int i = 0; i < historyArray.length(); i++) {
                        JSONObject object = historyArray.getJSONObject(i);
                        History history = new History(object.getString("receiver_id"), object.getString("caller_id"), object.getString("duration"), object.getString("chat_type"), object.getString("call_status"), object.getString("amount"), object.getString("currency"), object.getString("qb_caller_id"), object.getString("qb_receiver_id"), object.getString("doctor_id"), object.getString("patient_id"), object.getString("doctor_email"), object.getString("patient_email"), object.getString("doctor_full_name"), object.getString("patient_full_name"), object.getString("doctor_specialization"), object.getString("call_date"));
                        historyArrayList.add(history);
                        //addAmount(object.getString("amount"));
                    }
                    updateAdapter();
                } else {
                    recentPostRecyclerView.setVisibility(View.GONE);
                    emptyTextView.setVisibility(View.VISIBLE);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateAdapter() {
        recentPostRecyclerView.setLayoutManager(new WrappingLinearLayoutManager(this));
        recentPostRecyclerView.setHasFixedSize(true);
        rpAdapter = new HistoryRecyclerView(this, historyArrayList);
        recentPostRecyclerView.setAdapter(rpAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
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

    public void addAmount(String amount) {
        if (StringUtils.isNotEmpty(amount)) {
            totalAmount = totalAmount + Double.parseDouble(amount);
        }
    }

    public void setToolbar() {
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

    public void ClickOnCallBtn(History history) {
        Utils.showProgress(this);
        HistoryHandler.getInstance().setCallHistoryObj(history);
        String login = mSharePreferences.getQBEmail();
        String password = "password";
        startIncomeCallListenerService(login, password, Constants.LOGIN, Constants.BundleKeys.HISTORY_CALL);
    }

    public void startIncomeCallListenerService(String login, String password, int startServiceVariant, String normalCall) {
        Intent tempIntent = new Intent(this, IncomeCallListenerService.class);
        PendingIntent pendingIntent = createPendingResult(Constants.LOGIN_TASK_CODE, tempIntent, 0);
        Intent intent = new Intent(this, IncomeCallListenerService.class);
        intent.putExtra(Constants.USER_LOGIN, login);
        intent.putExtra(Constants.USER_PASSWORD, password);
        intent.putExtra(Constants.START_SERVICE_VARIANT, startServiceVariant);
        intent.putExtra(Constants.PARAM_PINTENT, pendingIntent);
        intent.putExtra(Constants.BundleKeys.CALL_TYPE, normalCall);
        startService(intent);
    }
}
