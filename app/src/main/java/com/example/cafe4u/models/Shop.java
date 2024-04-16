package com.example.cafe4u.models;

import com.google.firebase.firestore.GeoPoint;

public class Shop {
    private String shopId, name, address, commentId, imageShop, openTime, phoneNumber, style;
    private int vote;
    private GeoPoint location;

    public Shop(String shopId, String name, String address, String commentId, String imageShop, String openTime, String phoneNumber, String style, int vote, GeoPoint location) {
        this.shopId = shopId;
        this.name = name;
        this.address = address;
        this.commentId = commentId;
        this.imageShop = imageShop;
        this.openTime = openTime;
        this.phoneNumber = phoneNumber;
        this.style = style;
        this.vote = vote;
        this.location = location;
    }
    public Shop() {}

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getImageShop() {
        return imageShop;
    }

    public void setImageShop(String imageShop) {
        this.imageShop = imageShop;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String styleId) {
        this.style = style;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }
}