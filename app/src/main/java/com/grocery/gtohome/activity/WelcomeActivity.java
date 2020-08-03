package com.grocery.gtohome.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.login_signup.Login_Activity;
import com.grocery.gtohome.adapter.SliderAdapter;
import com.grocery.gtohome.model.WelcomeModel;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout linearLayout;
    private SliderAdapter sliderAdapter;
    private int dotsCount;
    private ImageView[] dotes;
    private TextView tv_next,tv_skip;
    private int currentPage=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        viewPager = findViewById(R.id.slider_pager);
        linearLayout = findViewById(R.id.linear_layout);
        tv_next = findViewById(R.id.tv_next);
        tv_skip = findViewById(R.id.tv_skip);

        SetWelcomeContent();

        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPage+1==dotsCount){
                    Intent intent = new Intent(WelcomeActivity.this, Login_Activity.class);
                    startActivity(intent);
                    finish();
                }else {
                    viewPager.setCurrentItem(currentPage+1, true);
                }

            }
        });

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, Login_Activity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void SetWelcomeContent() {
        ArrayList<WelcomeModel> welcome_array = new ArrayList<>();

        welcome_array.add(new WelcomeModel("",R.drawable.welcome1));
        welcome_array.add(new WelcomeModel("",R.drawable.welcome2));
        welcome_array.add(new WelcomeModel("",R.drawable.welcome3));
        welcome_array.add(new WelcomeModel("",R.drawable.welcome4));
        welcome_array.add(new WelcomeModel("",R.drawable.welcome5));
        welcome_array.add(new WelcomeModel("",R.drawable.welcome6));

        //dotesIndicater(0);
        sliderAdapter = new SliderAdapter(WelcomeActivity.this,welcome_array);
        viewPager.setAdapter(sliderAdapter);
        viewPager.setPageTransformer(true, new ZoomOutSlideTransformer());
        viewPager.setCurrentItem(currentPage, true);
        viewPager.addOnPageChangeListener(pageChangeListener);
        dotesIndicater();


       // Handler handler = new Handler();

//        Runnable update = new Runnable() {
//            public void run() {
//                if (currentPage == 6) {
//                    currentPage = 6;
//                }
//                viewPager.setCurrentItem(currentPage++, true);
//            }
//        };

//        new Timer().schedule(new TimerTask() {
//
//            @Override
//            public void run() {
//                handler.post(update);
//            }
//        }, 1000*2, 1000*2);
    }


    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            for (int i = 0; i < dotsCount; i++) {
                dotes[i].setImageResource(R.drawable.circle_inactive);
            }

            dotes[position].setImageResource(R.drawable.circle_active);

//            if (position + 1 == dotsCount) {
//                tv_skip.setVisibility(View.VISIBLE);
//
//            } else {
//                tv_skip.setVisibility(View.INVISIBLE);
//
//            }

            //******************************
            if (position==0){
                currentPage=0;
            }else if (position==1){
                currentPage=1;
            }else if (position==2){
                currentPage=2;
            }else if (position==3){
                currentPage=3;
            }else if (position==4){
                currentPage=4;
            }else if (position==5){
                currentPage=5;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @SuppressLint("ClickableViewAccessibility")
    public void dotesIndicater() {
        dotsCount = sliderAdapter.getCount();
        dotes = new ImageView[dotsCount];
        linearLayout.removeAllViews();
        for (int i = 0; i < dotsCount; i++) {
            dotes[i] = new ImageView(this);
            dotes[i].setImageResource(R.drawable.circle_inactive);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    20,
                    20
            );

            params.setMargins(4, 0, 4, 0);

            final int presentPosition = i;
            dotes[presentPosition].setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    viewPager.setCurrentItem(presentPosition);
                    return true;
                }

            });


            linearLayout.addView(dotes[i], params);

        }
        dotes[0].setImageResource(R.drawable.circle_active);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}