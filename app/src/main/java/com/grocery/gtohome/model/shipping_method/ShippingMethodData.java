package com.grocery.gtohome.model.shipping_method;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raghvendra Sahu on 04-May-20.
 */
public class ShippingMethodData {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("quote")
    @Expose
    private ShippingQuote quote;
    @SerializedName("sort_order")
    @Expose
    private String sortOrder;
    @SerializedName("error")
    @Expose
    private Boolean error;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ShippingQuote getQuote() {
        return quote;
    }

    public void setQuote(ShippingQuote quote) {
        this.quote = quote;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

}
