package com.grocery.gtohome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.grocery.gtohome.BR;
import com.grocery.gtohome.R;
import com.grocery.gtohome.databinding.ShipingListBinding;
import com.grocery.gtohome.model.shipping_method.ShippingMethodData;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 04-May-20.
 */
public class ShippingMethodAdapter extends RecyclerView.Adapter<ShippingMethodAdapter.ViewHolder> {

    private List<ShippingMethodData> dataModelList;
    Context context;
    private int lastCheckedPosition = -1;
     String subTitle,subCode;
    AdapterCallback mAdapterCallback;

    public ShippingMethodAdapter(List<ShippingMethodData> dataModelList, Context ctx) {
        this.dataModelList = dataModelList;
        context = ctx;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ShipingListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.shiping_list, parent, false);

        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ShippingMethodData dataModel = dataModelList.get(position);
        holder.bind(dataModel);
        try {
            mAdapterCallback = ((AdapterCallback) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
        holder.itemRowBinding.setModel(dataModel);
        // holder.itemRowBinding.setItemClickListener(this);
        holder.itemRowBinding.radioShipingCharge.setChecked(position == lastCheckedPosition);

        holder.itemRowBinding.tvShippingTitle.setText(dataModel.getTitle());
        if (dataModel.getCode().equalsIgnoreCase("flat")){
            holder.itemRowBinding.radioShipingCharge.setText(dataModel.getQuote().getFlat().getTitle()+" "+
                    dataModel.getQuote().getFlat().getText());
        }else if (dataModel.getCode().equalsIgnoreCase("free")){
            holder.itemRowBinding.radioShipingCharge.setText(dataModel.getQuote().getFree().getTitle()+" "+
                    dataModel.getQuote().getFree().getText());
        }


        holder.itemRowBinding.radioShipingCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastCheckedPosition = holder.getAdapterPosition();
              //  MainTitle=dataModel.getTitle();
              //  MainCode=dataModel.getCode();

                if (dataModel.getCode().equalsIgnoreCase("flat")){
                    subTitle=dataModel.getQuote().getFlat().getTitle();
                    subCode=dataModel.getQuote().getFlat().getCode();
                }else if (dataModel.getCode().equalsIgnoreCase("free")){
                    subTitle=dataModel.getQuote().getFree().getTitle();
                    subCode=dataModel.getQuote().getFree().getCode();
                }


                mAdapterCallback.onMethodCallback(subTitle,subCode);
                notifyDataSetChanged();

            }
        });

    }

    public interface AdapterCallback {
        void onMethodCallback(String subTitle, String subCode);
    }


    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ShipingListBinding itemRowBinding;

        public ViewHolder(ShipingListBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(Object obj) {
            itemRowBinding.setVariable(BR.model, obj);
            itemRowBinding.executePendingBindings();
        }
    }


}
