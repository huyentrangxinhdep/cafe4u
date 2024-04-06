package com.example.cafe4u.models;

public class Shop {
    int image;
    String name;
    int vote;
    String describe;

    public Shop(int image, String name, int vote, String describe) {
        this.image = image;
        this.name = name;
        this.vote = vote;
        this.describe = describe;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
