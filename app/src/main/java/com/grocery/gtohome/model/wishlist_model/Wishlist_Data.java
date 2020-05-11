package com.grocery.gtohome.model.wishlist_model;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raghvendra Sahu on 10-May-20.
 */
public class Wishlist_Data {

    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("stock")
    @Expose
    private String stock;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("special")
    @Expose
    private Boolean special;
    @SerializedName("href")
    @Expose
    private String href;
    @SerializedName("remove")
    @Expose
    private String remove;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Boolean getSpecial() {
        return special;
    }

    public void setSpecial(Boolean special) {
        this.special = special;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getRemove() {
        return remove;
    }

    public void setRemove(String remove) {
        this.remove = remove;
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
