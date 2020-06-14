package com.grocery.gtohome.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletUseModel {
    @SerializedName("text")
    @Expose
    private String text;
//    @SerializedName("payment_methods")
//    @Expose
//    private PaymentMethods paymentMethods;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

//    public PaymentMethods getPaymentMethods() {
//        return paymentMethods;
//    }
//
//    public void setPaymentMethods(PaymentMethods paymentMethods) {
//        this.paymentMethods = paymentMethods;
//    }
}
