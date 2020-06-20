package com.grocery.gtohome.model.product_slider;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductBannerImage {
    @SerializedName("banner_image_id")
    @Expose
    private String bannerImageId;
    @SerializedName("sort_order")
    @Expose
    private String sortOrder;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("image")
    @Expose
    private String image;

    public ProductBannerImage(String image) {
        this.image=image;
    }

    public String getBannerImageId() {
        return bannerImageId;
    }

    public void setBannerImageId(String bannerImageId) {
        this.bannerImageId = bannerImageId;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
