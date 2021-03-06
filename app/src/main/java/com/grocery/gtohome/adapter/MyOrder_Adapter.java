package com.grocery.gtohome.adapter;

import android.content.Context;
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
import com.grocery.gtohome.databinding.AddressListBinding;
import com.grocery.gtohome.databinding.MyOrderListBinding;
import com.grocery.gtohome.fragment.my_orders.OrderHistoryDetailsFragment;
import com.grocery.gtohome.model.SampleModel;
import com.grocery.gtohome.model.order_history.OrderHistoryData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raghvendra Sahu on 09-Apr-20.
 */
public class MyOrder_Adapter extends RecyclerView.Adapter<MyOrder_Adapter.ViewHolder> {

    private List<OrderHistoryData> dataModelList;
    Context context;


    public MyOrder_Adapter(List<OrderHistoryData> dataModelList, Context ctx) {
        this.dataModelList = dataModelList;
        context = ctx;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyOrderListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.my_order_list, parent, false);

        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderHistoryData dataModel = dataModelList.get(position);
        holder.bind(dataModel);
        holder.itemRowBinding.setModel(dataModel);
        // holder.itemRowBinding.setItemClickListener(this);

         holder.itemRowBinding.llOrderDetails.setOnClickListener(new View.OnClickListener() {
         @Override
          public void onClick(View v) {

                OrderHistoryDetailsFragment fragment2 = new OrderHistoryDetailsFragment();
                Bundle bundle = new Bundle();
                // bundle.putSerializable("MyPhotoModelResponse", dataModelList.get(position));
                 bundle.putString("Order_Id",dataModel.getOrderId());
                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                fragment2.setArguments(bundle);
         }
         });
    }

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public MyOrderListBinding itemRowBinding;

        public ViewHolder(MyOrderListBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(Object obj) {
            itemRowBinding.setVariable(BR.model, obj);
            itemRowBinding.executePendingBindings();
        }
    }
}
