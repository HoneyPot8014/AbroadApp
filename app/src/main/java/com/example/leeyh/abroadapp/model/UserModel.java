package com.example.leeyh.abroadapp.model;

public class UserModel {

    private String uid;
    private String userId;
    private String userImageUrl;

    public UserModel(String uid, String userId, String userImageUrl) {
        this.uid = uid;
        this.userId = userId;
        this.userImageUrl = userImageUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }
}
