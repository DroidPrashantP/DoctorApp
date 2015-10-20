package com.midoconline.app.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.midoconline.app.R;
import com.midoconline.app.Util.SharePreferences;
import com.midoconline.app.Util.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int NAV_ITEMS_MAIN = R.id.group_main;
    private DrawerLayout mDrawerLayout;
    private Button mDoctorLogIn;
    String[] mMedicalSpecialitySpinnerValues = { "MEDICAL SPECIALIST", "Orthipedics", "Cunecology"};
    String[] mDoctorspinnerValues = { "DOCTOR", "Dr.Smith", "Dr. Greg"};
    Spinner mMenuSpinner;
    private SharePreferences mSharePreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharePreferences = new SharePreferences(this);
        IniTiView();
        setToolbar();
    }

    private void  IniTiView(){
        mDoctorLogIn = (Button) findViewById(R.id.btn_doctor_login);
        mDoctorLogIn.setOnClickListener(this);

        Spinner mySpinner = (Spinner) findViewById(R.id.MS_spinner);
        mySpinner.setAdapter(new MyAdapter(this, R.layout.custom_spinner_view, mMedicalSpecialitySpinnerValues));

        Spinner doctorSpinner = (Spinner) findViewById(R.id.Doctor_spinner);
        doctorSpinner.setAdapter(new MyAdapter(this, R.layout.custom_spinner_view, mDoctorspinnerValues));

    }

    public void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);

        findViewById(R.id.nav_drawer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSharePreferences.isLoggedIn()) {
                    mMenuSpinner.performClick();
                }else {
                    Utils.ShowDialog("Please Login First",MainActivity.this);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == mDoctorLogIn){
            Intent intent = new Intent(MainActivity.this, AskForRegistration.class);
            startActivity(intent);
        }
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


}
