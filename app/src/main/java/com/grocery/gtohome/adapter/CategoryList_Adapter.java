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
import com.grocery.gtohome.databinding.HomeCategoryItemBinding;
import com.grocery.gtohome.model.category_model.CategoryName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raghvendra Sahu on 21-Apr-20.
 */
public class CategoryList_Adapter extends RecyclerView.Adapter<CategoryList_Adapter.ViewHolder> {

    private List<CategoryName> dataModelList;
    Context context;


    public CategoryList_Adapter(List<CategoryName> dataModelList, Context ctx) {
        this.dataModelList = dataModelList;
        context = ctx;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HomeCategoryItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.home_category_item, parent, false);

        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CategoryName dataModel = dataModelList.get(position);
        holder.bind(dataModel);
        holder.itemRowBinding.setModel(dataModel);
        // holder.itemRowBinding.setItemClickListener(this);


        FruitVeg_Adapter friendsAdapter = new FruitVeg_Adapter(dataModel.getChildren(),context);
        holder.itemRowBinding.setFruitvegAdapter(friendsAdapter);//set databinding adapter
        friendsAdapter.notifyDataSetChanged();


    }

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public HomeCategoryItemBinding itemRowBinding;

        public ViewHolder(HomeCategoryItemBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(Object obj) {
            itemRowBinding.setVariable(BR.model, obj);
            itemRowBinding.executePendingBindings();
        }
    }
}
