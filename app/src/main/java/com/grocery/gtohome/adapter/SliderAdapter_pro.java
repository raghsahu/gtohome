package com.grocery.gtohome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.grocery.gtohome.R;
import com.grocery.gtohome.model.SliderModel;
import com.grocery.gtohome.model.product_slider.ProductBannerImage;
import com.grocery.gtohome.model.product_slider.ProductSliderData;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter_pro extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    List<ProductBannerImage> listarray;

    public SliderAdapter_pro(Context context, List<ProductBannerImage> listarray1) {
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
        View view = layoutInflater.inflate(R.layout.product_slide_image, container, false);

        ImageView imageView = view.findViewById(R.id.prod_imag);

        //tv_test.setText(listarray.get(position).getDesc());
        //imageView.setImageResource(sliderImage[position]);

        for (int i=0; i<listarray.size(); i++){

            Glide.with(context)
                    .load(listarray.get(position).getImage())
                    .centerCrop()
                    // .placeholder(R.drawable.loading_spinner)
                    .into(imageView);
        }


//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent httpIntent = new Intent(Intent.ACTION_VIEW);
//                httpIntent.setData(Uri.parse(listarray.get(position).getUrl()));
//                context.startActivity(httpIntent);
//            }
//        });


        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }


}
