package com.example.leeyh.abroadapp.prototype;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.leeyh.abroadapp.R;

public class ProtoChatListItemView extends LinearLayout {

    TextView mRoomNameTextView;
    TextView mLastMessageTextView;

    public ProtoChatListItemView(Context context) {
        super(context);
        init(context);
    }

    public ProtoChatListItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.proto_chat_list_item_view, this, true);

        mRoomNameTextView = findViewById(R.id.room_name_text_view);
        mLastMessageTextView = findViewById(R.id.last_message_text_view);
    }

    public void setRoomName(String roomName) {
        mRoomNameTextView.setText(roomName);
    }

    public void setLastMEssage(String message) {
        mLastMessageTextView.setText(message);
    }
}
