package com.example.cafe4u.models;

public class Comment {
    public String context, reviewerId, shopId, id;
    public float rate;

    public Comment(String context, String reviewerId, String shopId, String id, float rate) {
        this.context = context;
        this.reviewerId = reviewerId;
        this.shopId = shopId;
        this.rate = rate;
        this.id = id;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
