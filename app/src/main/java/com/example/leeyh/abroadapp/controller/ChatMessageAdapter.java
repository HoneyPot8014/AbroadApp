package com.example.leeyh.abroadapp.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.leeyh.abroadapp.helper.ApplicationManagement;
import com.example.leeyh.abroadapp.model.ChatModel;
import com.example.leeyh.abroadapp.view.ChatMessageItemView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.leeyh.abroadapp.constants.StaticString.MESSAGE;
import static com.example.leeyh.abroadapp.constants.StaticString.SEND_MESSAGE_ID;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_ID;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_INFO;

public class ChatMessageAdapter extends BaseAdapter{

    List<ChatModel> items = new ArrayList<>();

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
        ApplicationManagement appManager = (ApplicationManagement) viewGroup.getContext().getApplicationContext();
        SharedPreferences sharedPreferences = viewGroup.getContext().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        String myId = sharedPreferences.getString(USER_ID, null);

        ChatMessageItemView itemView;
        if(view == null) {
            itemView = new ChatMessageItemView(viewGroup.getContext());
        } else {
            itemView = (ChatMessageItemView) view;
        }

        ChatModel item = items.get(i);
        if(myId.equals(item.getUserId())) {
            itemView.isMyMessage(true);
            itemView.setMyMessageTextView(item.getMessage());
        } else {
            itemView.isMyMessage(false);
            itemView.setOtherMessageTextView(item.getMessage());
            itemView.setOtherProfileImageView(item.getUserId());
        }

        return itemView;
    }

    public void addItem(JSONObject chatModel) {
        try {
            String id = chatModel.getString(SEND_MESSAGE_ID);
            String message = chatModel.getString(MESSAGE);
            items.add(new ChatModel(id, message));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
