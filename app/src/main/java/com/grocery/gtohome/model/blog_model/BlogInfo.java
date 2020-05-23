package com.grocery.gtohome.model.blog_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BlogInfo {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("text_date_added")
    @Expose
    private String textDateAdded;
    @SerializedName("date_added")
    @Expose
    private String dateAdded;
    @SerializedName("text_your_comments")
    @Expose
    private String textYourComments;
    @SerializedName("entry_author")
    @Expose
    private String entryAuthor;
    @SerializedName("entry_email")
    @Expose
    private String entryEmail;
    @SerializedName("entry_comment")
    @Expose
    private String entryComment;
    @SerializedName("button_comment_add")
    @Expose
    private String buttonCommentAdd;
    @SerializedName("allow_comments")
    @Expose
    private String allowComments;
    @SerializedName("login_required")
    @Expose
    private String loginRequired;
    @SerializedName("auto_approve")
    @Expose
    private String autoApprove;
    @SerializedName("blog_comments")
    @Expose
    private List<BlogComment> blogComments = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTextDateAdded() {
        return textDateAdded;
    }

    public void setTextDateAdded(String textDateAdded) {
        this.textDateAdded = textDateAdded;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getTextYourComments() {
        return textYourComments;
    }

    public void setTextYourComments(String textYourComments) {
        this.textYourComments = textYourComments;
    }

    public String getEntryAuthor() {
        return entryAuthor;
    }

    public void setEntryAuthor(String entryAuthor) {
        this.entryAuthor = entryAuthor;
    }

    public String getEntryEmail() {
        return entryEmail;
    }

    public void setEntryEmail(String entryEmail) {
        this.entryEmail = entryEmail;
    }

    public String getEntryComment() {
        return entryComment;
    }

    public void setEntryComment(String entryComment) {
        this.entryComment = entryComment;
    }

    public String getButtonCommentAdd() {
        return buttonCommentAdd;
    }

    public void setButtonCommentAdd(String buttonCommentAdd) {
        this.buttonCommentAdd = buttonCommentAdd;
    }

    public String getAllowComments() {
        return allowComments;
    }

    public void setAllowComments(String allowComments) {
        this.allowComments = allowComments;
    }

    public String getLoginRequired() {
        return loginRequired;
    }

    public void setLoginRequired(String loginRequired) {
        this.loginRequired = loginRequired;
    }

    public String getAutoApprove() {
        return autoApprove;
    }

    public void setAutoApprove(String autoApprove) {
        this.autoApprove = autoApprove;
    }

    public List<BlogComment> getBlogComments() {
        return blogComments;
    }

    public void setBlogComments(List<BlogComment> blogComments) {
        this.blogComments = blogComments;
    }
}
