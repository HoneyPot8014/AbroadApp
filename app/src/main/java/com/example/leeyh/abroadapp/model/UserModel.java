package com.example.leeyh.abroadapp.model;

public class UserModel {

    private String userId;
    private String userNickName;
    private String locate;

    public UserModel(String userId, String userNickName, String locate) {
        this.userId = userId;
        this.userNickName = userNickName;
        this.locate = locate;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public String getLocate() {
        return locate;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public void setLocate(String locate) {
        this.locate = locate;
    }
}
