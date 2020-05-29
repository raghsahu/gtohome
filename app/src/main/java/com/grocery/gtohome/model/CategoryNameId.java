package com.grocery.gtohome.model;

public class CategoryNameId {
    String name;
    String category_id;

    public CategoryNameId(String name, String category_id) {
        this.name = name;
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }
}
