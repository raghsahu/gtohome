package com.grocery.gtohome.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.grocery.gtohome.R;
import com.grocery.gtohome.databinding.ActivityOrderSuccessBinding;

public class OrderSuccess_Activity extends AppCompatActivity {
    ActivityOrderSuccessBinding binding;
    String OrderId;
    String SuccessMsg="<p>Your order has been successfully processed!</p><p>You can view your order history by going to the <a href=\"https://gtohome.in/index.php?route=account/account\">my account</a> page and by clicking on <a href=\"https://gtohome.in/index.php?route=account/order\">history</a>.</p><p>If your purchase has an associated download, you can go to the account <a href=\"https://gtohome.in/index.php?route=account/download\">downloads</a> page to view them.</p><p>Please direct any questions you have to the <a href=\"https://gtohome.in/index.php?route=information/contact\">store owner</a>.</p><p>Thanks for shopping with us online!</p>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_success_);
        //setContentView(R.layout.activity_order_success_);
        binding.toolbar.tvToolbar.setText("Order Success");
        if (getIntent()!=null){
            OrderId=getIntent().getStringExtra("OrderId");
        }

        binding.tvOrderSuccess.setText("Your order has been successfully processed! You Order Id is "+OrderId +
                "\n Thanks for shopping with us online!");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            binding.tvOrderSuccess.setText("You Order Id is <b>"+OrderId+" </b> "+ Html.fromHtml(SuccessMsg, Html.FROM_HTML_MODE_COMPACT));
//        } else {
//            binding.tvOrderSuccess.setText("You Order Id is <b>"+OrderId+" </b> "+ Html.fromHtml(SuccessMsg));
//        }


        binding.toolbar.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(OrderSuccess_Activity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}
