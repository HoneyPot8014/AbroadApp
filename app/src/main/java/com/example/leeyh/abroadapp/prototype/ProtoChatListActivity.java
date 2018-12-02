//package com.example.leeyh.abroadapp.prototype;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//
//import com.example.leeyh.abroadapp.R;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import static com.example.leeyh.abroadapp.prototype.Constants.CHAT_ROUTE;
//import static com.example.leeyh.abroadapp.prototype.Constants.GET_CHAT_LIST;
//import static com.example.leeyh.abroadapp.prototype.Constants.INVITED_JOIN;
//import static com.example.leeyh.abroadapp.prototype.Constants.JOIN_SUCCESS;
//import static com.example.leeyh.abroadapp.prototype.Constants.REQUEST_JOIN;
//import static com.example.leeyh.abroadapp.prototype.Constants.SET_CHAT_LIST;
//import static com.example.leeyh.abroadapp.prototype.ProtoSignUpActivity.EVENT;
//import static com.example.leeyh.abroadapp.prototype.ProtoSignUpActivity.ROUTING;
//import static com.example.leeyh.abroadapp.prototype.ProtoSignUpActivity.USER_ID;
//
//public class ProtoChatListActivity extends AppCompatActivity {
//
//    public static final String ROOM_NAME = "roomName";
//    public static final String JOIN_MEMBERS = "joinMembers";
//    public static final String CHATS = "chats";
//    private ProtoChatListAdaptor mAdaptor;
//    private EditText mInviteEditText;
//    private String mUserId;
//    private BroadcastReceiver setChatListReceiver;
//    private BroadcastReceiver inviteJoinReceiver;
//    private BroadcastReceiver joinSuccessReceiver;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_proto_chat_list);
//
//        Log.d("생명주기", "onCreate: CHAT");
//        mUserId = getIntent().getStringExtra(USER_ID);
//
//        if(getIntent() != null) {
//            if(getIntent().getIntExtra("storedUser", 0) == 101) {
//                mUserId = getIntent().getStringExtra(USER_ID);
//            }
//        }
//        SharedPreferences preferences = getSharedPreferences("userInfo", MODE_PRIVATE);
//        mUserId = preferences.getString(USER_ID, null);
//
//        Intent service = new Intent(getApplicationContext(), ProtoBackgroundChat.class);
//        service.putExtra(ROUTING, CHAT_ROUTE);
//        startService(service);
//
//        ListView chatListView = findViewById(R.id.chat_list_view);
//        mAdaptor = new ProtoChatListAdaptor();
//        chatListView.setAdapter(mAdaptor);
//        mInviteEditText = findViewById(R.id.invite_id_edit_text_view);
//        Button inviteUserButton = findViewById(R.id.invite_user_button);
//        inviteUserButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onInviteUserButtonClicked();
//            }
//        });
//
//        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                String roomName = mAdaptor.getChatModel(i).getRoomName();
//                Intent goToChatRoom = new Intent(getApplicationContext(), ProtoRoomChatActivity.class);
//                goToChatRoom.putExtra(USER_ID, mUserId);
//                goToChatRoom.putExtra(ROOM_NAME, roomName);
//                startActivity(goToChatRoom);
//            }
//        });
//
//        getChatList();
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        setChatList();
//        invitedJoin();
//        joinSuccess();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        unregisterReceiver(setChatListReceiver);
//        unregisterReceiver(inviteJoinReceiver);
//        unregisterReceiver(joinSuccessReceiver);
//    }
//
//    private void onInviteUserButtonClicked() {
//        String inviteId = mInviteEditText.getText().toString();
//
//        Intent service = new Intent(getApplicationContext(), ProtoBackgroundChat.class);
//        service.putExtra(EVENT, REQUEST_JOIN);
//        service.putExtra(USER_ID, mUserId);
//        service.putExtra("invite", inviteId);
//        startService(service);
//    }
//
//    private void getChatList() {
//        Intent service = new Intent(getApplicationContext(), ProtoBackgroundChat.class);
//        service.putExtra(EVENT, GET_CHAT_LIST);
//        service.putExtra(USER_ID, mUserId);
//        startService(service);
//    }
//
//    private void setChatList() {
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(SET_CHAT_LIST);
//        setChatListReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                try {
//                    JSONArray chats = new JSONArray(intent.getStringExtra(CHATS));
//                    for (int i = 0; i < chats.length(); i++) {
//                        JSONObject chat = chats.optJSONObject(i);
//                        String roomName = chat.optString(ROOM_NAME);
//                        String members = chat.optString(JOIN_MEMBERS);
//                        mAdaptor.addItem(new ProtoChatModel(roomName, members));
//                    }
//                    mAdaptor.notifyDataSetChanged();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        registerReceiver(setChatListReceiver, intentFilter);
//    }
//
//    private void invitedJoin() {
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(INVITED_JOIN);
//        inviteJoinReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                mAdaptor.addItem(new ProtoChatModel(intent.getStringExtra(ROOM_NAME), intent.getStringExtra(JOIN_MEMBERS)));
//                mAdaptor.notifyDataSetChanged();
//            }
//        };
//        registerReceiver(inviteJoinReceiver, intentFilter);
//    }
//
//    private void joinSuccess() {
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(JOIN_SUCCESS);
//        joinSuccessReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Intent goToChatRoom = new Intent(getApplicationContext(), ProtoRoomChatActivity.class);
//                goToChatRoom.putExtra(USER_NAME, intent.getStringExtra(USER_NAME));
//                goToChatRoom.putExtra(ROOM_NAME, intent.getStringExtra(ROOM_NAME));
//                startActivity(goToChatRoom);
//            }
//        };
//        registerReceiver(joinSuccessReceiver, intentFilter);
//    }
//}
