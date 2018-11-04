package com.example.leeyh.abroadapp.prototype;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.model.ChatModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.leeyh.abroadapp.prototype.Constants.GET_STORED_CHAT;
import static com.example.leeyh.abroadapp.prototype.Constants.RECEIVE_CHAT;
import static com.example.leeyh.abroadapp.prototype.Constants.REQUEST_STORED_CHAT;
import static com.example.leeyh.abroadapp.prototype.Constants.SEND_CHAT;
import static com.example.leeyh.abroadapp.prototype.ProtoChatListActivity.CHATS;
import static com.example.leeyh.abroadapp.prototype.ProtoChatListActivity.ROOM_NAME;
import static com.example.leeyh.abroadapp.prototype.ProtoSignUpActivity.EVENT;
import static com.example.leeyh.abroadapp.prototype.ProtoSignUpActivity.USER_ID;

public class ProtoRoomChatActivity extends AppCompatActivity {

    public static final String MESSAGE = "message";
    public static final String SEND_ID = "sendMessageId";
    private ProtoChatListViewAdaptor mChatAdaptor;
    private ListView mChatView;
    private EditText mInputMessageTextView;
    private String mUserId;
    private String mRoomName;
    private BroadcastReceiver getStoredChatReceiver;
    private BroadcastReceiver receiveChatReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_chat);

        SharedPreferences preferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        mUserId = preferences.getString(USER_ID, null);
        mRoomName = getIntent().getStringExtra(ROOM_NAME);

        mChatView = findViewById(R.id.chat_messages_list_view);
        mChatView.setStackFromBottom(true);
        Button sendMessageButton = findViewById(R.id.send_message_button);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSendButtonClicked();
            }
        });
        mInputMessageTextView = findViewById(R.id.input_message_edit_text_view);

        mChatAdaptor = new ProtoChatListViewAdaptor();
        mChatView.setAdapter(mChatAdaptor);
        restoreChats();
    }

    //전송 버튼 이벤트
    private void onSendButtonClicked() {
        String message = mInputMessageTextView.getText().toString();
        mChatAdaptor.addMessage(new ChatModel(mUserId, message, true));
        mChatAdaptor.notifyDataSetChanged();
        mChatView.setSelection(mChatAdaptor.getCount());

        Intent service = new Intent(getApplicationContext(), ProtoBackgroundChat.class);
        service.putExtra(EVENT, SEND_CHAT);
        service.putExtra(USER_ID, mUserId);
        service.putExtra(ROOM_NAME, mRoomName);
        service.putExtra(MESSAGE, message);
        startService(service);

    }

    private void restoreChats() {
        Intent service = new Intent(getApplicationContext(), ProtoBackgroundChat.class);
        service.putExtra(EVENT, REQUEST_STORED_CHAT);
        service.putExtra(USER_ID, mUserId);
        service.putExtra(ROOM_NAME, mRoomName);
        startService(service);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getStoredChatSocketOnEvent();
        receiveChatSocketOnEvent();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(getStoredChatReceiver);
        unregisterReceiver(receiveChatReceiver);
    }

    private void getStoredChatSocketOnEvent() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GET_STORED_CHAT);
        getStoredChatReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    JSONArray receivedData = new JSONArray(intent.getStringExtra(CHATS));
                    for (int i = 0; i < receivedData.length(); i++) {
                        JSONObject messageObject = (JSONObject) receivedData.get(i);
                        String message = messageObject.optString(MESSAGE);
                        String userId = messageObject.optString(SEND_ID);
                        if (!userId.equals(mUserId)) {
                            mChatAdaptor.addMessage(new ChatModel(userId, message, false));
                        } else {
                            mChatAdaptor.addMessage(new ChatModel(userId, message, true));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        registerReceiver(getStoredChatReceiver, intentFilter);
    }

    private void receiveChatSocketOnEvent() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVE_CHAT);
        receiveChatReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!intent.getStringExtra(USER_ID).equals(mUserId)) {
                    mChatAdaptor.addMessage(new ChatModel("other", intent.getStringExtra(MESSAGE), false));
                    mChatAdaptor.notifyDataSetChanged();
                }
            }
        };
        registerReceiver(receiveChatReceiver, intentFilter);
    }
}
