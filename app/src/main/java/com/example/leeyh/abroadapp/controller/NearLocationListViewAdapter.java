package com.example.leeyh.abroadapp.controller;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.leeyh.abroadapp.dataconverter.DataConverter;
import com.example.leeyh.abroadapp.view.NearLocationItemView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.leeyh.abroadapp.constants.StaticString.LATITUDE;
import static com.example.leeyh.abroadapp.constants.StaticString.LONGITUDE;
import static com.example.leeyh.abroadapp.constants.StaticString.NICKNAME;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_ID;

public class NearLocationListViewAdapter extends BaseAdapter {

    JSONArray items = new JSONArray();

    @Override
    public int getCount() {
        return items.length();
    }

    @Override
    public Object getItem(int i) {
        try {
            return items.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
            return  null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        NearLocationItemView itemView;

        if (view == null) {
            itemView = new NearLocationItemView(viewGroup.getContext());
        } else {
            itemView = (NearLocationItemView) view;
        }

        try {
            JSONObject item = items.getJSONObject(i);
            double latitude = Double.parseDouble(item.getString(LATITUDE));
            double longitude = Double.parseDouble(item.getString(LONGITUDE));
            itemView.setNickName(item.getString(NICKNAME));
            itemView.setLocate(DataConverter.convertAddress(viewGroup.getContext(), latitude, longitude));
            itemView.setImageView(item.getString(USER_ID), viewGroup.getContext());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return itemView;
    }

    public void deleteAllItem() {
        items = null;
    }

    public void addList(JSONArray userDataJsonArray) {
        items = userDataJsonArray;
    }
}
