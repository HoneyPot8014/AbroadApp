package com.example.leeyh.abroadapp.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.helper.ApplicationManagement;

public class ChatMessageItemView extends LinearLayout{

    private ApplicationManagement mAppManager;
    private ImageView mOtherProfileImageView;
    private TextView mOtherMessageTextView;
    private TextView mMyMessageTextView;

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
    }

    public void setOtherProfileImageView(String userId) {
        mOtherProfileImageView.setImageBitmap(mAppManager.getBitmapFromMemoryCache(userId));
    }

    public void setOtherMessageTextView(String message) {
        mOtherMessageTextView.setText(message);
    }

    public void setMyMessageTextView(String message) {
        mMyMessageTextView.setText(message);
    }

    public void isMyMessage(boolean isMyMessage) {
        if(isMyMessage) {
            mOtherMessageTextView.setVisibility(GONE);
            mMyMessageTextView.setVisibility(VISIBLE);
        } else {
            mOtherMessageTextView.setVisibility(VISIBLE);
            mMyMessageTextView.setVisibility(GONE);
        }
    }

}
