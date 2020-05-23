package com.grocery.gtohome.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.grocery.gtohome.CustomExpandableListAdapter;
import com.grocery.gtohome.FragmentNavigationManager;
import com.grocery.gtohome.NavigationManager;
import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.login_signup.Login_Activity;
import com.grocery.gtohome.adapter.WishList_Adapter;
import com.grocery.gtohome.api_client.Api_Call;
import com.grocery.gtohome.api_client.Base_Url;
import com.grocery.gtohome.api_client.RxApiClient;
import com.grocery.gtohome.fragment.All_Product_Fragment;
import com.grocery.gtohome.fragment.ContactUs_Fragment;
import com.grocery.gtohome.fragment.Home_Fragment;
import com.grocery.gtohome.fragment.my_basket.MyBasket_Fragment;
import com.grocery.gtohome.fragment.my_account.My_Account_Fragment;
import com.grocery.gtohome.fragment.Search_Fragment;
import com.grocery.gtohome.fragment.nav_fragment.Blog_Fragment;
import com.grocery.gtohome.model.SimpleResultModel;
import com.grocery.gtohome.model.category_model.CategoryChild;
import com.grocery.gtohome.model.category_model.CategoryModel;
import com.grocery.gtohome.model.category_model.CategoryName;
import com.grocery.gtohome.model.wishlist_model.Wishlist_Model;
import com.grocery.gtohome.session.SessionManager;
import com.grocery.gtohome.utils.Connectivity;
import com.grocery.gtohome.utils.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

import static com.grocery.gtohome.api_client.Base_Url.BaseUrl;
import static com.grocery.gtohome.api_client.Base_Url.categoriesapi;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public BottomNavigationView navView;
    BottomNavigationMenuView bottomNavigationMenuView;
    TextView tv_main_header;
    ImageView iv_drawer;
    private Utilities utilities;
    public static TextView tv_budge;
    //*******************************
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    String mActivityTitle;
     String[] items;

    private ExpandableListView mExpandableListView;
    private ExpandableListAdapter mExpandableListAdapter;
     List<String> mExpandableListTitle;
    private NavigationManager mNavigationManager;
    // Map<String, List<String>> mExpandableListData;
     Map<String, List<CategoryChild>> mExpandableListData;

    SessionManager session;
    private String Customer_Id;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Fragment fragment_home = new Home_Fragment();
                    FragmentTransaction ft_home = getSupportFragmentManager().beginTransaction();
                    ft_home.replace(R.id.frame, fragment_home, "HOME_FRAGMENT");
                    ft_home.addToBackStack(null);
                    ft_home.commit();

                    return true;
                case R.id.navigation_search:
                    Fragment fragment_faq = new Search_Fragment();
                    FragmentTransaction ft_faq = getSupportFragmentManager().beginTransaction();
                    ft_faq.replace(R.id.frame, fragment_faq);
                    ft_faq.addToBackStack(null);
                    ft_faq.commit();

                    return true;
                case R.id.navigation_my_basket:
                    Fragment fragment_create = new MyBasket_Fragment();
                    FragmentTransaction ft_create = getSupportFragmentManager().beginTransaction();
                    ft_create.replace(R.id.frame, fragment_create);
                    ft_create.addToBackStack(null);
                    ft_create.commit();

                    return true;

                case R.id.navigation_my_account:
                    Fragment fragment_privacy = new My_Account_Fragment();
                    FragmentTransaction ft_p = getSupportFragmentManager().beginTransaction();
                    ft_p.replace(R.id.frame, fragment_privacy);
                    ft_p.addToBackStack(null);
                    ft_p.commit();

                    return true;

                case R.id.navigation_contact_us:
                    Fragment fragment = new ContactUs_Fragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame, fragment);
                     ft.addToBackStack(null);
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
        utilities = Utilities.getInstance(this);
        navView = findViewById(R.id.nav_view);
        tv_main_header = findViewById(R.id.tv_main_header);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        session = new SessionManager(MainActivity.this);
        Customer_Id = session.getUser().getCustomerId();
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

        if (Connectivity.isConnected(MainActivity.this)) {
            getCartCount();
        } else {
            //Toast.makeText(getActivity(), "Please check Internet", Toast.LENGTH_SHORT).show();
            utilities.dialogOK(MainActivity.this, getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
        }

        //LayoutInflater inflater = getLayoutInflater();
      //  View listHeaderView = inflater.inflate(R.layout.nav_header, null, false);
      //  mExpandableListView.addHeaderView(listHeaderView);
       // mExpandableListView.addFooterView(listHeaderView);

        // View headerview = mExpandableListView.getHeaderView(0);
        TextView nav_tv_blog = findViewById(R.id.tv_blog);
        nav_tv_blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Blog_Fragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
        });



        if (session.getCategoryData()!=null && !session.getCategoryData().isEmpty()){
            mExpandableListTitle = new ArrayList<String>();
            mExpandableListData = new HashMap<String, List<CategoryChild>>();

            for (int i=0; i<session.getCategoryData().size(); i++){
                mExpandableListTitle.add(session.getCategoryData().get(i).getName());

            }

            for (int i=0; i<mExpandableListTitle.size(); i++){
                // List<String> colors = new ArrayList<String>();
                List<CategoryChild> colors = new ArrayList<CategoryChild>();
                for (int j=0; j<session.getCategoryData().get(i).getChildren().size(); j++){
                    // colors.add(response.getCategories().get(i).getChildren().get(j).getName());
                    colors=session.getCategoryData().get(i).getChildren();

                }
                mExpandableListData.put(mExpandableListTitle.get(i),
                        colors);

            }

            mExpandableListAdapter = new CustomExpandableListAdapter(MainActivity.this, mExpandableListTitle, mExpandableListData);
            mExpandableListView.setAdapter(mExpandableListAdapter);

        }else {
            if (Connectivity.isConnected(this)){
                Category_SubCategoryData();
            }else {
                utilities.dialogOK(this, getString(R.string.validation_title), getString(R.string.please_check_internet), getString(R.string.ok), false);
            }
        }



        addDrawerItems();
        setupDrawer();

        if (savedInstanceState == null) {
              //selectFirstItemAsDefault();
        }

    }

    @SuppressLint("CheckResult")
    private void getCartCount() {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // progressDialog.setCancelable(false);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(BaseUrl).create(Api_Call.class);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("customer_id", Customer_Id);

        apiInterface.CartCount(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SimpleResultModel>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onNext(SimpleResultModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_add_cart", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getStatus()) {
                                // utilities.dialogOK(getActivity(), "","Success: You have added to your wish list", getString(R.string.ok), false);
                                CountCart(response.getCartCount().toString());
                            } else {

                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        //Handle error
                        progressDialog.dismiss();
                        Log.e("product_details_error", e.toString());

                        if (e instanceof HttpException) {
                            int code = ((HttpException) e).code();
                            switch (code) {
                                case 403:
                                    break;
                                case 404:
                                    //Toast.makeText(EmailSignupActivity.this, R.string.email_already_use, Toast.LENGTH_SHORT).show();
                                    break;
                                case 409:
                                    break;
                                default:
                                    // Toast.makeText(EmailSignupActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } else {
                            if (TextUtils.isEmpty(e.getMessage())) {
                                // Toast.makeText(EmailSignupActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(EmailSignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                    }
                });

    }

    @SuppressLint("CheckResult")
    private void Category_SubCategoryData() {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, R.style.MyGravity);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        Api_Call apiInterface = RxApiClient.getClient(Base_Url.BaseUrl).create(Api_Call.class);

        apiInterface.CategoryApi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CategoryModel>() {
                    @Override
                    public void onNext(CategoryModel response) {
                        //Handle logic
                        try {
                            progressDialog.dismiss();
                            Log.e("result_my_test", "" + response.getMsg());
                            //Toast.makeText(EmailSignupActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.getResult().equalsIgnoreCase("true")) {
                                session.setCategoryData(response.getCategories());

                                mExpandableListTitle = new ArrayList<String>();
                                mExpandableListData = new HashMap<String, List<CategoryChild>>();

                                for (int i=0; i<response.getCategories().size(); i++){
                                    mExpandableListTitle.add(response.getCategories().get(i).getName());

                                }


                                for (int i=0; i<mExpandableListTitle.size(); i++){
                                   // List<String> colors = new ArrayList<String>();
                                    List<CategoryChild> colors = new ArrayList<CategoryChild>();
                                    for (int j=0; j<response.getCategories().get(i).getChildren().size(); j++){
                                     // colors.add(response.getCategories().get(i).getChildren().get(j).getName());
                                      colors=response.getCategories().get(i).getChildren();

                                    }
                                    mExpandableListData.put(mExpandableListTitle.get(i),
                                            colors);

                                }

                                mExpandableListAdapter = new CustomExpandableListAdapter(MainActivity.this, mExpandableListTitle, mExpandableListData);
                                mExpandableListView.setAdapter(mExpandableListAdapter);

                            } else {
                                //Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
                            }


                        } catch (Exception e) {
                            progressDialog.dismiss();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        //Handle error
                        progressDialog.dismiss();
                        Log.e("mr_product_error", e.toString());

                        if (e instanceof HttpException) {
                            int code = ((HttpException) e).code();
                            switch (code) {
                                case 403:
                                    break;
                                case 404:
                                    //Toast.makeText(EmailSignupActivity.this, R.string.email_already_use, Toast.LENGTH_SHORT).show();
                                    break;
                                case 409:
                                    break;
                                default:
                                    // Toast.makeText(EmailSignupActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } else {
                            if (TextUtils.isEmpty(e.getMessage())) {
                                // Toast.makeText(EmailSignupActivity.this, R.string.network_failure, Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(EmailSignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                    }
                });

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

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else if (myFragment != null && myFragment.isVisible()) {
            // add your code here
            //finish();
            Exit();
        } else {
            super.onBackPressed();
        }

    }

    private void Exit() {
            new AlertDialog.Builder(this).setTitle(getString(R.string.app_name))
                    .setMessage("Are you sure you want to exit the app!")
                    .setIcon((int) R.drawable.logo)
                    .setPositiveButton(getResources().getString(R.string.yes), new C03424())
                    .setNegativeButton(getResources().getString(R.string.no), new C03435()).show();

    }

    public void Update_header(String title) {
        tv_main_header.setText(title);
    }
    public void CheckBottom(int pos) {
        navView.getMenu().getItem(pos).setChecked(true);
    }

    public void CountCart(String pos) {
        bottomNavigationMenuView =
                (BottomNavigationMenuView) navView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(2);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        View badge = LayoutInflater.from(this)
                .inflate(R.layout.cart_count, bottomNavigationMenuView, false);
        tv_budge = badge.findViewById(R.id.notification_badge);
       // tv.setText(" ");
        tv_budge.setText(pos);
        itemView.addView(badge);

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

                String selectedID =  mExpandableListData.get(mExpandableListTitle.get(groupPosition))
                        .get(childPosition).getCategory_id();

                mNavigationManager.showFragmentAction(selectedID);
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

    public void fn_selectedPosition(int group, int child) {

        All_Product_Fragment homefragment = new All_Product_Fragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.frame, homefragment, "All_Product_Fragment");
        ft.addToBackStack(null);
        ft.commit();


        mDrawerLayout.closeDrawer(Gravity.LEFT);

    }

    class C03424 implements DialogInterface.OnClickListener {
        C03424() {
        }

        public void onClick(DialogInterface dialog, int which) {
            finish();
        }
    }

    class C03435 implements DialogInterface.OnClickListener {
        C03435() {
        }

        public void onClick(DialogInterface dialog, int which) {
//            drawer.closeDrawer(GravityCompat.START);
        }
    }
}
