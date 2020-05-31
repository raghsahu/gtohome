package com.grocery.gtohome.model.login_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginError {
    @SerializedName("approved")
    @Expose
    private String approved;

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }
}
