package com.grocery.gtohome.model.wallet_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WalletModelList {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("balance")
    @Expose
    private String balance;
    @SerializedName("credit_count")
    @Expose
    private Integer creditCount;
    @SerializedName("total_credits")
    @Expose
    private String totalCredits;
    @SerializedName("credits")
    @Expose
    private List<Credit_List> credits = null;
    @SerializedName("debit_count")
    @Expose
    private Integer debitCount;
    @SerializedName("total_debits")
    @Expose
    private String totalDebits;
    @SerializedName("debits")
    @Expose
    private List<Debit_List> debits = null;

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

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public Integer getCreditCount() {
        return creditCount;
    }

    public void setCreditCount(Integer creditCount) {
        this.creditCount = creditCount;
    }

    public String getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(String totalCredits) {
        this.totalCredits = totalCredits;
    }

    public List<Credit_List> getCredits() {
        return credits;
    }

    public void setCredits(List<Credit_List> credits) {
        this.credits = credits;
    }

    public Integer getDebitCount() {
        return debitCount;
    }

    public void setDebitCount(Integer debitCount) {
        this.debitCount = debitCount;
    }

    public String getTotalDebits() {
        return totalDebits;
    }

    public void setTotalDebits(String totalDebits) {
        this.totalDebits = totalDebits;
    }

    public List<Debit_List> getDebits() {
        return debits;
    }

    public void setDebits(List<Debit_List> debits) {
        this.debits = debits;
    }
}
