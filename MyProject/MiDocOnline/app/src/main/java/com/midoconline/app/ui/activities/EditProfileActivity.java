package com.midoconline.app.ui.activities;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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
import com.midoconline.app.Util.widget.CircularNetworkImageView;
import com.midoconline.app.beans.SignUpBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = EditProfileActivity.class.getName();
    public static final String SIMPLE_DATE_FORMAT = "dd-MM-yyyy";
    private EditText mEdtName, mEdtEmail, mEdtSpeciality, mEdtLicence, mEdtCity, mEdtStreet, mEdtZipcode, mEdtMobile, mEdtCountry, mEdtBirthday, mEdtSurname, mEdtNotes;
    private Spinner mHeightSpinner, mWeightSpinner, mBloogGroupSpinner;
    private Button mRequestApproval;
    private SignUpBean mSignUpBean;
    private LinearLayout mMainView;
    private SharePreferences mSharePreferences;
    private RelativeLayout mainContainer;
    private RelativeLayout maleWrapper, femaleWrapper;
    private TextView maleTextview, femaleTextview;
    private String mUserType;
    private TableRow mGenderLayout;
    public String mGender = "male";
    public String mBirthday;
    private static int mYear, mMonth, mDate;
    private View mView;
    private CircularNetworkImageView mUserImage;
    private String[] mHeightValues;
    private String[] mWeightValues;
    private String[] mBloodGropupValues;
    private TableRow mHeightWeightWrapper;
    private TableRow mBloogGroupWrapper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        setContentView(R.layout.activity_edit_profile);
        setToolbar();
        setCurrentValuesOfDateAndTime();
        intiView();
        setupView();

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setupView() {
        mSharePreferences = new SharePreferences(this);
        mUserType = mSharePreferences.getUserType();
        mUserImage = (CircularNetworkImageView) findViewById(R.id.user_image);
        mUserImage.setDefaultImageResId(R.drawable.profilepicture);

        mEdtName.setText(mSharePreferences.getUserName());
        mEdtSurname.setText(mSharePreferences.getSurname());
        mEdtEmail.setText(mSharePreferences.getUserEmail());
        mEdtSpeciality.setText(mSharePreferences.getUserSpecialist());
        mEdtCity.setText(mSharePreferences.getCity());
        mEdtMobile.setText(mSharePreferences.getMobile());
        mEdtCountry.setText(mSharePreferences.getCountry());
        Log.e("Birthday", mSharePreferences.getBirthdate());
        if (StringUtils.isNotEmpty(mSharePreferences.getBirthdate())) {
            mEdtBirthday.setText(mSharePreferences.getBirthdate());
        } else {
            mEdtBirthday.setHint(getString(R.string.birthday_text));
        }

        mHeightValues = getResources().getStringArray(R.array.height_array);
        mWeightValues = getResources().getStringArray(R.array.weight_array);
        mBloodGropupValues = getResources().getStringArray(R.array.bloodGroup_array);

        mHeightSpinner = (Spinner) findViewById(R.id.heightSpinner);
        mHeightSpinner.setAdapter(new MyAdapter(this, R.layout.custom_spinner_view, mHeightValues));

        mWeightSpinner = (Spinner) findViewById(R.id.weightSpinner);
        mWeightSpinner.setAdapter(new MyAdapter(this, R.layout.custom_spinner_view, mWeightValues));

        mBloogGroupSpinner = (Spinner) findViewById(R.id.bloodGroupSpinner);
        mBloogGroupSpinner.setAdapter(new MyAdapter(this, R.layout.custom_spinner_view, mBloodGropupValues));


        if (mUserType.equalsIgnoreCase(Constants.BundleKeys.PATIENT)) {
            mEdtSpeciality.setVisibility(View.GONE);
            mEdtLicence.setVisibility(View.GONE);
            mEdtCity.setVisibility(View.GONE);
            mEdtCountry.setVisibility(View.GONE);
            mEdtSurname.setVisibility(View.VISIBLE);
            mEdtBirthday.setVisibility(View.VISIBLE);
            mGenderLayout.setVisibility(View.VISIBLE);
            mEdtNotes.setVisibility(View.VISIBLE);
            mEdtStreet.setVisibility(View.VISIBLE);
            mEdtZipcode.setVisibility(View.VISIBLE);
            mHeightWeightWrapper.setVisibility(View.VISIBLE);
            mBloogGroupWrapper.setVisibility(View.VISIBLE);


        } else {
            mEdtSpeciality.setVisibility(View.VISIBLE);
            mEdtLicence.setVisibility(View.VISIBLE);
            mEdtCity.setVisibility(View.VISIBLE);
            mEdtCountry.setVisibility(View.VISIBLE);
            mEdtSurname.setVisibility(View.GONE);
            mEdtBirthday.setVisibility(View.GONE);
            mGenderLayout.setVisibility(View.GONE);
            mEdtNotes.setVisibility(View.GONE);
            mEdtStreet.setVisibility(View.GONE);
            mEdtZipcode.setVisibility(View.GONE);
            mHeightWeightWrapper.setVisibility(View.GONE);
            mBloogGroupWrapper.setVisibility(View.GONE);
        }

        if (mSharePreferences.getGender().equalsIgnoreCase("male")) {
            mGenderLayout.setBackgroundResource(R.drawable.oval_bg);
            maleWrapper.setBackgroundColor(getResources().getColor(R.color.royal_blue));
            femaleWrapper.setBackground(null);
            maleTextview.setTextColor(getResources().getColor(R.color.white));
            femaleTextview.setTextColor(getResources().getColor(R.color.blue));
            mGender = "male";
        } else {
            mGenderLayout.setBackgroundResource(R.drawable.oval_bg);
            maleWrapper.setBackground(null);
            femaleWrapper.setBackgroundColor(getResources().getColor(R.color.royal_blue));
            maleTextview.setTextColor(getResources().getColor(R.color.blue));
            femaleTextview.setTextColor(getResources().getColor(R.color.white));
            mGender = "female";
        }

        maleWrapper.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                mGenderLayout.setBackgroundResource(R.drawable.oval_bg);
                maleWrapper.setBackgroundColor(getResources().getColor(R.color.royal_blue));
                femaleWrapper.setBackground(null);
                maleTextview.setTextColor(getResources().getColor(R.color.white));
                femaleTextview.setTextColor(getResources().getColor(R.color.blue));
                mGender = "male";
            }
        });
        femaleWrapper.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                mGenderLayout.setBackgroundResource(R.drawable.oval_bg);
                maleWrapper.setBackground(null);
                femaleWrapper.setBackgroundColor(getResources().getColor(R.color.royal_blue));
                maleTextview.setTextColor(getResources().getColor(R.color.blue));
                femaleTextview.setTextColor(getResources().getColor(R.color.white));
                mGender = "female";
            }
        });

        mEdtBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);
            }
        });
    }

    private void intiView() {
        mainContainer = (RelativeLayout) findViewById(R.id.mainContainer);
        mainContainer.setBackgroundResource(R.drawable.bg);
        mMainView = (LinearLayout) findViewById(R.id.MainLayoutWrapper);
        mEdtName = (EditText) findViewById(R.id.edt_name);
        mEdtSurname = (EditText) findViewById(R.id.edt_surname);
        mEdtEmail = (EditText) findViewById(R.id.edt_email);
        mEdtSpeciality = (EditText) findViewById(R.id.edt_speciality);
        mEdtLicence = (EditText) findViewById(R.id.edt_licence);
        mEdtCity = (EditText) findViewById(R.id.edt_city);
        mEdtStreet = (EditText) findViewById(R.id.edt_street);
        mEdtZipcode = (EditText) findViewById(R.id.edt_zipcode);
        mEdtNotes = (EditText) findViewById(R.id.edt_notes);
        mEdtMobile = (EditText) findViewById(R.id.edt_mobile);
        mEdtCountry = (EditText) findViewById(R.id.edt_country);
        mEdtBirthday = (EditText) findViewById(R.id.edt_dob);
        mRequestApproval = (Button) findViewById(R.id.btn_request_approval);
        mRequestApproval.setOnClickListener(this);
        mGenderLayout = (TableRow) findViewById(R.id.genderLayout);
        maleWrapper = (RelativeLayout) findViewById(R.id.maleWrapper);
        femaleWrapper = (RelativeLayout) findViewById(R.id.femaleWrapper);
        maleTextview = (TextView) findViewById(R.id.maleTextview);
        femaleTextview = (TextView) findViewById(R.id.femaleTextview);
        mainContainer.setBackgroundResource(R.drawable.bg);
        mRequestApproval.setText(getString(R.string.sign_up_text));
        mRequestApproval.setText(getString(R.string.update_profile));

        mHeightWeightWrapper = (TableRow) findViewById(R.id.heightWeightWrapper);
        mBloogGroupWrapper = (TableRow) findViewById(R.id.bloodGroupWrapper);


    }

    public void setCurrentValuesOfDateAndTime() {
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDate = calendar.get(Calendar.DATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
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
        if (v == mRequestApproval) {
            validation();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        return new DatePickerDialog(this, dateSetListener, mYear, mMonth, mDate);
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.set(year, monthOfYear, dayOfMonth);
            Calendar calendar2 = Calendar.getInstance(Locale.getDefault());
            if (calendar.getTimeInMillis() < calendar2.getTimeInMillis()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
                String strDate = dateFormat.format(calendar.getTime());
                mEdtBirthday.setText(strDate);
            } else {
                Toast.makeText(EditProfileActivity.this, "Please select valid birthday", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };

    private void validation() {
        if (mUserType.equalsIgnoreCase(Constants.BundleKeys.DOCTOR)) {
            DoctorValidation();
        } else {
            PatientValidation();
        }

    }

    public void PatientValidation() {
        if (StringUtils.isNotEmpty(mEdtName.getText().toString()) && StringUtils.isNotEmpty(mEdtSurname.getText().toString()) && StringUtils.isNotEmpty(mEdtEmail.getText().toString()) && StringUtils.isNotEmpty(mEdtBirthday.getText().toString()) && StringUtils.isNotEmpty(mGender) && StringUtils.isNotEmpty(mEdtMobile.getText().toString())) {
            SendRequest();
        } else {

            if (!StringUtils.isNotEmpty(mEdtName.getText().toString())) {
                Utils.ShowDialog(getString(R.string.name_error), this);
            } else {
                if (!StringUtils.isNotEmpty(mEdtSurname.getText().toString())) {
                    Utils.ShowDialog(getString(R.string.surname_error), this);
                } else {
                    if (!StringUtils.isNotEmpty(mEdtEmail.getText().toString())) {
                        Utils.ShowDialog(getString(R.string.email_error), this);
                    } else {
                        if (!isValidEmail(mEdtEmail.getText().toString())) {
                            Utils.ShowDialog(getString(R.string.email_pattern_error), this);
                        } else {
                            if (!StringUtils.isNotEmpty(mEdtBirthday.getText().toString())) {
                                Utils.ShowDialog(getString(R.string.birthday_error), this);
                            } else {
                                if (!StringUtils.isNotEmpty(mEdtMobile.getText().toString())) {
                                    Utils.ShowDialog(getString(R.string.mobile_error), this);
                                } else {
                                    if (mEdtMobile.getText().toString().length() > 10)
                                        Utils.ShowDialog(getString(R.string.mobile_error), this);
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    public void DoctorValidation() {
        if (StringUtils.isNotEmpty(mEdtName.getText().toString()) && StringUtils.isNotEmpty(mEdtEmail.getText().toString()) && StringUtils.isNotEmpty(mEdtSpeciality.getText().toString()) && StringUtils.isNotEmpty(mEdtCity.getText().toString()) && StringUtils.isNotEmpty(mEdtCountry.getText().toString()) && StringUtils.isNotEmpty(mEdtMobile.getText().toString())) {
            SendRequest();
        } else {
            if (!StringUtils.isNotEmpty(mEdtName.getText().toString())) {
                Utils.ShowDialog(getString(R.string.name_error), this);
            } else {
                if (!StringUtils.isNotEmpty(mEdtEmail.getText().toString())) {
                    Utils.ShowDialog(getString(R.string.email_error), this);
                } else {
                    if (!isValidEmail(mEdtEmail.getText().toString())) {
                        Utils.ShowDialog(getString(R.string.email_pattern_error), this);
                    } else {
                        if (!StringUtils.isNotEmpty(mEdtSpeciality.getText().toString())) {
                            Utils.ShowDialog(getString(R.string.speciality_licence_error), this);
                        } else {
                            if (!StringUtils.isNotEmpty(mEdtCity.getText().toString())) {
                                Utils.ShowDialog(getString(R.string.city_error), this);
                            } else {
                                if (!StringUtils.isNotEmpty(mEdtMobile.getText().toString()) && mEdtMobile.getText().toString().length() > 10) {
                                    Utils.ShowDialog(getString(R.string.mobile_error), this);
                                } else {
                                    if (!StringUtils.isNotEmpty(mEdtCountry.getText().toString())) {
                                        Utils.ShowDialog(getString(R.string.country_error), this);
                                    } else {
                                        if (!StringUtils.isNotEmpty(mEdtMobile.getText().toString())) {
                                            Utils.ShowDialog(getString(R.string.mobile_error), this);
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void SendRequest() {
        mSignUpBean = new SignUpBean(mEdtName.getText().toString(), mEdtSurname.getText().toString(), mEdtCity.getText().toString(), mEdtEmail.getText().toString(), mEdtCountry.getText().toString(), mEdtMobile.getText().toString(), "", mEdtSpeciality.getText().toString(), mGender, mBirthday);
        if (NetworkManager.isConnectedToInternet(this)) {

            if ((isValidEmail(mEdtEmail.getText().toString())) && mEdtMobile.getText().toString().length() == 10) {
                Utils.showProgress(this);
                ExecutePostRequest();
            } else {
                if (!isValidEmail(mEdtEmail.getText().toString())) {
                    Utils.ShowDialog(getString(R.string.email_pattern_error), this);
                }
                if (mEdtMobile.getText().toString().length() > 10 || mEdtMobile.getText().toString().length() < 10) {
                    Utils.ShowDialog(getString(R.string.mobile_pattern_error), this);
                }
            }
        } else {
            Utils.ShowSnackBar(mMainView, getString(R.string.no_internet_connection));
        }
    }

    public void ExecutePostRequest() {

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringrequest = new StringRequest(Request.Method.POST, Constants.URL.UPDATE_PROFILE, new Response.Listener<String>() {
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
                Log.d(TAG, "" + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", "" + mEdtName.getText().toString());
                params.put("password", "" + mSignUpBean.getPassword());
                params.put("mobile_no", "" + mEdtMobile.getText().toString());
                params.put("key", mSharePreferences.getKey());
                params.put("authentication_token", mSharePreferences.getAuthenticationToken());

                if (mUserType.equalsIgnoreCase(Constants.BundleKeys.DOCTOR)) {
                    params.put("specialize", "" + mSignUpBean.getSpecialize());
                    params.put("licence", "" + mEdtLicence.getText().toString());
                    params.put("city", "" + mSignUpBean.getcity());
                    params.put("country", "" + mSignUpBean.getcountry());
                } else {
                    params.put("dob", "" + mEdtBirthday.getText().toString());
                    params.put("surname", "" + mEdtSurname.getText().toString());
                    params.put("gender", "" + mGender);
                    params.put("height", "" + mHeightSpinner.getSelectedItem().toString());
                    params.put("weight", "" + mWeightSpinner.getSelectedItem().toString());
                    params.put("blood_group", "" + mBloogGroupSpinner.getSelectedItem().toString());
                    params.put("zip_code", "" + mEdtZipcode.getText().toString());
                    params.put("street", "" + mEdtStreet.getText().toString());
                    params.put("med_notes", "" + mEdtNotes.getText().toString());
                    params.put("image_url", "");
                    params.put("terms_and_condition", "" + true);
                }

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        stringrequest.setRetryPolicy(new DefaultRetryPolicy(Constants.MY_SOCKET_TIMEOUT_MS, Constants.MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringrequest);
    }

    private void ParseReponse(String response) {
        Log.e("Response", response);
        if (StringUtils.isNotEmpty(response)) {
            try {
                JSONObject mainobj = new JSONObject(response);
                String status = mainobj.getString("status");
                String message = mainobj.getString("message");
                if (status.equalsIgnoreCase("Success")) {
                    JSONObject obj = mainobj.getJSONObject("user");
                    String user_type = obj.getString("type");
                    if (user_type.equalsIgnoreCase(Constants.BundleKeys.DOCTOR)) {
                        clearData();
                    } else {
                        mSharePreferences.setLoggedIn(true);
                        mSharePreferences.setUserEmail(obj.getString("email"));
                        mSharePreferences.setUserName(obj.getString("username"));
                        mSharePreferences.setSurname(obj.getString("surname"));
                        mSharePreferences.setUserId(obj.getString("id"));
                        mSharePreferences.setAuthenticationToken(obj.getString("authentication_token"));
                        mSharePreferences.setKey(obj.getString("key"));
                        mSharePreferences.setSecretKey(obj.getString("secret_key"));
                        mSharePreferences.setMobile(obj.getString("mobile_no"));
                        mSharePreferences.setUserType(user_type);
                        mSharePreferences.setGender(obj.getString("gender"));
                        mSharePreferences.setBirthdate(obj.getString("dob"));
                        ShowDialog(message);

                    }

                } else if (status.equalsIgnoreCase("Failure")) {
                    Utils.ShowDialog(message, EditProfileActivity.this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public void setToolbar() {
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

    public void ShowDialog(String message) {

        final Dialog mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.layout_custom_logout_dialog);
        mDialog.setCancelable(true);
        mDialog.show();
        TextView textViewmessage = (TextView) mDialog.findViewById(R.id.text);
        textViewmessage.setText(message);
        LinearLayout mainWrapper = (LinearLayout) mDialog.findViewById(R.id.mainWrapper);
        mDialog.findViewById(R.id.cancel_logout_btn).setVisibility(View.GONE);
        Button okBtn = (Button) mDialog.findViewById(R.id.logout_yes_btn);
        okBtn.setText("Ok");
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                finish();
            }
        });

    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public void clearData() {
        mSharePreferences.ClearSharepreference();
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
            TextView main_text = (TextView) mySpinner.findViewById(R.id.speciality_textView);
            main_text.setText(spinnervalue[position]);
            return mySpinner;
        }
    }
}

