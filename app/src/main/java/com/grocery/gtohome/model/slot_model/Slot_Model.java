package com.grocery.gtohome.model.slot_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raghvendra Sahu on 12-May-20.
 */
public class Slot_Model {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("module_pincodedays")
    @Expose
    private ModulePincodedays modulePincodedays;

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

    public ModulePincodedays getModulePincodedays() {
        return modulePincodedays;
    }

    public void setModulePincodedays(ModulePincodedays modulePincodedays) {
        this.modulePincodedays = modulePincodedays;
    }

}
