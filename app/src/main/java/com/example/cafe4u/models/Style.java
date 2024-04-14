package com.example.cafe4u.models;

public class Style {
    public String id, space;

    public Style(String id, String space) {
        this.id = id;
        this.space = space;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }
}
