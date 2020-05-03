package com.grocery.gtohome.model.address_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 03-May-20.
 */
public class AddressModel {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("default_address_id")
    @Expose
    private String defaultAddressId;
    @SerializedName("addresses")
    @Expose
    private List<AddressData> addresses = null;

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

    public String getDefaultAddressId() {
        return defaultAddressId;
    }

    public void setDefaultAddressId(String defaultAddressId) {
        this.defaultAddressId = defaultAddressId;
    }

    public List<AddressData> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressData> addresses) {
        this.addresses = addresses;
    }

}
