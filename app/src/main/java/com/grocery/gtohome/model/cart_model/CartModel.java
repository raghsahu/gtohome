package com.grocery.gtohome.model.cart_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 28-Apr-20.
 */
public class CartModel {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("cartCount")
    @Expose
    private Integer cartCount;
    @SerializedName("products")
    @Expose
    private List<CartProduct> products = null;
    @SerializedName("vouchers")
    @Expose
    private List<Object> vouchers = null;
    @SerializedName("totals")
    @Expose
    private List<CartTotal> totals = null;

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

    public Integer getCartCount() {
        return cartCount;
    }

    public void setCartCount(Integer cartCount) {
        this.cartCount = cartCount;
    }

    public List<CartProduct> getProducts() {
        return products;
    }

    public void setProducts(List<CartProduct> products) {
        this.products = products;
    }

    public List<Object> getVouchers() {
        return vouchers;
    }

    public void setVouchers(List<Object> vouchers) {
        this.vouchers = vouchers;
    }

    public List<CartTotal> getTotals() {
        return totals;
    }

    public void setTotals(List<CartTotal> totals) {
        this.totals = totals;
    }
}
