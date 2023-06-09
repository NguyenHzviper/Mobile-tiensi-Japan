package com.nvn.homely.data.model.cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ACart implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("prod_id")
    @Expose
    private String prodId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;

    public ACart(String id, String prodId, String name, String image, Integer price, Integer quantity) {
        this.id = id;
        this.prodId = prodId;
        this.name = name;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
    }

    public ACart(Cart cart, Integer price) {
        this.id = cart.getId();
        this.prodId = cart.getProdId();
        this.name = cart.getName();
        this.image = cart.getImage();
        this.quantity = cart.getQuantity();
        this.price = price;
    }

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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
