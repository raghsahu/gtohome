package com.grocery.gtohome.model.review_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewModel {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("reviews")
    @Expose
    private List<ReviewModelData> reviews = null;

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

    public List<ReviewModelData> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewModelData> reviews) {
        this.reviews = reviews;
    }
}
