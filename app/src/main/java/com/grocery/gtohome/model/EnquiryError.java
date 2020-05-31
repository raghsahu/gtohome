package com.grocery.gtohome.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EnquiryError {
    @SerializedName("enquiry")
    @Expose
    private String enquiry;

    public String getEnquiry() {
        return enquiry;
    }

    public void setEnquiry(String enquiry) {
        this.enquiry = enquiry;
    }
}
