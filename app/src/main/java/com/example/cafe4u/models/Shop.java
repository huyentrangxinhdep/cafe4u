package com.example.cafe4u.models;

import com.google.firebase.firestore.GeoPoint;

public class Shop {

    int vote;
    String name, address, imageShop, openTime, style, commentId, shopId;
    GeoPoint location;

    public Shop(String name, String address, String imageShop, String openTime, String style, String commentId, GeoPoint location) {
        this.name = name;
        this.address = address;
        this.imageShop = imageShop;
        this.openTime = openTime;
        this.style = style;
        this.commentId = commentId;
        this.location = location;
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

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }
}
