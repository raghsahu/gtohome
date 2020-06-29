package com.grocery.gtohome.model.forgot_pw_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotModel {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("forgetBy")
    @Expose
    private String forgetBy;
    @SerializedName("checkstatus")
    @Expose
    private Integer checkstatus;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getForgetBy() {
        return forgetBy;
    }

    public void setForgetBy(String forgetBy) {
        this.forgetBy = forgetBy;
    }

    public Integer getCheckstatus() {
        return checkstatus;
    }

    public void setCheckstatus(Integer checkstatus) {
        this.checkstatus = checkstatus;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
