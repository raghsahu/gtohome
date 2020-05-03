package com.grocery.gtohome.fragment.state_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 03-May-20.
 */
public class StateModel {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("country_id")
    @Expose
    private String countryId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("iso_code_2")
    @Expose
    private String isoCode2;
    @SerializedName("iso_code_3")
    @Expose
    private String isoCode3;
    @SerializedName("address_format")
    @Expose
    private String addressFormat;
    @SerializedName("postcode_required")
    @Expose
    private String postcodeRequired;
    @SerializedName("country_status")
    @Expose
    private String countryStatus;
    @SerializedName("zone")
    @Expose
    private List<StateZoneData> zone = null;

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

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsoCode2() {
        return isoCode2;
    }

    public void setIsoCode2(String isoCode2) {
        this.isoCode2 = isoCode2;
    }

    public String getIsoCode3() {
        return isoCode3;
    }

    public void setIsoCode3(String isoCode3) {
        this.isoCode3 = isoCode3;
    }

    public String getAddressFormat() {
        return addressFormat;
    }

    public void setAddressFormat(String addressFormat) {
        this.addressFormat = addressFormat;
    }

    public String getPostcodeRequired() {
        return postcodeRequired;
    }

    public void setPostcodeRequired(String postcodeRequired) {
        this.postcodeRequired = postcodeRequired;
    }

    public String getCountryStatus() {
        return countryStatus;
    }

    public void setCountryStatus(String countryStatus) {
        this.countryStatus = countryStatus;
    }

    public List<StateZoneData> getZone() {
        return zone;
    }

    public void setZone(List<StateZoneData> zone) {
        this.zone = zone;
    }
}
