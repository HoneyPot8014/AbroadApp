package com.example.leeyh.abroadapp.controller;

import android.view.View;

import org.json.JSONObject;

public  interface RecyclerItemClickListener {
    void onItemClicked(View view, int position, JSONObject item);
}