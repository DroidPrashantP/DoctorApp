package com.midoconline.app.ui.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.midoconline.app.R;
import com.midoconline.app.Util.SimpleDividerItemDecoration;
import com.midoconline.app.Util.WrappingLinearLayoutManager;
import com.midoconline.app.ui.adapters.HistoryCustomAdapter;
import com.midoconline.app.ui.adapters.HistoryRecyclerView;

public class DoctorHistoryActivity extends AppCompatActivity {

    private RelativeLayout mMainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setToolbar();

        mMainLayout = (RelativeLayout) findViewById(R.id.mainContainer);

        RecyclerView recentPostRecyclerView = (RecyclerView) findViewById(R.id.history_recyclerView);
        recentPostRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        HistoryRecyclerView rpAdapter = new HistoryRecyclerView(this);
        recentPostRecyclerView.setAdapter(rpAdapter);

       // mMainLayout.setBackgroundResource(R.drawable.bg);
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

    public  void ClickChatHistory(View view){
        Intent i = new Intent(DoctorHistoryActivity.this, HistoryChatActivity.class);
        startActivity(i);
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
}
