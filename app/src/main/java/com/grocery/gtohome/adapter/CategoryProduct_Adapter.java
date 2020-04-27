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
import com.grocery.gtohome.databinding.CategoryProductListBinding;
import com.grocery.gtohome.databinding.FeatureProductListBinding;
import com.grocery.gtohome.fragment.Product_Details_Fragment;
import com.grocery.gtohome.model.SampleModel;
import com.grocery.gtohome.model.category_product_model.CategoryProduct_List;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raghvendra Sahu on 26-Apr-20.
 */
public class CategoryProduct_Adapter extends RecyclerView.Adapter<CategoryProduct_Adapter.ViewHolder> {

    private List<CategoryProduct_List> dataModelList;
    Context context;


    public CategoryProduct_Adapter(List<CategoryProduct_List> dataModelList, Context ctx) {
        this.dataModelList = dataModelList;
        context = ctx;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CategoryProductListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.category_product_list, parent, false);

        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CategoryProduct_List dataModel = dataModelList.get(position);
        holder.bind(dataModel);
        holder.itemRowBinding.setModel(dataModel);
        // holder.itemRowBinding.setItemClickListener(this);

        holder.itemRowBinding.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Product_Details_Fragment fragment2 = new Product_Details_Fragment();
                Bundle bundle = new Bundle();
                // bundle.putSerializable("MyPhotoModelResponse", dataModelList.get(position));
                 bundle.putString("Product_Id",dataModel.getProductId());
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
        public CategoryProductListBinding itemRowBinding;

        public ViewHolder(CategoryProductListBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(Object obj) {
            itemRowBinding.setVariable(BR.model, obj);
            itemRowBinding.executePendingBindings();
        }
    }
}
