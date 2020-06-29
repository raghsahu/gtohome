package com.grocery.gtohome.model.forgot_pw_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyEmailOtp {
    @SerializedName("statusotp")
    @Expose
    private Integer statusotp;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Integer getStatusotp() {
        return statusotp;
    }

    public void setStatusotp(Integer statusotp) {
        this.statusotp = statusotp;
    }

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
}
