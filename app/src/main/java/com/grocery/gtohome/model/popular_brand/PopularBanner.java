package com.grocery.gtohome.model.popular_brand;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PopularBanner {

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
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("manufacturer_id")
    @Expose
    private String manufacturer_id;
    @SerializedName("image")
    @Expose
    private String image;

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

    public String getManufacturer_id() {
        return manufacturer_id;
    }

    public void setManufacturer_id(String manufacturer_id) {
        this.manufacturer_id = manufacturer_id;
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
