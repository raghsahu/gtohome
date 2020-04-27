package com.grocery.gtohome.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 27-Apr-20.
 */
public class FilterByModel {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("sortby")
    @Expose
    private List<FilterBy> sortby = null;

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

    public List<FilterBy> getSortby() {
        return sortby;
    }

    public void setSortby(List<FilterBy> sortby) {
        this.sortby = sortby;
    }
}
