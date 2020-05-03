package com.grocery.gtohome.fragment.country_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 03-May-20.
 */
public class CountryModel {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("countries")
    @Expose
    private List<CountryData> countries = null;

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

    public List<CountryData> getCountries() {
        return countries;
    }

    public void setCountries(List<CountryData> countries) {
        this.countries = countries;
    }
}
