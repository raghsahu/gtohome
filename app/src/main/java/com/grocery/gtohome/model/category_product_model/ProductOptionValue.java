package com.grocery.gtohome.model.category_product_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raghvendra Sahu on 27-Apr-20.
 */
public class ProductOptionValue {
    @SerializedName("product_option_value_id")
    @Expose
    private String productOptionValueId;
    @SerializedName("option_value_id")
    @Expose
    private String optionValueId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("price_prefix")
    @Expose
    private String pricePrefix;

    public ProductOptionValue(String qtyName, String qtyPrice, String qtyOptionValueId, String qtyProductOptionValueId) {
        this.name=qtyName;
        this.price=qtyPrice;
        this.optionValueId=qtyOptionValueId;
        this.productOptionValueId=qtyProductOptionValueId;
    }

    public String getProductOptionValueId() {
        return productOptionValueId;
    }

    public void setProductOptionValueId(String productOptionValueId) {
        this.productOptionValueId = productOptionValueId;
    }

    public String getOptionValueId() {
        return optionValueId;
    }

    public void setOptionValueId(String optionValueId) {
        this.optionValueId = optionValueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPricePrefix() {
        return pricePrefix;
    }

    public void setPricePrefix(String pricePrefix) {
        this.pricePrefix = pricePrefix;
    }
}
