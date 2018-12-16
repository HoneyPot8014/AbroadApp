package com.example.leeyh.abroadapp.controller;

import android.content.Context;
import android.content.SharedPreferences;
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
import static com.example.leeyh.abroadapp.constants.StaticString.MEMBER_UUID;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_INFO;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_NAME;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_UUID;

public class ChatListAdapter extends BaseAdapter {

    private SharedPreferences sharedPreferences;
    private JSONArray items = new JSONArray();

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

        sharedPreferences = viewGroup.getContext().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);

        ChatListItemView itemView;

        if (view == null) {
            itemView = new ChatListItemView(viewGroup.getContext());
        } else {
            itemView = (ChatListItemView) view;
        }

        try {
            JSONObject item = items.getJSONObject(i);
            JSONArray joinMembers = (JSONArray) item.get(JOIN_MEMBERS);
            JSONArray joinUuid = (JSONArray) item.get(MEMBER_UUID);
            String profileId;
            if (sharedPreferences.getString(USER_UUID, null).equals(joinUuid.getString(0))) {
                profileId = joinUuid.getString(1);
            } else {
                profileId = joinUuid.getString(0);
            }
            StringBuilder members = new StringBuilder();
            for (int k = 0; k < joinMembers.length(); k++) {
                if (!sharedPreferences.getString(USER_NAME, null).equals(joinMembers.get(k))){
                    members.append(joinMembers.getString(k)).append(" ");
                }
            }
            itemView.setChatListRoomNickName(members.toString());
            itemView.setChatListProfileImage(profileId, viewGroup.getContext());
            itemView.setChatListLastMessage(item.getString(LAST_MESSAGE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return itemView;
    }

    public void addList(JSONArray chattingList) {
        items = chattingList;
    }

    public JSONArray getList() {
        return this.items;
    }

    public void setList(JSONArray chattingList) {
        this.items = chattingList;
    }
}
