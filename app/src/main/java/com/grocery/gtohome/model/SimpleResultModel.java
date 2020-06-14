package com.grocery.gtohome.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raghvendra Sahu on 21-Apr-20.
 */
public class SimpleResultModel {
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("cartCount")
    @Expose
    private Integer cartCount;

    @SerializedName("error")
    @Expose
    private EnquiryError error;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getCartCount() {
        return cartCount;
    }

    public void setCartCount(Integer cartCount) {
        this.cartCount = cartCount;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public EnquiryError getError() {
        return error;
    }

    public void setError(EnquiryError error) {
        this.error = error;
    }
}
