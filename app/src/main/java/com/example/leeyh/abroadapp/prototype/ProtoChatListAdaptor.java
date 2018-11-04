package com.example.leeyh.abroadapp.prototype;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProtoChatListAdaptor extends BaseAdapter {

    List<ProtoChatModel> items = new ArrayList<>();

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
        ProtoChatListItemView itemView;

        if(view == null) {
            itemView = new ProtoChatListItemView(viewGroup.getContext());
        } else {
            itemView = (ProtoChatListItemView)view;
        }

        ProtoChatModel itemModel = items.get(i);
        itemView.setRoomName(itemModel.getRoomName());
        itemView.setLastMEssage(itemModel.getLastMessage());
        return itemView;
    }

    public void addItem(ProtoChatModel protoChatModel) {
        items.add(protoChatModel);
    }

    public ProtoChatModel getChatModel(int position) {
        return items.get(position);
    }
}
