package com.example.leeyh.abroadapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

public class UserModel implements Parcelable {

    private String uid;
    private String userEMail;
    private String userImageUrl;
    private String userName;
    private String age;
    private String sex;
    private String country;
    private String plan;

    public UserModel() {

    }

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

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    protected UserModel(Parcel in) {
        uid = in.readString();
        userEMail = in.readString();
        userImageUrl = in.readString();
        userName = in.readString();
        age = in.readString();
        sex = in.readString();
        country = in.readString();
        plan = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(userEMail);
        dest.writeString(userImageUrl);
        dest.writeString(userName);
        dest.writeString(age);
        dest.writeString(sex);
        dest.writeString(country);
        dest.writeString(plan);
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
