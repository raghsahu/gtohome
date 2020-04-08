package com.grocery.gtohome.model;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by Raghvendra Sahu on 08-Apr-20.
 */
public class SampleModel {
    String product_name;
    String price;
    int image;

    public SampleModel(String product_name, String price, int image) {
        this.product_name = product_name;
        this.price = price;
        this.image = image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @BindingAdapter("postImage")
    public static void loadImage(ImageView view, int imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                // .placeholder()
                .apply(new RequestOptions())
                .into(view);
    }

}
