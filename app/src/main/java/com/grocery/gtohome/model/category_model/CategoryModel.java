package com.grocery.gtohome.model.category_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 21-Apr-20.
 */
public class CategoryModel {
    @SerializedName("categories")
    @Expose
    private List<CategoryName> categories = null;
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("msg")
    @Expose
    private String msg;

    public List<CategoryName> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryName> categories) {
        this.categories = categories;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
