package com.grocery.gtohome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.grocery.gtohome.BR;
import com.grocery.gtohome.R;
import com.grocery.gtohome.databinding.FeatureProductListBinding;
import com.grocery.gtohome.databinding.PopularBrandListBinding;
import com.grocery.gtohome.model.SampleModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raghvendra Sahu on 09-Apr-20.
 */
public class PopularBrand_Adapter extends RecyclerView.Adapter<PopularBrand_Adapter.ViewHolder> {

    private List<SampleModel> dataModelList;
    Context context;


    public PopularBrand_Adapter(ArrayList<SampleModel> dataModelList, Context ctx) {
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
        SampleModel dataModel = dataModelList.get(position);
        holder.bind(dataModel);
        holder.itemRowBinding.setModel(dataModel);
        // holder.itemRowBinding.setItemClickListener(this);

        holder.itemRowBinding.llVeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                TravelingFragment fragment2 = new TravelingFragment();
//                Bundle bundle = new Bundle();
//                // bundle.putSerializable("MyPhotoModelResponse", dataModelList.get(position));
//                // bundle.putString("Type","Photo");
//                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = manager.beginTransaction();
//                fragmentTransaction.replace(R.id.frame, fragment2);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//                fragment2.setArguments(bundle);
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
