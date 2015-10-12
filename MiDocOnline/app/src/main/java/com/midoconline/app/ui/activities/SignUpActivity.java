package com.midoconline.app.ui.activities;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.midoconline.app.R;
import com.midoconline.app.Util.StringUtils;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = SignUpActivity.class.getName();
    private EditText mEdtName, mEdtEmail,mEdtPassword,mEdtSpecialityLicence;
    private Button mRequestApproval;
    private TextInputLayout mEmailTextInput, mNameTextInput, mPasswordTextInput, mSpecialityTextInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        intiView();
    }

    private void intiView() {
        mEdtName = (EditText) findViewById(R.id.edt_name);
        mEdtEmail = (EditText) findViewById(R.id.edt_email);
        mEdtPassword = (EditText) findViewById(R.id.edt_password);
        mEdtSpecialityLicence = (EditText) findViewById(R.id.edt_speciality_licence);
        mRequestApproval = (Button) findViewById(R.id.btn_request_approval);
        mRequestApproval.setOnClickListener(this);
        mEmailTextInput = (TextInputLayout) findViewById(R.id.til_email);
        mNameTextInput = (TextInputLayout) findViewById(R.id.til_name);
        mPasswordTextInput = (TextInputLayout) findViewById(R.id.til_password);
        mSpecialityTextInput = (TextInputLayout) findViewById(R.id.til_speciality_licence);

        mSpecialityTextInput.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (text.length() <2 ) {

                    mSpecialityTextInput.setError(getString(R.string.speciality_licence_error));
                    mSpecialityTextInput.setErrorEnabled(true);
                } else {

                    mSpecialityTextInput.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
        if (v == mRequestApproval){
            validation();
        }
    }

    private void validation(){
        if (StringUtils.isNotEmpty(mEdtName.getText().toString()) && StringUtils.isNotEmpty(mEdtEmail.getText().toString()) && StringUtils.isNotEmpty(mEdtPassword.getText().toString()) && StringUtils.isNotEmpty(mEdtSpecialityLicence.getText().toString())){
            Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
            startActivity(intent);
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
        }

    }
}
