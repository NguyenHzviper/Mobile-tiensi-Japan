package com.nvn.homely.data.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("deliveryAddress")
    @Expose
    private String deliveryAddress;
    @SerializedName("buyDate")
    @Expose
    private String buyDate;
    @SerializedName("deliveryCancelDay")
    @Expose
    private Object deliveryCancelDay;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("recipientName")
    @Expose
    private String recipientName;
    @SerializedName("listOrderItem")
    @Expose
    private int money;

    public Order() {

    }

    public Order(String id, String userId, String deliveryAddress, String buyDate, Object deliveryCancelDay, String phone, Boolean status, String recipientName, ArrayList<ListOrderItem> listOrderItem) {
        this.id = id;
        this.userId = userId;
        this.deliveryAddress = deliveryAddress;
        this.buyDate = buyDate;
        this.deliveryCancelDay = deliveryCancelDay;
        this.phone = phone;
        this.status = status;
        this.recipientName = recipientName;

    }

    public Order(Order order) {
        this.id = order.getId();
        this.userId = order.getUserId();
        this.deliveryAddress = order.getDeliveryAddress();
        this.buyDate = order.getBuyDate();
        this.deliveryCancelDay = order.getDeliveryCancelDay();
        this.phone = order.getPhone();
        this.status = order.getStatus();
        this.recipientName = order.getRecipientName();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    public Object getDeliveryCancelDay() {
        return deliveryCancelDay;
    }

    public void setDeliveryCancelDay(Object deliveryCancelDay) {
        this.deliveryCancelDay = deliveryCancelDay;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public void setMoney(int money){this.money = money;}
    public int getMoney() {return this.money;}

}
