package com.grocery.gtohome.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.grocery.gtohome.R;
import com.grocery.gtohome.activity.MainActivity;
import com.grocery.gtohome.adapter.FeatureProduct_Adapter;
import com.grocery.gtohome.databinding.FragmentAllProductsBinding;
import com.grocery.gtohome.databinding.FragmentContactUsBinding;
import com.grocery.gtohome.model.SampleModel;
import com.grocery.gtohome.model.SliderModel;

import java.util.ArrayList;

/**
 * Created by Raghvendra Sahu on 10-Apr-20.
 */
public class All_Product_Fragment extends Fragment {
    FragmentAllProductsBinding binding;

    public static All_Product_Fragment newInstance(String movieTitle) {
        All_Product_Fragment fragmentAction = new All_Product_Fragment();
        Bundle args = new Bundle();
        //args.putString(KEY_MOVIE_TITLE, movieTitle);
        fragmentAction.setArguments(args);

        return fragmentAction;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_products, container, false);
        View root = binding.getRoot();
        try {
            ((MainActivity) getActivity()).Update_header(getString(R.string.all_products));
        } catch (Exception e) {
        }

        getFeaturedProductList();//fruit veg list



        return root;
    }


    private void getFeaturedProductList() {
        ArrayList<SampleModel> sampleModels=new ArrayList<>();
        sampleModels.add(new SampleModel("Apple", "20", R.drawable.apple));
        sampleModels.add(new SampleModel("Apple Gala", "100", R.drawable.gala_apple));
        sampleModels.add(new SampleModel("Banana Robest", "50", R.drawable.banana_robust));
        sampleModels.add(new SampleModel("Beans", "20", R.drawable.beans_img));
        sampleModels.add(new SampleModel("Beetroot", "20", R.drawable.beetroot));
        sampleModels.add(new SampleModel("Broad Beans", "20", R.drawable.broad_beans));

        FeatureProduct_Adapter friendsAdapter = new FeatureProduct_Adapter(sampleModels,getActivity());
        binding.setFeatureAdapter(friendsAdapter);//set databinding adapter
        friendsAdapter.notifyDataSetChanged();
    }






}
