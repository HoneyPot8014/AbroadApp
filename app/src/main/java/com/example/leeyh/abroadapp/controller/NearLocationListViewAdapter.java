package com.example.leeyh.abroadapp.controller;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.leeyh.abroadapp.model.UserModel;
import com.example.leeyh.abroadapp.view.NearLocationItemView;

import java.util.ArrayList;
import java.util.List;

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

        if(view == null) {
            itemView = new NearLocationItemView(viewGroup.getContext());
        }else {
            itemView = (NearLocationItemView) view;
        }

        UserModel item = items.get(i);

        return null;
    }

   public void setMemberProfile() {

   }
}
