package com.grocery.gtohome.model.wishlist_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 10-May-20.
 */
public class Wishlist_Model {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("wishlist")
    @Expose
    private List<Wishlist_Data> wishlist = null;

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

    public List<Wishlist_Data> getWishlist() {
        return wishlist;
    }

    public void setWishlist(List<Wishlist_Data> wishlist) {
        this.wishlist = wishlist;
    }
}
