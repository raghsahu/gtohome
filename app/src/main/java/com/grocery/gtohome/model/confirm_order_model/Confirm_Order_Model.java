package com.grocery.gtohome.model.confirm_order_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Confirm_Order_Model {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("orders")
    @Expose
    private OrdersListData orders;

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

    public OrdersListData getOrders() {
        return orders;
    }

    public void setOrders(OrdersListData orders) {
        this.orders = orders;
    }

}
