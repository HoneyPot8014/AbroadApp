package com.example.leeyh.abroadapp.view.fragment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.leeyh.abroadapp.R;

public class ChatListItemView extends LinearLayout{

    private ImageView mChatListProfileImageView;
    private TextView mChatListRoomNickNameTextView;
    private TextView mChatListLastMessageTextView;

    public ChatListItemView(Context context) {
        super(context);
        init(context);
    }

    public ChatListItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.chat_list_item_view, this, true);
        mChatListProfileImageView = rootView.findViewById(R.id.chat_list_profile_image_view);
        mChatListRoomNickNameTextView = rootView.findViewById(R.id.chat_list_room_nick_name_text_view);
        mChatListLastMessageTextView = rootView.findViewById(R.id.chat_list_last_message_text_view);
    }

    public void setChatListProfileImage() {

    }

    public void setChatListRoomNickName() {

    }

    public void setChatListLastMessage() {

    }
}
