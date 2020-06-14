package com.grocery.gtohome.model.review_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewModelData {
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("date_added")
    @Expose
    private String dateAdded;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

}
