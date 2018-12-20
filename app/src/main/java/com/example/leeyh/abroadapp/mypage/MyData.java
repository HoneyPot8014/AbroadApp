package com.example.leeyh.abroadapp.mypage;

import android.graphics.Bitmap;

import java.io.Serializable;

public class MyData implements Serializable {
    public String date;
    public String country;
    public String budget;

    //    public String img;
    public Bitmap img;

    public MyData(String date, String country, String budget, Bitmap img){
        this.date = date;
        this.country = country;
        this.budget = budget;
        this.img = img;
    }
}
