package com.grocery.gtohome.model.slot_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raghvendra Sahu on 12-May-20.
 */
public class Individualslots {
    @SerializedName("19")
    @Expose
    private String _1;
    @SerializedName("18")
    @Expose
    private String _2;
    @SerializedName("17")
    @Expose
    private String _5;
    @SerializedName("16")
    @Expose
    private String _6;

    public String get1() {
        return _1;
    }

    public void set1(String _1) {
        this._1 = _1;
    }

    public String get2() {
        return _2;
    }

    public String get_5() {
        return _5;
    }

    public void set_5(String _5) {
        this._5 = _5;
    }

    public String get_6() {
        return _6;
    }

    public void set_6(String _6) {
        this._6 = _6;
    }

    public void set2(String _2) {
        this._2 = _2;
    }
}
