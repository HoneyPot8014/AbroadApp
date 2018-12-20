package com.example.leeyh.abroadapp.mypage;

import com.google.gson.annotations.Expose;

public class Dictionary {
    @Expose
    private String id;
    @Expose
    private String month;

    public Dictionary(String id, String month) {
        this.id = id;
        this.month = month;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
