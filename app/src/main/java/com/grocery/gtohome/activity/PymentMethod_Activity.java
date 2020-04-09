package com.grocery.gtohome.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.grocery.gtohome.R;
import com.grocery.gtohome.databinding.ActivityPymentMethodBinding;

public class PymentMethod_Activity extends AppCompatActivity {
    ActivityPymentMethodBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_pyment_method_);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pyment_method_);

        binding.toolbar.tvToolbar.setText("Payment Method");

        binding.toolbar.imgBack.setOnClickListener(new View.OnClickListener() {
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
