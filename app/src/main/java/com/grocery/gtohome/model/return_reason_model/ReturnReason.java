package com.grocery.gtohome.model.return_reason_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReturnReason {
    @SerializedName("return_reason_id")
    @Expose
    private String returnReasonId;
    @SerializedName("name")
    @Expose
    private String name;

    public String getReturnReasonId() {
        return returnReasonId;
    }

    public void setReturnReasonId(String returnReasonId) {
        this.returnReasonId = returnReasonId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
