package com.grocery.gtohome.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.grocery.gtohome.R;
import com.grocery.gtohome.model.return_reason_model.ReturnReason;

import java.util.ArrayList;
import java.util.List;

public class ReturnReasonAdapter extends RecyclerView.Adapter<ReturnReasonAdapter.ViewHolder> {

    private List<ReturnReason> offersList;
    private Context context;

    private int lastSelectedPosition = -1;
    public String returnId;

    public ReturnReasonAdapter(Context ctx, List<ReturnReason> offersListIn) {
        offersList = offersListIn;
        context = ctx;
    }

    @Override
    public ReturnReasonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.return_reques_item, parent, false);

        ReturnReasonAdapter.ViewHolder viewHolder = new ReturnReasonAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReturnReasonAdapter.ViewHolder holder,
                                 int position) {
        ReturnReason offersModel = offersList.get(position);
        holder.selectionState.setText(offersModel.getName());

        //since only one radio button is allowed to be selected,
        // this condition un-checks previous selections
        holder.selectionState.setChecked(lastSelectedPosition == position);

        holder.selectionState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                returnId=offersModel.getReturnReasonId();
                Log.e("return_id",returnId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return offersList.size();
    }

    public String ReturnReasonId(){
        return returnId;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RadioButton selectionState;

        public ViewHolder(View view) {
            super(view);
            selectionState = (RadioButton) view.findViewById(R.id.radioButton1);

            selectionState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();


                }
            });
        }
    }
}
