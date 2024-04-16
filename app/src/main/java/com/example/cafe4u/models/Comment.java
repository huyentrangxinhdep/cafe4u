package com.example.cafe4u.models;

public class Comment
{
    private int img_comment;
    private String userName, txt_comment;
    private float ratingBar;

    public Comment(int img_comment, String userName, String txt_comment, float ratingBar) {

        this.img_comment = img_comment;
        this.userName = userName;
        this.txt_comment = txt_comment;
        this.ratingBar = ratingBar;
    }

    public int getImg_comment() {
        return img_comment;
    }

    public void setImg_comment(int img_comment) {
        this.img_comment = img_comment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTxt_comment() {
        return txt_comment;
    }

    public void setTxt_comment(String txt_comment) {
        this.txt_comment = txt_comment;
    }

    public float getRatingBar() {
        return ratingBar;
    }

    public void setRatingBar(float ratingBar) {
        this.ratingBar = ratingBar;
    }
}