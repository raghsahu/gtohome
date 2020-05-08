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
import com.grocery.gtohome.databinding.MyItemListBinding;
import com.grocery.gtohome.databinding.MyOrderListBinding;
import com.grocery.gtohome.fragment.my_orders.OrderHistoryDetailsFragment;
import com.grocery.gtohome.model.order_history.OrderDetailsProduct;
import com.grocery.gtohome.model.order_history.OrderHistoryData;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 06-May-20.
 */
public class HistoryItemList_Adapter extends RecyclerView.Adapter<HistoryItemList_Adapter.ViewHolder> {

    private List<OrderDetailsProduct> dataModelList;
    Context context;


    public HistoryItemList_Adapter(List<OrderDetailsProduct> dataModelList, Context ctx) {
        this.dataModelList = dataModelList;
        context = ctx;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyItemListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.my_item_list, parent, false);

        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderDetailsProduct dataModel = dataModelList.get(position);
        holder.bind(dataModel);
        holder.itemRowBinding.setModel(dataModel);
        // holder.itemRowBinding.setItemClickListener(this);



    }

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public MyItemListBinding itemRowBinding;

        public ViewHolder(MyItemListBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(Object obj) {
            itemRowBinding.setVariable(BR.model, obj);
            itemRowBinding.executePendingBindings();
        }
    }


}
