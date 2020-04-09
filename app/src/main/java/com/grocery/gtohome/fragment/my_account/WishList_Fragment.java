package com.grocery.gtohome.fragment.my_account;

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
import com.grocery.gtohome.adapter.AddressBook_Adapter;
import com.grocery.gtohome.adapter.WishList_Adapter;
import com.grocery.gtohome.databinding.FragmentMyAccountBinding;
import com.grocery.gtohome.databinding.FragmentWishlistBinding;
import com.grocery.gtohome.model.SampleModel;

import java.util.ArrayList;

/**
 * Created by Raghvendra Sahu on 09-Apr-20.
 */
public class WishList_Fragment extends Fragment {
    FragmentWishlistBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wishlist, container, false);
        View root = binding.getRoot();
        try {
            ((MainActivity) getActivity()).Update_header(getString(R.string.my_wishlist));
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

        WishList_Adapter friendsAdapter = new WishList_Adapter(sampleModels,getActivity());
        binding.setWishlistAdapter(friendsAdapter);//set databinding adapter
        friendsAdapter.notifyDataSetChanged();
    }
}
