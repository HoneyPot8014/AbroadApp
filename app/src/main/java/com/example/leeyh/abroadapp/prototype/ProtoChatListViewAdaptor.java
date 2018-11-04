package com.example.leeyh.abroadapp.prototype;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.leeyh.abroadapp.model.ChatModel;
import com.example.leeyh.abroadapp.view.MessageView;

import java.util.ArrayList;
import java.util.List;

public class ProtoChatListViewAdaptor extends BaseAdapter {

    private List<ChatModel> items = new ArrayList<>();

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

        MessageView otherChatMessageView;

        ChatModel item = items.get(i);
        if (view == null) {
            otherChatMessageView = new MessageView(viewGroup.getContext());
        } else {
            otherChatMessageView = (MessageView) view;
        }

        if(item.isMyMessage()) {
            otherChatMessageView.getmProfileImageView().setVisibility(View.GONE);
            otherChatMessageView.getmOtherMessageView().setVisibility(View.GONE);
            otherChatMessageView.getmMyMessageView().setVisibility(View.VISIBLE);
            otherChatMessageView.getmMyMessageView().setText(item.getMessage());

        } else {
            otherChatMessageView.getmProfileImageView().setVisibility(View.VISIBLE);
            otherChatMessageView.getmOtherMessageView().setVisibility(View.VISIBLE);
            otherChatMessageView.getmMyMessageView().setVisibility(View.GONE);
            otherChatMessageView.getmOtherMessageView().setText(item.getMessage());
        }

        return otherChatMessageView;
    }

    public void addMessage(ChatModel item) {
        items.add(item);
    }

}
