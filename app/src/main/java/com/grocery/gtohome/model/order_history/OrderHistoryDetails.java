package com.grocery.gtohome.model.order_history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raghvendra Sahu on 06-May-20.
 */
public class OrderHistoryDetails {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("order")
    @Expose
    private OrderDetails order;

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

    public OrderDetails getOrder() {
        return order;
    }

    public void setOrder(OrderDetails order) {
        this.order = order;
    }
}
