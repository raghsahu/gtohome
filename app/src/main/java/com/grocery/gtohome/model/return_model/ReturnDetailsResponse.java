package com.grocery.gtohome.model.return_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.grocery.gtohome.model.order_history.HistoryItem;

import java.util.List;

public class ReturnDetailsResponse {
    @SerializedName("return_id")
    @Expose
    private String returnId;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("date_ordered")
    @Expose
    private String dateOrdered;
    @SerializedName("date_added")
    @Expose
    private String dateAdded;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("telephone")
    @Expose
    private String telephone;
    @SerializedName("product")
    @Expose
    private String product;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("opened")
    @Expose
    private String opened;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("histories")
    @Expose
    private List<HistoryItem> histories = null;

    public String getReturnId() {
        return returnId;
    }

    public void setReturnId(String returnId) {
        this.returnId = returnId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDateOrdered() {
        return dateOrdered;
    }

    public void setDateOrdered(String dateOrdered) {
        this.dateOrdered = dateOrdered;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getOpened() {
        return opened;
    }

    public void setOpened(String opened) {
        this.opened = opened;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<HistoryItem> getHistories() {
        return histories;
    }

    public void setHistories(List<HistoryItem> histories) {
        this.histories = histories;
    }

}
