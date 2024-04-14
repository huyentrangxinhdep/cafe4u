package com.example.cafe4u.models;

public class HistorySearch {
    public String id, shopId, userId;

    public HistorySearch(String id, String shopId, String userId) {
        this.id = id;
        this.shopId = shopId;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
