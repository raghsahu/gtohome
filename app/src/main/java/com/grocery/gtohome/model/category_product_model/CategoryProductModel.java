package com.grocery.gtohome.model.category_product_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 26-Apr-20.
 */
public class CategoryProductModel {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("products")
    @Expose
    private List<CategoryProduct_List> products = null;

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

    public List<CategoryProduct_List> getProducts() {
        return products;
    }

    public void setProducts(List<CategoryProduct_List> products) {
        this.products = products;
    }
}
