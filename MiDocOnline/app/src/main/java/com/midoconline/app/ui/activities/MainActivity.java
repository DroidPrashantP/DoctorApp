package com.midoconline.app.ui.activities;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.midoconline.app.R;

public class MainActivity extends AppCompatActivity {

    private static final int NAV_ITEMS_MAIN = R.id.group_main;
    private DrawerLayout mDrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        initializeNavigationDrawer();
    }

    /**
     * Initialize navigation drawer
     */
    protected void initializeNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        // set onClicklistener on user profile layout
        findViewById(R.id.ll_user_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawers();
//                createView(new UserProfileActivity(), R.string.profile);
//                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
//                startActivity(intent);
            }
        });

    }
    /**
     * Setup menus action
     *
     * @param navigationView
     */
    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //if an item from extras group is clicked,refresh NAV_ITEMS_MAIN to remove previously checked item
                navigationView.getMenu().setGroupCheckable(NAV_ITEMS_MAIN, (menuItem.getGroupId() == NAV_ITEMS_MAIN), true);

//                //Update highlighted item in the navigation menu
//                if (menuItem.getGroupId() != NAV_ITEMS_EXTRA) {
//                    menuItem.setChecked(true);
//                }

                switch (menuItem.getItemId()) {
                    case R.id.item_place_order:
                        //createView(new DealFragment(), R.string.title_deal);
                        break;
                    case R.id.item_medical_history:
                        // createView(new CouponFragment(), R.string.title_coupon);
                        break;
                    case R.id.item_share:
                        // createView(new TopProductsFragment(), R.string.title_price_comparison);
                        break;
                    case R.id.item_about_us:
                        // createView(new DealFragment(), R.string.title_stores);
                        break;

                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    /**
     * Create fragment view : returns on click menu item
     *
     * @param fragment
     * @param title
     */
    private void createView(Fragment fragment, int title) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_layout, fragment, getApplicationContext().getText(title).toString())
                .addToBackStack(null)
                .commit();
    }

    public void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("MiDocOnline");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_drawer);
        ab.setDisplayHomeAsUpEnabled(true);
//        toolBarHeight = toolbar.getHeight()
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
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
