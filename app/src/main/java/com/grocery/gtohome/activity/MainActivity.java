package com.grocery.gtohome.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.grocery.gtohome.R;
import com.grocery.gtohome.fragment.ContactUs_Fragment;
import com.grocery.gtohome.fragment.Home_Fragment;
import com.grocery.gtohome.fragment.my_basket.MyBasket_Fragment;
import com.grocery.gtohome.fragment.my_account.My_Account_Fragment;
import com.grocery.gtohome.fragment.Search_Fragment;

public class MainActivity extends AppCompatActivity {
    public BottomNavigationView navView;
    TextView tv_main_header;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Fragment fragment_home=new Home_Fragment();
                    FragmentTransaction ft_home = getSupportFragmentManager().beginTransaction();
                    ft_home.replace(R.id.frame, fragment_home);
                    // ft.addToBackStack(null);
                    ft_home.commit();

                    return true;
                case R.id.navigation_search:
                    Fragment fragment_faq=new Search_Fragment();
                    FragmentTransaction ft_faq = getSupportFragmentManager().beginTransaction();
                    ft_faq.replace(R.id.frame, fragment_faq);
                    // ft.addToBackStack(null);
                    ft_faq.commit();

                    return true;
                case R.id.navigation_my_basket:
                    Fragment fragment_create=new MyBasket_Fragment();
                    FragmentTransaction ft_create = getSupportFragmentManager().beginTransaction();
                    ft_create.replace(R.id.frame, fragment_create);
                    // ft.addToBackStack(null);
                    ft_create.commit();

                    return true;

                case R.id.navigation_my_account:
                    Fragment fragment_privacy=new My_Account_Fragment();
                    FragmentTransaction ft_p = getSupportFragmentManager().beginTransaction();
                    ft_p.replace(R.id.frame, fragment_privacy);
                    // ft.addToBackStack(null);
                    ft_p.commit();

                    return true;

                case R.id.navigation_contact_us:
                    Fragment fragment=new ContactUs_Fragment();
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
        tv_main_header = findViewById(R.id.tvToolbar);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //open default fragmentframe
        setHomeFragment();
        navView.getMenu().findItem(R.id.navigation_home).setChecked(true);

    }


    private void setHomeFragment() {
        Home_Fragment homefragment=new Home_Fragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.frame, homefragment,"HOME_FRAGMENT");
        ft.addToBackStack(null);
        ft.commit();
    }


    @Override
    public void onBackPressed() {
        Home_Fragment myFragment = (Home_Fragment)getSupportFragmentManager().findFragmentByTag("HOME_FRAGMENT");
        if (myFragment != null && myFragment.isVisible()) {
            // add your code here
            finish();
        }else {
            super.onBackPressed();
        }


    }

    public void Update_header(String title) {
        tv_main_header.setText(title);
    }
}
