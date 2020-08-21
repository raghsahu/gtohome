package com.grocery.gtohome.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.grocery.gtohome.BR;
import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.DeliveryAreaActivity;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.activity.login_signup.Login_Activity;
import com.grocery.gtohome.databinding.AreaListBinding;
import com.grocery.gtohome.databinding.MyOrderListBinding;
import com.grocery.gtohome.fragment.my_orders.OrderHistoryDetailsFragment;
import com.grocery.gtohome.model.DeliveryAreaModel;
import com.grocery.gtohome.model.order_history.OrderHistoryData;
import com.grocery.gtohome.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class DeliveryArea_Adapter extends RecyclerView.Adapter<DeliveryArea_Adapter.ViewHolder> {

    private List<DeliveryAreaModel> dataModelList;
    Context context;
    SessionManager session;

    public DeliveryArea_Adapter(ArrayList<DeliveryAreaModel> dataModelList, Context ctx) {
        this.dataModelList = dataModelList;
        context = ctx;
        session = new SessionManager(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AreaListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.area_list, parent, false);

        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DeliveryAreaModel dataModel = dataModelList.get(position);
        holder.bind(dataModel);
        holder.itemRowBinding.setModel(dataModel);
        // holder.itemRowBinding.setItemClickListener(this);

        holder.itemRowBinding.cardArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemRowBinding.cardArea.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));

                session.setArea(dataModel.getDelivery_area());

               // session.createSession(response.getCustomerInfo());
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                ((AppCompatActivity)context).finish();

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public AreaListBinding itemRowBinding;

        public ViewHolder(AreaListBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(Object obj) {
            itemRowBinding.setVariable(BR.model, obj);
            itemRowBinding.executePendingBindings();
        }
    }
}
