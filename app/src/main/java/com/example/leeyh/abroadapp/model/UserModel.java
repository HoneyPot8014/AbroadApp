package com.example.leeyh.abroadapp.model;

public class UserModel {

    private String uid;
    private String userId;
    private String userImageUrl;
    private String userName;
    private String age;
    private String sex;
    private String country;

    public UserModel(String uid, String userId, String userImageUrl, String userName, String age, String sex, String country) {
        this.uid = uid;
        this.userId = userId;
        this.userImageUrl = userImageUrl;
        this.userName = userName;
        this.age = age;
        this.sex = sex;
        this.country = country;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
