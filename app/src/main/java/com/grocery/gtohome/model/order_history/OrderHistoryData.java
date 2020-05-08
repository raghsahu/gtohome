package com.grocery.gtohome.model.order_history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raghvendra Sahu on 06-May-20.
 */
public class OrderHistoryData {
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("date_added")
    @Expose
    private String dateAdded;
    @SerializedName("products")
    @Expose
    private Integer products;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("view")
    @Expose
    private String view;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Integer getProducts() {
        return products;
    }

    public void setProducts(Integer products) {
        this.products = products;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }
}
