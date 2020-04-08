package com.grocery.gtohome.activity.login_signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.grocery.gtohome.R;
import com.grocery.gtohome.databinding.ActivityForgotPwBinding;

public class Forgot_Pw_Activity extends AppCompatActivity {
    ActivityForgotPwBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_forgot__pw_);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_forgot__pw_);


        binding.tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
