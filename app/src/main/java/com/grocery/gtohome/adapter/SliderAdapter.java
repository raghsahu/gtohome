package com.grocery.gtohome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.grocery.gtohome.R;
import com.grocery.gtohome.model.WelcomeModel;

import java.util.ArrayList;

public class SliderAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    ArrayList<WelcomeModel> listarray;

    public SliderAdapter(Context context) {
        this.context = context;
    }
    public SliderAdapter(Context context, ArrayList<WelcomeModel> listarray1) {
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
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.list_item_welcome, container, false);

        ImageView imageView = view.findViewById(R.id.iv_icon);
       // TextView tv_test = view.findViewById(R.id.tv_test);

       // tv_test.setText(listarray.get(position).getName());
        // imageView.setImageResource(sliderImage[position]);
        Glide
                .with(context)
                .load(listarray.get(position).getImage())
               // .centerCrop()
                // .placeholder(R.drawable.loading_spinner)
                .into(imageView);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
