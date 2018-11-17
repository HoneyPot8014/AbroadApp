package com.example.leeyh.abroadapp.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.leeyh.abroadapp.helper.ApplicationManagement;
import com.example.leeyh.abroadapp.model.ChatModel;
import com.example.leeyh.abroadapp.view.ChatMessageItemView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.leeyh.abroadapp.constants.StaticString.MESSAGE;
import static com.example.leeyh.abroadapp.constants.StaticString.SEND_MESSAGE_ID;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_ID;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_INFO;

public class ChatMessageAdapter extends BaseAdapter {

    //    List<ChatModel> items = new ArrayList<>();
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
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SharedPreferences sharedPreferences = viewGroup.getContext().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        String myId = sharedPreferences.getString(USER_ID, null);

        ChatMessageItemView itemView;
        if (view == null) {
            itemView = new ChatMessageItemView(viewGroup.getContext());
        } else {
            itemView = (ChatMessageItemView) view;
        }

//        ChatModel item = items.get(i);
        JSONObject item = null;
        try {
            item = items.getJSONObject(i);
            if (myId.equals(item.getString(SEND_MESSAGE_ID))) {
                itemView.setIsMyMessage(true);
                itemView.setMyMessageTextView(item.getString(MESSAGE));
            } else {
//                itemView.isMyMessage(false);
                itemView.setIsMyMessage(false);
                itemView.setOtherMessageTextView(item.getString(MESSAGE));
                itemView.setOtherProfileImageView(viewGroup.getContext(), item.getString(SEND_MESSAGE_ID));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return itemView;
    }

    public void addItem(JSONObject chatModel) {
        items.put(chatModel);
    }

    public void addList(JSONArray chatMessageList) {
        items = chatMessageList;
    }
}
