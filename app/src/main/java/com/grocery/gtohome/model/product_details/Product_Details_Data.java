package com.grocery.gtohome.model.product_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.grocery.gtohome.model.category_product_model.CategoryProduct_List;
import com.grocery.gtohome.model.category_product_model.OptionQuantityType;

import java.util.List;

/**
 * Created by Raghvendra Sahu on 26-Apr-20.
 */
public class Product_Details_Data {

    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("manufacturer")
    @Expose
    private String manufacturer;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("special")
    @Expose
    private String special;
    @SerializedName("tax")
    @Expose
    private Boolean tax;
    @SerializedName("minimum")
    @Expose
    private String minimum;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("stock_status")
    @Expose
    private String stockStatus;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("reward")
    @Expose
    private Integer reward;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("href")
    @Expose
    private String href;
    @SerializedName("images")
    @Expose
    private List<Object> images = null;
    @SerializedName("options")
    @Expose
    private List<OptionQuantityType> options = null;
    @SerializedName("relatedProducts")
    @Expose
    private List<CategoryProduct_List> relatedProducts = null;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public Boolean getTax() {
        return tax;
    }

    public void setTax(Boolean tax) {
        this.tax = tax;
    }

    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Integer getReward() {
        return reward;
    }

    public void setReward(Integer reward) {
        this.reward = reward;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public List<Object> getImages() {
        return images;
    }

    public void setImages(List<Object> images) {
        this.images = images;
    }

    public List<OptionQuantityType> getOptions() {
        return options;
    }

    public void setOptions(List<OptionQuantityType> options) {
        this.options = options;
    }

    public List<CategoryProduct_List> getRelatedProducts() {
        return relatedProducts;
    }

    public void setRelatedProducts(List<CategoryProduct_List> relatedProducts) {
        this.relatedProducts = relatedProducts;
    }


}
