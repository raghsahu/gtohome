package com.grocery.gtohome.model.shipping_method;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raghvendra Sahu on 04-May-20.
 */
public class ShippingQuoteFlat {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("cost")
    @Expose
    private String cost;
    @SerializedName("tax_class_id")
    @Expose
    private String taxClassId;
    @SerializedName("text")
    @Expose
    private String text;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getTaxClassId() {
        return taxClassId;
    }

    public void setTaxClassId(String taxClassId) {
        this.taxClassId = taxClassId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
