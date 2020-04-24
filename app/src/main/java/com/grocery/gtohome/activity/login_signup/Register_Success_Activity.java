package com.grocery.gtohome.activity.login_signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.databinding.ActivityRegisterSuccessBinding;
import com.grocery.gtohome.model.register_model.RegistrationModelData;
import com.grocery.gtohome.session.SessionManager;

public class Register_Success_Activity extends AppCompatActivity {
    ActivityRegisterSuccessBinding binding;
    RegistrationModelData RegistrationData;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_register__success_);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_register__success_);
        session = new SessionManager(Register_Success_Activity.this);
        try {
            if (getIntent()!=null){
                RegistrationData=(RegistrationModelData) getIntent().getSerializableExtra("RegistrationData");
            }

        }catch (Exception e){
        }


        binding.tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.createSession(RegistrationData);

                Intent intent=new Intent(Register_Success_Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
