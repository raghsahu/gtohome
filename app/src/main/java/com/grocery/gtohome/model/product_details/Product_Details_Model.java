package com.grocery.gtohome.model.product_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 26-Apr-20.
 */
public class Product_Details_Model {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("products")
    @Expose
    private List<Product_Details_Data> products = null;

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

    public List<Product_Details_Data> getProducts() {
        return products;
    }

    public void setProducts(List<Product_Details_Data> products) {
        this.products = products;
    }
}
