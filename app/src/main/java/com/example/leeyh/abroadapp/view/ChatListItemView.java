package com.example.leeyh.abroadapp.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.helper.ApplicationManagement;

public class ChatListItemView extends LinearLayout{

    private ImageView mChatListProfileImageView;
    private TextView mChatListRoomNickNameTextView;
    private TextView mChatListLastMessageTextView;
    private ApplicationManagement mAppManager;

    public ChatListItemView(Context context) {
        super(context);
        init(context);
        mAppManager = (ApplicationManagement) context.getApplicationContext();
    }

    public ChatListItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        mAppManager = (ApplicationManagement) context.getApplicationContext();
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.chat_list_item_view, this, true);
        mChatListProfileImageView = rootView.findViewById(R.id.chat_list_profile_image_view);
        mChatListRoomNickNameTextView = rootView.findViewById(R.id.chat_list_room_nick_name_text_view);
        mChatListLastMessageTextView = rootView.findViewById(R.id.chat_list_last_message_text_view);
    }

    public void setChatListProfileImage(String id, Context context) {
        Glide.with(context).load("http://49.236.137.55/profile?id=" + id + ".jpeg").into(mChatListProfileImageView);
//        mChatListProfileImageView.setImageBitmap(mAppManager.getBitmapFromMemoryCache(userId));
    }

    public void setChatListRoomNickName(String nickName) {
        mChatListRoomNickNameTextView.setText(nickName);
    }

    public void setChatListLastMessage(String lastMessage) {
        mChatListLastMessageTextView.setText(lastMessage);
    }
}
