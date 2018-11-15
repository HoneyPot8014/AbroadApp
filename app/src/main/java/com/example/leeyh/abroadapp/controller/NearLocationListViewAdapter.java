package com.example.leeyh.abroadapp.controller;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.leeyh.abroadapp.model.UserModel;
import com.example.leeyh.abroadapp.view.NearLocationItemView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.leeyh.abroadapp.constants.StaticString.DISTANCE;
import static com.example.leeyh.abroadapp.constants.StaticString.NICKNAME;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_ID;

public class NearLocationListViewAdapter extends BaseAdapter {

    List<UserModel> items = new ArrayList<>();

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
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

        UserModel item = items.get(i);
        itemView.setNickName(item.getUserNickName());
        itemView.setLocate(item.getLocate());
        itemView.setImageView(item.getUserId());

        return itemView;
    }

    public void addItem(JSONObject userData) {
        String id = userData.optString(USER_ID);
        String nickName = userData.optString(NICKNAME);
        String locate = userData.optString(DISTANCE);
        items.add(new UserModel(id, nickName, locate));
    }

    public void deleteAllItem() {
        items.clear();
    }
}
