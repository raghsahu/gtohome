package com.grocery.gtohome.model;

public class WelcomeModel {
    String name;
    String url;
    int image;

    public WelcomeModel(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public WelcomeModel(String name, int image, String url) {
        this.name = name;
        this.image = image;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setImage(int image) {
        this.image = image;
    }

}
