package com.grocery.gtohome.fragment.my_orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.adapter.MyOrder_Adapter;
import com.grocery.gtohome.adapter.WishList_Adapter;
import com.grocery.gtohome.databinding.FragmentMyAccountBinding;
import com.grocery.gtohome.databinding.FragmentMyOrdersBinding;
import com.grocery.gtohome.model.SampleModel;

import java.util.ArrayList;

/**
 * Created by Raghvendra Sahu on 09-Apr-20.
 */
public class OrderHistory_Fragment extends Fragment {
    FragmentMyOrdersBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_orders, container, false);
        View root = binding.getRoot();
        try {
            ((MainActivity) getActivity()).Update_header(getString(R.string.order_history));
        } catch (Exception e) {
        }
        //onback press call
        View view = getActivity().findViewById(R.id.img_back);
        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            //Do your stuff
            imageView.setVisibility(View.VISIBLE);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).onBackPressed();
                }
            });
        }
        getWishList();
        return root;

    }


    private void getWishList() {

        ArrayList<SampleModel> sampleModels=new ArrayList<>();
        sampleModels.add(new SampleModel("Apple", "20", R.drawable.apple));
        sampleModels.add(new SampleModel("Apple Gala", "100", R.drawable.gala_apple));

        MyOrder_Adapter friendsAdapter = new MyOrder_Adapter(sampleModels,getActivity());
        binding.setMyOrderAdapter(friendsAdapter);//set databinding adapter
        friendsAdapter.notifyDataSetChanged();
    }
}
