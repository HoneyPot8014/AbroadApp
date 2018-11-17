package com.example.leeyh.abroadapp.controller;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.leeyh.abroadapp.view.ChatListItemView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.leeyh.abroadapp.constants.StaticString.JOIN_MEMBERS;
import static com.example.leeyh.abroadapp.constants.StaticString.LAST_MESSAGE;

public class ChatListAdapter extends BaseAdapter {

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

        ChatListItemView itemView;

        if (view == null) {
            itemView = new ChatListItemView(viewGroup.getContext());
        } else {
            itemView = (ChatListItemView) view;
        }

        try {
            JSONObject item = items.getJSONObject(i);
            itemView.setChatListLastMessage(item.getString(LAST_MESSAGE));
            JSONArray joinMembers = (JSONArray) item.get(JOIN_MEMBERS);
            itemView.setChatListProfileImage(joinMembers.getString(0), viewGroup.getContext());
            Log.d("채팅2", "getView: " + joinMembers);
            itemView.setChatListRoomNickName(joinMembers.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return itemView;
    }

    public void addList(JSONArray chattingList) {
        items = chattingList;
    }
}
