package com.grocery.gtohome.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.grocery.gtohome.R;
import com.grocery.gtohome.fragment.All_Product_Fragment;
import com.grocery.gtohome.model.SliderModel;
import com.grocery.gtohome.model.popular_brand.PopularBanner;

import java.util.ArrayList;
import java.util.List;

public class SlidePopularAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    List<PopularBanner> listarray;

    public SlidePopularAdapter(Context context, List<PopularBanner> listarray1) {
        this.context = context;
        this.listarray = listarray1;
    }


    @Override
    public int getCount() {
        return listarray.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.slide_brand, container, false);

        ImageView imageView = view.findViewById(R.id.popular_image);

        Glide
                .with(context)
                .load(listarray.get(position).getImage())
                .centerCrop()
                // .placeholder(R.drawable.loading_spinner)
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listarray.get(position).getCategoryId()!=null && !listarray.get(position).getCategoryId().equalsIgnoreCase("")){

                    All_Product_Fragment fragment2 = new All_Product_Fragment();
                    Bundle bundle = new Bundle();
                    // bundle.putSerializable("MyPhotoModelResponse", dataModelList.get(position));
                    bundle.putString("SubCategory_Id",listarray.get(position).getCategoryId());
                    bundle.putString("manufacturer_id",listarray.get(position).getManufacturer_id());
                    FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = manager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    fragment2.setArguments(bundle);

                }

            }
        });


        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public float getPageWidth(int position) {
        //return super.getPageWidth(position);
        return (0.5f);
    }
}
