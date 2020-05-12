package com.grocery.gtohome.model.shipping_method;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raghvendra Sahu on 04-May-20.
 */
public class ShippingQuote {

    @SerializedName("flat")
    @Expose
    private ShippingQuoteFlat flat;
    @SerializedName("free")
    @Expose
    private ShippingQuoteFlat free;

    public ShippingQuoteFlat getFree() {
        return free;
    }

    public void setFree(ShippingQuoteFlat free) {
        this.free = free;
    }

    public ShippingQuoteFlat getFlat() {
        return flat;
    }

    public void setFlat(ShippingQuoteFlat flat) {
        this.flat = flat;
    }
}
