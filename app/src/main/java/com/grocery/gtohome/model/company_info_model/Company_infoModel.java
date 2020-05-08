package com.grocery.gtohome.model.company_info_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 06-May-20.
 */
public class Company_infoModel {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("informations")
    @Expose
    private List<InformationCompany> informations = null;

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

    public List<InformationCompany> getInformations() {
        return informations;
    }

    public void setInformations(List<InformationCompany> informations) {
        this.informations = informations;
    }
}
