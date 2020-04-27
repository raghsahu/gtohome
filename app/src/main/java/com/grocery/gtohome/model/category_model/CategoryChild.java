package com.grocery.gtohome.model.category_model;

import android.widget.ImageView;

import androidx.databinding.BaseObservable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.grocery.gtohome.R;

/**
 * Created by Raghvendra Sahu on 21-Apr-20.
 */
public class CategoryChild extends BaseObservable {
    @SerializedName("category_id")
    @Expose
    private String category_id;
    @SerializedName("link")
    @Expose
    private String link;

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("product_count")
    @Expose
    private String productCount;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getProductCount() {
        return productCount;
    }

    public void setProductCount(String productCount) {
        this.productCount = productCount;
    }

    public String getImageFullPath() {
        return getImage();
    }


    @BindingAdapter("postImage")
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                 .placeholder(R.drawable.placeholder_image)
                //.error(android.R.drawable.placeholder_image)
                .apply(new RequestOptions())
                .into(view);
    }

}
