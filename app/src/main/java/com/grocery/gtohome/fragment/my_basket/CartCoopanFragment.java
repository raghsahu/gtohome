package com.grocery.gtohome.fragment.my_basket;

import android.content.Intent;
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
import com.grocery.gtohome.databinding.FragmentCartCoopanBinding;

/**
 * Created by Raghvendra Sahu on 09-Apr-20.
 */
public class CartCoopanFragment extends Fragment {
    FragmentCartCoopanBinding binding;
    private String TotalPrice,SubTotal;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart_coopan, container, false);
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
            imageView.setVisibility(View.VISIBLE);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).onBackPressed();
                }
            });
        }

        if (getArguments()!=null){
            TotalPrice=getArguments().getString("TotalPrice");
            SubTotal=getArguments().getString("SubTotal");

            binding.tvSubtotal.setText(SubTotal);
            binding.tvTotal.setText(TotalPrice);
        }

        binding.tvCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DeliveryAddressFragment fragment2 = new DeliveryAddressFragment();
//                Bundle bundle = new Bundle();
//                // bundle.putSerializable("MyPhotoModelResponse", dataModelList.get(position));
//                bundle.putString("TotalPrice",TotalPrice);
//                bundle.putString("SubTotal",SubTotal);
//                FragmentManager manager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = manager.beginTransaction();
//                fragmentTransaction.replace(R.id.frame, fragment2);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//                fragment2.setArguments(bundle);
                Intent intent = new Intent(getActivity(), ChekoutActivity.class);
                intent.putExtra("TotalPrice",TotalPrice);
                intent.putExtra("SubTotal",SubTotal);
                startActivity(intent);
            }
        });





        return root;

    }
}
