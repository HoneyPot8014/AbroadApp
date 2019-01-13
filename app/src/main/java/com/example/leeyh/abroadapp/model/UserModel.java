package com.example.leeyh.abroadapp.model;

import android.support.annotation.Nullable;

public class UserModel {

    private String uid;
    private String userEMail;
    private String userImageUrl;
    private String userName;
    private String age;
    private String sex;
    private String country;
    private String plan;

    public UserModel(String uid, String userEMail, String userImageUrl, String userName, String age, String sex, String country, @Nullable String plan) {
        this.uid = uid;
        this.userEMail = userEMail;
        this.userImageUrl = userImageUrl;
        this.userName = userName;
        this.age = age;
        this.sex = sex;
        this.country = country;
        this.plan = plan;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserEMail() {
        return userEMail;
    }

    public void setUserEMail(String userId) {
        this.userEMail = userId;
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

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }
}
