package com.grocery.gtohome.model.category_model;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 21-Apr-20.
 */
public class CategoryName extends BaseObservable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("children")
    @Expose
    private List<CategoryChild> children = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CategoryChild> getChildren() {
        return children;
    }

    public void setChildren(List<CategoryChild> children) {
        this.children = children;
    }

}
