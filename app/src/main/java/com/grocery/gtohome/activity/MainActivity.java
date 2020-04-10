package com.grocery.gtohome.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.grocery.gtohome.CustomExpandableListAdapter;
import com.grocery.gtohome.ExpandableListDataSource;
import com.grocery.gtohome.FragmentNavigationManager;
import com.grocery.gtohome.NavigationManager;
import com.grocery.gtohome.R;
import com.grocery.gtohome.fragment.ContactUs_Fragment;
import com.grocery.gtohome.fragment.Home_Fragment;
import com.grocery.gtohome.fragment.my_basket.MyBasket_Fragment;
import com.grocery.gtohome.fragment.my_account.My_Account_Fragment;
import com.grocery.gtohome.fragment.Search_Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public BottomNavigationView navView;
    TextView tv_main_header;
    ImageView iv_drawer;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    String mActivityTitle;
     String[] items;

    private ExpandableListView mExpandableListView;
    private ExpandableListAdapter mExpandableListAdapter;
    private List<String> mExpandableListTitle;
    private NavigationManager mNavigationManager;

    private Map<String, List<String>> mExpandableListData;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Fragment fragment_home = new Home_Fragment();
                    FragmentTransaction ft_home = getSupportFragmentManager().beginTransaction();
                    ft_home.replace(R.id.frame, fragment_home);
                    // ft.addToBackStack(null);
                    ft_home.commit();

                    return true;
                case R.id.navigation_search:
                    Fragment fragment_faq = new Search_Fragment();
                    FragmentTransaction ft_faq = getSupportFragmentManager().beginTransaction();
                    ft_faq.replace(R.id.frame, fragment_faq);
                    // ft.addToBackStack(null);
                    ft_faq.commit();

                    return true;
                case R.id.navigation_my_basket:
                    Fragment fragment_create = new MyBasket_Fragment();
                    FragmentTransaction ft_create = getSupportFragmentManager().beginTransaction();
                    ft_create.replace(R.id.frame, fragment_create);
                    // ft.addToBackStack(null);
                    ft_create.commit();

                    return true;

                case R.id.navigation_my_account:
                    Fragment fragment_privacy = new My_Account_Fragment();
                    FragmentTransaction ft_p = getSupportFragmentManager().beginTransaction();
                    ft_p.replace(R.id.frame, fragment_privacy);
                    // ft.addToBackStack(null);
                    ft_p.commit();

                    return true;

                case R.id.navigation_contact_us:
                    Fragment fragment = new ContactUs_Fragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame, fragment);
                    // ft.addToBackStack(null);
                    ft.commit();

                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.nav_view);
        tv_main_header = findViewById(R.id.tv_main_header);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //open default fragmentframe
        setHomeFragment();
        navView.getMenu().findItem(R.id.navigation_home).setChecked(true);


        //*****************************
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        iv_drawer = findViewById(R.id.iv_drawer);
        iv_drawer.setOnClickListener(this);
        //mActivityTitle = getTitle().toString();

        mExpandableListView = (ExpandableListView) findViewById(R.id.navList);
        mNavigationManager = FragmentNavigationManager.obtain(this);

        initItems();

        LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.nav_header, null, false);
        mExpandableListView.addHeaderView(listHeaderView);
       // mExpandableListView.addFooterView(listHeaderView);

        mExpandableListData = ExpandableListDataSource.getData(this);
        mExpandableListTitle = new ArrayList(mExpandableListData.keySet());

        addDrawerItems();
        setupDrawer();

        if (savedInstanceState == null) {
            //  selectFirstItemAsDefault();
        }

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);

    }


    private void setHomeFragment() {
        Home_Fragment homefragment = new Home_Fragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.frame, homefragment, "HOME_FRAGMENT");
        ft.addToBackStack(null);
        ft.commit();
    }


    @Override
    public void onBackPressed() {
        Home_Fragment myFragment = (Home_Fragment) getSupportFragmentManager().findFragmentByTag("HOME_FRAGMENT");
        if (myFragment != null && myFragment.isVisible()) {
            // add your code here
            finish();
        } else {
            super.onBackPressed();
        }


    }

    public void Update_header(String title) {
        tv_main_header.setText(title);
    }


    //**************************************************************************
    private void selectFirstItemAsDefault() {
        if (mNavigationManager != null) {
            String firstActionMovie = getResources().getStringArray(R.array.actionFilms)[0];
            mNavigationManager.showFragmentAction(firstActionMovie);
            getSupportActionBar().setTitle(firstActionMovie);
        }
    }

    private void initItems() {
        items = getResources().getStringArray(R.array.film_genre);
    }

    private void addDrawerItems() {
        mExpandableListAdapter = new CustomExpandableListAdapter(this, mExpandableListTitle, mExpandableListData);
        mExpandableListView.setAdapter(mExpandableListAdapter);
        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
//                getSupportActionBar().setTitle(mExpandableListTitle.get(groupPosition).toString());
            }
        });

        mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                //getSupportActionBar().setTitle(R.string.film_genres);
            }
        });

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                String selectedItem = ((List) (mExpandableListData.get(mExpandableListTitle.get(groupPosition))))
                        .get(childPosition).toString();
                //  getSupportActionBar().setTitle(selectedItem);

                mNavigationManager.showFragmentAction(selectedItem);
//                if (items[0].equals(mExpandableListTitle.get(groupPosition))) {
//                     mNavigationManager.showFragmentAction(selectedItem);
//                } else if (items[1].equals(mExpandableListTitle.get(groupPosition))) {
//                    //  mNavigationManager.showFragmentComedy(selectedItem);
//                } else if (items[2].equals(mExpandableListTitle.get(groupPosition))) {
//                    // mNavigationManager.showFragmentDrama(selectedItem);
//                } else if (items[3].equals(mExpandableListTitle.get(groupPosition))) {
//                    //  mNavigationManager.showFragmentMusical(selectedItem);
//                } else if (items[4].equals(mExpandableListTitle.get(groupPosition))) {
//                    //mNavigationManager.showFragmentThriller(selectedItem);
//                } else {
//                    throw new IllegalArgumentException("Not supported fragment type");
//                }

                mDrawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                getSupportActionBar().setTitle(R.string.film_genres);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_drawer:
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {

                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {

                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                break;
        }
    }
}
