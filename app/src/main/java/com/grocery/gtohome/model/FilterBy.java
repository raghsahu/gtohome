package com.grocery.gtohome.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

/**
 * Created by Raghvendra Sahu on 27-Apr-20.
 */
public class FilterBy {
 @SerializedName("text")
 @Expose
 private String text;
 @SerializedName("value")
 @Expose
 private String value;
 @SerializedName("order")
 @Expose
 private String order;

 public String getText() {
  return text;
 }

 public void setText(String text) {
  this.text = text;
 }

 public String getValue() {
  return value;
 }

 public void setValue(String value) {
  this.value = value;
 }

 public String getOrder() {
  return order;
 }

 public void setOrder(String order) {
  this.order = order;
 }

}
