package com.grocery.gtohome.model.login_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModel {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("customerLogin")
    @Expose
    private Boolean customerLogin;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("api_token")
    @Expose
    private String apiToken;
    @SerializedName("customer_info")
    @Expose
    private CustomerInfo customerInfo;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getCustomerLogin() {
        return customerLogin;
    }

    public void setCustomerLogin(Boolean customerLogin) {
        this.customerLogin = customerLogin;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }

}
