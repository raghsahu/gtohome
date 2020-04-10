package com.grocery.gtohome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.grocery.gtohome.R;
import com.grocery.gtohome.model.SliderModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Raghvendra Sahu on 10-Apr-20.
 */
public class TestimonialSliderAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    ArrayList<SliderModel> listarray;

    public TestimonialSliderAdapter(Context context, ArrayList<SliderModel> listarray1) {
        this.context = context;
        this.listarray = listarray1;
    }

//    public int[] sliderImage = {
//            R.drawable.mic_icon,
//            R.drawable.mymik,
//            R.drawable.mic_icon
//    };

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
        View view = layoutInflater.inflate(R.layout.slide_testimonials, container, false);

        CircleImageView imageView = view.findViewById(R.id.circle_imag);
        TextView tv_test = view.findViewById(R.id.tv_test);

        tv_test.setText(listarray.get(position).getDesc());
        //imageView.setImageResource(sliderImage[position]);
        Glide
                .with(context)
                .load(listarray.get(position).getSlide_image())
                .centerCrop()
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
