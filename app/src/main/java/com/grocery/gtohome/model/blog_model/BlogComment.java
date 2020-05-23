package com.grocery.gtohome.model.blog_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BlogComment {
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("date_added")
    @Expose
    private String dateAdded;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }
}
