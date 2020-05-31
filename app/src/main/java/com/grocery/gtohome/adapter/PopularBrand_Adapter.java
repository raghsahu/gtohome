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
import com.grocery.gtohome.databinding.FeatureProductListBinding;
import com.grocery.gtohome.databinding.PopularBrandListBinding;
import com.grocery.gtohome.fragment.All_Product_Fragment;
import com.grocery.gtohome.model.SampleModel;
import com.grocery.gtohome.model.popular_brand.PopularBanner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raghvendra Sahu on 09-Apr-20.
 */
public class PopularBrand_Adapter extends RecyclerView.Adapter<PopularBrand_Adapter.ViewHolder> {

    private List<PopularBanner> dataModelList;
    Context context;


    public PopularBrand_Adapter(List<PopularBanner> dataModelList, Context ctx) {
        this.dataModelList = dataModelList;
        context = ctx;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PopularBrandListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.popular_brand_list, parent, false);

        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PopularBanner dataModel = dataModelList.get(position);
        holder.bind(dataModel);
        holder.itemRowBinding.setModel(dataModel);
        // holder.itemRowBinding.setItemClickListener(this);

        holder.itemRowBinding.llBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataModel.getCategoryId()!=null && !dataModel.getCategoryId().equalsIgnoreCase("")){

                    All_Product_Fragment fragment2 = new All_Product_Fragment();
                    Bundle bundle = new Bundle();
                    // bundle.putSerializable("MyPhotoModelResponse", dataModelList.get(position));
                    bundle.putString("SubCategory_Id",dataModel.getCategoryId());
                    bundle.putString("manufacturer_id",dataModel.getManufacturer_id());
                    FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = manager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    fragment2.setArguments(bundle);

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public PopularBrandListBinding itemRowBinding;

        public ViewHolder(PopularBrandListBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(Object obj) {
            itemRowBinding.setVariable(BR.model, obj);
            itemRowBinding.executePendingBindings();
        }
    }
}
