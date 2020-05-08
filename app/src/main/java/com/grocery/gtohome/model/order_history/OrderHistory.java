package com.grocery.gtohome.model.order_history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 06-May-20.
 */
public class OrderHistory {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("orders")
    @Expose
    private List<OrderHistoryData> orders = null;

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

    public List<OrderHistoryData> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderHistoryData> orders) {
        this.orders = orders;
    }
}
