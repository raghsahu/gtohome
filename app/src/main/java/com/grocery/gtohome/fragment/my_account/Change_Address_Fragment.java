package com.grocery.gtohome.fragment.my_account;

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
import com.grocery.gtohome.adapter.AddressBook_Adapter;
import com.grocery.gtohome.adapter.FruitVeg_Adapter;
import com.grocery.gtohome.databinding.FragmentChangeAddressBinding;
import com.grocery.gtohome.databinding.FragmentMyAccountBinding;
import com.grocery.gtohome.model.SampleModel;

import java.util.ArrayList;

/**
 * Created by Raghvendra Sahu on 09-Apr-20.
 */
public class Change_Address_Fragment extends Fragment {
    FragmentChangeAddressBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_address, container, false);
        View root = binding.getRoot();
        try {
            ((MainActivity) getActivity()).Update_header(getString(R.string.my_account));
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

        getAddressBook();

        binding.tvNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAddressFragment fragment2 = new AddAddressFragment();
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

    private void getAddressBook() {

        ArrayList<SampleModel> sampleModels=new ArrayList<>();
        sampleModels.add(new SampleModel("Apple", "20", R.drawable.apple));
        sampleModels.add(new SampleModel("Apple Gala", "100", R.drawable.gala_apple));

        AddressBook_Adapter friendsAdapter = new AddressBook_Adapter(sampleModels,getActivity());
        binding.setAddressBookAdapter(friendsAdapter);//set databinding adapter
        friendsAdapter.notifyDataSetChanged();
    }
}
