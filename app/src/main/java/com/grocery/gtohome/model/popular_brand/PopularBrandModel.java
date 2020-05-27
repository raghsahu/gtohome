package com.grocery.gtohome.model.popular_brand;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PopularBrandModel {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("response")
    @Expose
    private PopularBrandResponse response;

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

    public PopularBrandResponse getResponse() {
        return response;
    }

    public void setResponse(PopularBrandResponse response) {
        this.response = response;
    }

}
