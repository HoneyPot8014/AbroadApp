package com.example.leeyh.abroadapp.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.leeyh.abroadapp.R;

public class MyChatMessageView extends LinearLayout {

    private TextView mTextView;

    public MyChatMessageView(Context context) {
        super(context);
        init(context);
    }

    public MyChatMessageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.my_chat_message_item, this,true);

        mTextView = findViewById(R.id.chat_message_item_text_view);
    }

    public void setText(String message) {
        mTextView.setText(message);
    }

    public TextView getTextView() {
        return mTextView;
    }
}
