package com.grocery.gtohome.model.blog_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BlogModel {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("error")
    @Expose
    private ErrorComment error;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("blogs")
    @Expose
    private List<BlogData> blogs = null;

    @SerializedName("blog_info")
    @Expose
    private BlogInfo blogInfo;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public ErrorComment getError() {
        return error;
    }

    public void setError(ErrorComment error) {
        this.error = error;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public BlogInfo getBlogInfo() {
        return blogInfo;
    }

    public void setBlogInfo(BlogInfo blogInfo) {
        this.blogInfo = blogInfo;
    }

    public List<BlogData> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<BlogData> blogs) {
        this.blogs = blogs;
    }
}
