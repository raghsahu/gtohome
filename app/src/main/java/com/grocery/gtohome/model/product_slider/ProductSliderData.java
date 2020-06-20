package com.grocery.gtohome.model.product_slider;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductSliderData {
    @SerializedName("banner_id")
    @Expose
    private String bannerId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("width")
    @Expose
    private String width;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("banner_images")
    @Expose
    private List<ProductBannerImage> bannerImages = null;

    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ProductBannerImage> getBannerImages() {
        return bannerImages;
    }

    public void setBannerImages(List<ProductBannerImage> bannerImages) {
        this.bannerImages = bannerImages;
    }
}
