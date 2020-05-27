package com.grocery.gtohome.model.home_slider;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeSliderBanner {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("response")
    @Expose
    private BannerResponse response;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public BannerResponse getResponse() {
        return response;
    }

    public void setResponse(BannerResponse response) {
        this.response = response;
    }

    public class BannerResponse {
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
        private List<HomeBannerList> banners = null;

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

        public List<HomeBannerList> getBanners() {
            return banners;
        }

        public void setBanners(List<HomeBannerList> banners) {
            this.banners = banners;
        }
    }

    public class HomeBannerList {

        @SerializedName("banner_image_id")
        @Expose
        private String bannerImageId;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("link")
        @Expose
        private String link;
        @SerializedName("category_id")
        @Expose
        private String categoryId;
        @SerializedName("image")
        @Expose
        private String image;

        public String getBannerImageId() {
            return bannerImageId;
        }

        public void setBannerImageId(String bannerImageId) {
            this.bannerImageId = bannerImageId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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
}
