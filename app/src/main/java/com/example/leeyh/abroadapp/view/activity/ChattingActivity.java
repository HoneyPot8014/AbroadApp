package com.example.leeyh.abroadapp.view.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.background.OnResponseReceivedListener;
import com.example.leeyh.abroadapp.controller.ChatMessageAdapter;
import com.example.leeyh.abroadapp.helper.ApplicationManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.leeyh.abroadapp.constants.SocketEvent.CHAT_MESSAGE;
import static com.example.leeyh.abroadapp.constants.SocketEvent.CHAT_MESSAGE_SUCCESS;
import static com.example.leeyh.abroadapp.constants.SocketEvent.RECEIVE_MESSAGE;
import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTE_CHAT;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SEND_MESSAGE;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SEND_MESSAGE_FAILED;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SEND_MESSAGE_SUCCESS;
import static com.example.leeyh.abroadapp.constants.StaticString.MESSAGE;
import static com.example.leeyh.abroadapp.constants.StaticString.MESSAGE_FROM_SERVICE;
import static com.example.leeyh.abroadapp.constants.StaticString.ROOM_NAME;
import static com.example.leeyh.abroadapp.constants.StaticString.SEND_MESSAGE_ID;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_INFO;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_NAME;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_UUID;

public class ChattingActivity extends AppCompatActivity implements OnResponseReceivedListener{

    private String mRoomName;
    private ApplicationManagement mAppManager;
    private ListView mChatMessageListView;
    private ChatMessageAdapter mChatMessageAdaptor;
    private EditText mChatMessageEditText;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        Log.d("11chat Activity", "onCreate: ");

        if(getIntent().getStringExtra(ROOM_NAME) != null) {
            mRoomName = getIntent().getStringExtra(ROOM_NAME);
        }

        mAppManager = (ApplicationManagement) getApplicationContext().getApplicationContext();
        mAppManager.setOnResponseReceivedListener(this);
        mAppManager.routeSocket(ROUTE_CHAT);
        mSharedPreferences = getApplicationContext().getSharedPreferences(USER_INFO, MODE_PRIVATE);
        mChatMessageAdaptor = new ChatMessageAdapter();
        mAppManager.onResponseFromServer(CHAT_MESSAGE_SUCCESS);
        mAppManager.onResponseFromServer(SEND_MESSAGE_SUCCESS);
        mAppManager.onResponseFromServer(SEND_MESSAGE_FAILED);
        mAppManager.onResponseFromServer(RECEIVE_MESSAGE);

        mChatMessageListView = findViewById(R.id.room_chat_message_list_view);
        mChatMessageListView.setAdapter(mChatMessageAdaptor);
        mChatMessageEditText = findViewById(R.id.room_chat_message_edit_text);
        Button sendMessageButton = findViewById(R.id.room_chat_send_button);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        getChattingMessage();
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        broadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String room = intent.getStringExtra(ROOM_NAME);
                if(!room.equals(mRoomName)) {
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        int importance = NotificationManager.IMPORTANCE_HIGH;
                        NotificationChannel channel = new NotificationChannel("channel", "name", importance);
                        notificationManager.createNotificationChannel(channel);
                    }
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "channel");

                    Intent mNotificationIntent = new Intent(getApplicationContext(), ChattingActivity.class);
                    mNotificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    mNotificationIntent.putExtra(ROOM_NAME, room);
                    int requestID = (int) System.currentTimeMillis();
                    PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext()
                            , requestID
                            , mNotificationIntent
                            , PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentTitle(intent.getStringExtra(SEND_MESSAGE_ID)) // required
                            .setContentText(intent.getStringExtra(MESSAGE))  // required
                            .setDefaults(Notification.DEFAULT_ALL) // 알림, 사운드 진동 설정
                            .setAutoCancel(true) // 알림 터치시 반응 후 삭제
                            .setSound(RingtoneManager
                                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setSmallIcon(android.R.drawable.btn_star)
//                            .setLargeIcon(BitmapFactory.decodeResource(getResources()
//                                    , R.drawable.msg_icon))
//                            .setBadgeIconType(R.drawable.msg_icon)
                            .setContentIntent(mPendingIntent);
                    notificationManager.notify(0, builder.build());
//                    Intent otherChatMessage = new Intent(getApplicationContext(), ChattingActivity.class);
//                    otherChatMessage.putExtra(ROOM_NAME, room);
//                    otherChatMessage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP & Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    startActivity(otherChatMessage);
//                    finish();
                }
            }
        }, new IntentFilter(MESSAGE_FROM_SERVICE));
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("11chat Activity", "onStop: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("11chat Activity", "onResume: ");
    }

    @Override
    public void onResponseReceived(String onEvent, Object[] object) {
        switch (onEvent) {
            case CHAT_MESSAGE_SUCCESS:
                JSONArray chatMessageList = (JSONArray) object[0];
                mChatMessageAdaptor.addList(chatMessageList);
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        mChatMessageAdaptor.notifyDataSetChanged();
                        mChatMessageListView.setSelection(mChatMessageAdaptor.getCount() - 1);
                    }
                }.sendEmptyMessage(0);
                break;

            case RECEIVE_MESSAGE:
                JSONObject receivedMessage = (JSONObject) object[0];
                try {
                    if(mRoomName.equals(receivedMessage.getString(ROOM_NAME))) {
                        mChatMessageAdaptor.addItem(receivedMessage);
                        new Handler(Looper.getMainLooper()) {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                mChatMessageAdaptor.notifyDataSetChanged();
                                mChatMessageListView.setSelection(mChatMessageAdaptor.getCount() - 1);
                            }
                        }.sendEmptyMessage(0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case SEND_MESSAGE_SUCCESS:
                break;

            case SEND_MESSAGE_FAILED:
                break;
        }
    }

    public void getChattingMessage() {
        JSONObject chatMessageData = new JSONObject();
        try {
            chatMessageData.put(ROOM_NAME, mRoomName);
            mAppManager.emitRequestToServer(CHAT_MESSAGE, chatMessageData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage() {
        JSONObject sendMessageData = new JSONObject();
        try {
            sendMessageData.put(USER_NAME, mSharedPreferences.getString(USER_NAME, null));
            sendMessageData.put(USER_UUID, mSharedPreferences.getString(USER_UUID, null));
            sendMessageData.put(ROOM_NAME, mRoomName);
            sendMessageData.put(MESSAGE, mChatMessageEditText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAppManager.emitRequestToServer(SEND_MESSAGE, sendMessageData);
        mChatMessageEditText.setText("");
    }
}
