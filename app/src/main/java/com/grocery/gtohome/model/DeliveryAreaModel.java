package com.grocery.gtohome.model;

public class DeliveryAreaModel {
    String delivery_area; String pincode;

    public DeliveryAreaModel(String delivery_area, String pincode) {
        this.delivery_area = delivery_area;
        this.pincode = pincode;
    }

    public String getDelivery_area() {
        return delivery_area;
    }

    public void setDelivery_area(String delivery_area) {
        this.delivery_area = delivery_area;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
