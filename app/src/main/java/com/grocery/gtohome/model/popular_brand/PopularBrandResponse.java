package com.grocery.gtohome.model.popular_brand;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PopularBrandResponse {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("banner_id")
    @Expose
    private String bannerId;
    @SerializedName("width")
    @Expose
    private String width;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("banners")
    @Expose
    private List<PopularBanner> banners = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
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

    public List<PopularBanner> getBanners() {
        return banners;
    }

    public void setBanners(List<PopularBanner> banners) {
        this.banners = banners;
    }

}
