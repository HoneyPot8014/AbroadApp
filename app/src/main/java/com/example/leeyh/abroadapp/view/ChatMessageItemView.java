package com.example.leeyh.abroadapp.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.helper.ApplicationManagement;

public class ChatMessageItemView extends LinearLayout {

    private ApplicationManagement mAppManager;
    private ImageView mOtherProfileImageView;
    private TextView mOtherMessageTextView;
    private TextView mMyMessageTextView;
    private CardView mRoundedCardView;
    private boolean isMyMessage;

    public ChatMessageItemView(Context context) {
        super(context);
        inflate(context);
    }

    public ChatMessageItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context);
    }

    private void inflate(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.chat_message_view, this, true);

        mAppManager = (ApplicationManagement) context.getApplicationContext();
        mOtherProfileImageView = findViewById(R.id.other_profile_image_view);
        mOtherMessageTextView = findViewById(R.id.other_message_text_view);
        mMyMessageTextView = findViewById(R.id.my_message_text_view);
        mRoundedCardView = findViewById(R.id.messageCardView);
    }

    public void setIsMyMessage(boolean isMyMessage) {
        this.isMyMessage = isMyMessage;
    }

    public void setOtherProfileImageView(Context context, String userId) {
        mOtherProfileImageView.setVisibility(VISIBLE);
        mRoundedCardView.setVisibility(VISIBLE);
        Glide.with(context).load("http://49.236.137.55/profile?id=" + userId + ".jpeg").into(mOtherProfileImageView);
        mOtherMessageTextView.setVisibility(VISIBLE);
        mMyMessageTextView.setVisibility(GONE);
    }

    public void setOtherMessageTextView(String message) {
        mOtherMessageTextView.setVisibility(VISIBLE);
        mOtherMessageTextView.setText(message);
        mOtherProfileImageView.setVisibility(GONE);
        mOtherMessageTextView.setVisibility(GONE);
        mRoundedCardView.setVisibility(GONE);
    }
    public void setMyMessageTextView(String message) {
        mMyMessageTextView.setVisibility(VISIBLE);
        mMyMessageTextView.setText(message);
        mOtherMessageTextView.setVisibility(GONE);
        mOtherProfileImageView.setVisibility(GONE);
        mRoundedCardView.setVisibility(GONE);
    }

    public void isMyMessage(boolean isMyMessage) {
        if (isMyMessage) {
            mOtherMessageTextView.setVisibility(GONE);
            mMyMessageTextView.setVisibility(VISIBLE);
        } else {
            mOtherMessageTextView.setVisibility(VISIBLE);
            mMyMessageTextView.setVisibility(GONE);
        }
    }

}
