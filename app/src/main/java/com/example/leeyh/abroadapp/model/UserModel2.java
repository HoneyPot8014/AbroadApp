package com.example.leeyh.abroadapp.model;

public class UserModel2 {

    private String userId;
    private String userName;
    private String locate;
    private String password;
    private String gender;
    private String birthDay;

    public String getUserPassword() {
        return password;
    }

    public String getUserGender() {
        return gender;
    }

    public String getUserBirthDay() {
        return birthDay;
    }

    public void setUserPassword(String password) {

        this.password = password;
    }

    public void setUserGender(String gender) {
        this.gender = gender;
    }

    public void setUserBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getLocate() {
        return locate;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserLocate(String locate) {
        this.locate = locate;
    }
}
