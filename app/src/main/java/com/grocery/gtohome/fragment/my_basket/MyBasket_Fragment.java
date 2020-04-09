package com.grocery.gtohome.fragment.my_basket;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.adapter.Shopping_List_Adapter;
import com.grocery.gtohome.adapter.WishList_Adapter;
import com.grocery.gtohome.databinding.FragmentHomeBinding;
import com.grocery.gtohome.databinding.FragmentMyBasketBinding;
import com.grocery.gtohome.fragment.my_account.EditInfoFragment;
import com.grocery.gtohome.model.SampleModel;

import java.util.ArrayList;

/**
 * Created by Raghvendra Sahu on 08-Apr-20.
 */
public class MyBasket_Fragment extends Fragment {
    FragmentMyBasketBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_basket, container, false);
        View root = binding.getRoot();
        try {
            ((MainActivity) getActivity()).Update_header(getString(R.string.my_basket));
        } catch (Exception e) {
        }
        //onback press call
        View view = getActivity().findViewById(R.id.img_back);
        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            //Do your stuff
            imageView.setVisibility(View.GONE);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).onBackPressed();
                }
            });
        }

        getWishList();

        binding.tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartCoopanFragment fragment2 = new CartCoopanFragment();
                Bundle bundle = new Bundle();
                // bundle.putSerializable("MyPhotoModelResponse", dataModelList.get(position));
                //   bundle.putString("Title",dataModel.getCategory_name());
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                fragment2.setArguments(bundle);
            }
        });

        return root;

    }

    private void getWishList() {

        ArrayList<SampleModel> sampleModels=new ArrayList<>();
        sampleModels.add(new SampleModel("Apple", "20", R.drawable.apple));
        sampleModels.add(new SampleModel("Apple Gala", "100", R.drawable.gala_apple));

        Shopping_List_Adapter friendsAdapter = new Shopping_List_Adapter(sampleModels,getActivity());
        binding.setCartlistAdapter(friendsAdapter);//set databinding adapter
        friendsAdapter.notifyDataSetChanged();
    }
}
