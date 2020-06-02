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
import com.grocery.gtohome.databinding.BlogItemListBinding;
import com.grocery.gtohome.databinding.CreditItemListBinding;
import com.grocery.gtohome.fragment.nav_fragment.Blog_Details_Fragment;
import com.grocery.gtohome.model.blog_model.BlogData;
import com.grocery.gtohome.model.wallet_model.Credit_List;
import com.grocery.gtohome.session.SessionManager;
import com.grocery.gtohome.utils.Utilities;

import java.util.List;

public class CreditList_Adapter extends RecyclerView.Adapter<CreditList_Adapter.ViewHolder> {

    private List<Credit_List> dataModelList;
    Context context;
    private Utilities utilities;
    private SessionManager sessionManager;
    private String Customer_Id;


    public CreditList_Adapter(List<Credit_List> dataModelList, Context ctx) {
        this.dataModelList = dataModelList;
        context = ctx;
        utilities = Utilities.getInstance(context);
        sessionManager = new SessionManager(context);
        Customer_Id = sessionManager.getUser().getCustomerId();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CreditItemListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.credit_item_list, parent, false);

        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Credit_List dataModel = dataModelList.get(position);
        holder.bind(dataModel);
        holder.itemRowBinding.setModel(dataModel);
        // holder.itemRowBinding.setItemClickListener(this);

    }

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CreditItemListBinding itemRowBinding;

        public ViewHolder(CreditItemListBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(Object obj) {
            itemRowBinding.setVariable(BR.model, obj);
            itemRowBinding.executePendingBindings();
        }
    }
}
