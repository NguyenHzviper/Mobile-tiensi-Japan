package com.nvn.mobilent.data.model.cart;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Cart implements Serializable {

    @DocumentId
    private String id;
    private String prodId;
    private String name;
    private String image;
    private Integer quantity;
    private String userId;

    public Cart() {
        // Default constructor required for Firestore
    }

    public Cart(String id,String prodId,String image,String name, Integer quantity, String userId) {
        this.id = id;
        this.prodId = prodId;
        this.userId = userId;
        this.quantity = quantity;
        this.image = image;
        this.name = name;
    }

    public Cart(Cart c) {
        this.id = c.getId();
        this.prodId = c.getProdId();
        this.name = c.getName();
        this.image = c.getImage();
        this.quantity = c.getQuantity();
        this.userId = c.getUserId();
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @NonNull
    @Override
    public String toString() {
        return "CART:  " + getProdId() + "|" + getName() + "|" + getQuantity() + "|" + getImage();
    }
}
