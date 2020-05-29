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
import com.grocery.gtohome.databinding.MyOrderListBinding;
import com.grocery.gtohome.databinding.MyReturnListBinding;
import com.grocery.gtohome.fragment.my_orders.OrderHistoryDetailsFragment;
import com.grocery.gtohome.fragment.my_orders.ReturnDetailsFragment;
import com.grocery.gtohome.model.order_history.OrderHistoryData;
import com.grocery.gtohome.model.return_model.ReturnData;

import java.util.List;

public class MyReturn_Adapter extends RecyclerView.Adapter<MyReturn_Adapter.ViewHolder> {

    private List<ReturnData> dataModelList;
    Context context;

    public MyReturn_Adapter(List<ReturnData> dataModelList, Context ctx) {
        this.dataModelList = dataModelList;
        context = ctx;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyReturnListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.my_return_list, parent, false);

        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReturnData dataModel = dataModelList.get(position);
        holder.bind(dataModel);
        holder.itemRowBinding.setModel(dataModel);
        // holder.itemRowBinding.setItemClickListener(this);

        holder.itemRowBinding.llOrderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ReturnDetailsFragment fragment2 = new ReturnDetailsFragment();
                Bundle bundle = new Bundle();
                // bundle.putSerializable("MyPhotoModelResponse", dataModelList.get(position));
                bundle.putString("Return_Id",dataModel.getReturnId());
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
        public MyReturnListBinding itemRowBinding;

        public ViewHolder(MyReturnListBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(Object obj) {
            itemRowBinding.setVariable(BR.model, obj);
            itemRowBinding.executePendingBindings();
        }
    }
}
