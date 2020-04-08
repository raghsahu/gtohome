package com.grocery.gtohome.model;

/**
 * Created by Raghvendra Sahu on 08-Apr-20.
 */
public class SliderModel {
    String desc;
    int slide_image;

    public SliderModel(String desc, int slide_image) {
        this.desc = desc;
        this.slide_image = slide_image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSlide_image() {
        return slide_image;
    }

    public void setSlide_image(int slide_image) {
        this.slide_image = slide_image;
    }
}
