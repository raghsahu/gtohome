package com.grocery.gtohome.model.product_slider;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Product_Slider_Model {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("response")
    @Expose
    private List<ProductSliderData> response = null;

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

    public List<ProductSliderData> getResponse() {
        return response;
    }

    public void setResponse(List<ProductSliderData> response) {
        this.response = response;
    }
}
