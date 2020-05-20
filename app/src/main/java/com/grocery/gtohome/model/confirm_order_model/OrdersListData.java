package com.grocery.gtohome.model.confirm_order_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.grocery.gtohome.model.cart_model.CartTotal;
import com.grocery.gtohome.model.order_history.OrderDetailsProduct;

import java.util.List;

public class OrdersListData {

//    @SerializedName("shipping_method")
//    @Expose
//    private ShippingMethod shippingMethod;
    @SerializedName("products")
    @Expose
    private List<OrderDetailsProduct> products = null;
    @SerializedName("totals")
    @Expose
    private List<CartTotal> totals = null;

//    public ShippingMethod getShippingMethod() {
//        return shippingMethod;
//    }
//
//    public void setShippingMethod(ShippingMethod shippingMethod) {
//        this.shippingMethod = shippingMethod;
//    }

    public List<OrderDetailsProduct> getProducts() {
        return products;
    }

    public void setProducts(List<OrderDetailsProduct> products) {
        this.products = products;
    }

    public List<CartTotal> getTotals() {
        return totals;
    }

    public void setTotals(List<CartTotal> totals) {
        this.totals = totals;
    }
}

