package com.grocery.gtohome.activity.login_signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.databinding.ActivityRegisterSuccessBinding;

public class Register_Success_Activity extends AppCompatActivity {
    ActivityRegisterSuccessBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_register__success_);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_register__success_);

        binding.tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Register_Success_Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
