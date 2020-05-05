package com.grocery.gtohome.model.shipping_method;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 04-May-20.
 */
public class ShippingMethod {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("shipping_methods")
    @Expose
    private List<ShippingMethodData> shippingMethods = null;

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

    public List<ShippingMethodData> getShippingMethods() {
        return shippingMethods;
    }

    public void setShippingMethods(List<ShippingMethodData> shippingMethods) {
        this.shippingMethods = shippingMethods;
    }
}
