package com.grocery.gtohome.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;

import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.login_signup.Register_Activity;
import com.grocery.gtohome.adapter.DeliveryArea_Adapter;
import com.grocery.gtohome.adapter.MyOrder_Adapter;
import com.grocery.gtohome.databinding.ActivityDeliveryAreaBinding;
import com.grocery.gtohome.databinding.ActivityRegisterBinding;
import com.grocery.gtohome.model.DeliveryAreaModel;
import com.grocery.gtohome.session.SessionManager;
import com.grocery.gtohome.utils.Utilities;

import java.util.ArrayList;

public class DeliveryAreaActivity extends AppCompatActivity {
    ActivityDeliveryAreaBinding binding;
    SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_delivery_area);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_delivery_area);
        session = new SessionManager(DeliveryAreaActivity.this);


        ShowDeliveryArea();

    }

    private void ShowDeliveryArea() {
        ArrayList<DeliveryAreaModel>deliveryAreas=new ArrayList<DeliveryAreaModel>();

        deliveryAreas.add(new DeliveryAreaModel("Ayanchery",""));
        deliveryAreas.add(new DeliveryAreaModel("Maakkam mukk",""));
        deliveryAreas.add(new DeliveryAreaModel("Valliyad",""));
        deliveryAreas.add(new DeliveryAreaModel("Kamicheri",""));
        deliveryAreas.add(new DeliveryAreaModel("Kakkuni",""));
        deliveryAreas.add(new DeliveryAreaModel("Theekkuni",""));
        deliveryAreas.add(new DeliveryAreaModel("Mukkeduthum vayal",""));
        deliveryAreas.add(new DeliveryAreaModel("Thulattum nada",""));
        deliveryAreas.add(new DeliveryAreaModel("Paingottai",""));
        deliveryAreas.add(new DeliveryAreaModel("Kottappally town",""));
        deliveryAreas.add(new DeliveryAreaModel("Chundekai",""));


        DeliveryArea_Adapter friendsAdapter = new DeliveryArea_Adapter(deliveryAreas,DeliveryAreaActivity.this);
        binding.setAreaAdapter(friendsAdapter);//set databinding adapter
        friendsAdapter.notifyDataSetChanged();

    }


}