package com.example.cafe4u.models;

public class Shop {

    int vote;
    String name, address, imageShop, openTime, space, styleId;

    public Shop(int vote, String name, String address, String imageShop, String openTime, String space, String styleId) {
        this.vote = vote;
        this.name = name;
        this.address = address;
        this.imageShop = imageShop;
        this.openTime = openTime;
        this.space = space;
        this.styleId = styleId;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
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

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }
}
