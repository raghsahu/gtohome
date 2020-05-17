package com.grocery.gtohome.model.cart_model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Raghvendra Sahu on 28-Apr-20.
 */
public class CartTotal implements Serializable, Parcelable {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("text")
    @Expose
    private String text;

    protected CartTotal(Parcel in) {
        title = in.readString();
        text = in.readString();
    }

    public static final Creator<CartTotal> CREATOR = new Creator<CartTotal>() {
        @Override
        public CartTotal createFromParcel(Parcel in) {
            return new CartTotal(in);
        }

        @Override
        public CartTotal[] newArray(int size) {
            return new CartTotal[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(text);
    }
}
