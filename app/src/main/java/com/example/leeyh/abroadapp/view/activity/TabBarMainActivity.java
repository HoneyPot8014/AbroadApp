package com.example.leeyh.abroadapp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.background.OnResponseReceivedListener;
import com.example.leeyh.abroadapp.helper.ApplicationManagement;
import com.example.leeyh.abroadapp.view.fragment.ChatListFragment;
import com.example.leeyh.abroadapp.view.fragment.LocationFragment;
import com.example.leeyh.abroadapp.view.fragment.OnChatListItemClicked;
import com.example.leeyh.abroadapp.view.fragment.OnNewChatRoomMaked;
import com.example.leeyh.abroadapp.view.fragment.TravelPlanFragment;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.leeyh.abroadapp.constants.SocketEvent.CHAT_CONNECT;
import static com.example.leeyh.abroadapp.constants.SocketEvent.CHAT_CONNECT_FAILED;
import static com.example.leeyh.abroadapp.constants.SocketEvent.CHAT_CONNECT_SUCCESS;
import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTE_CHAT;
import static com.example.leeyh.abroadapp.constants.StaticString.CHAT_LIST_FRAGMENT;
import static com.example.leeyh.abroadapp.constants.StaticString.IS_FOREGROUND;
import static com.example.leeyh.abroadapp.constants.StaticString.LOCATION_FRAGMENT;
import static com.example.leeyh.abroadapp.constants.StaticString.MESSAGE_FROM_SERVICE;
import static com.example.leeyh.abroadapp.constants.StaticString.ROOM_NAME;
import static com.example.leeyh.abroadapp.constants.StaticString.TRAVEL_LIST_FRAGMENT;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_INFO;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_NAME;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_UUID;

public class TabBarMainActivity extends AppCompatActivity implements OnResponseReceivedListener, OnChatListItemClicked, OnNewChatRoomMaked {

    private ApplicationManagement mAppManager;
    private FragmentManager mFragmentManager;
//    private int travelCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_bar_main);

        mAppManager = (ApplicationManagement) getApplication();
        mAppManager.setOnResponseReceivedListener(this);
        mAppManager.routeSocket(ROUTE_CHAT);
        mAppManager.onResponseFromServer(CHAT_CONNECT_SUCCESS);
        mAppManager.onResponseFromServer(CHAT_CONNECT_FAILED);
        mFragmentManager = getSupportFragmentManager();

        emitToServerConnectedToChatNameSpace();
        Button locationTabButton = findViewById(R.id.location_tab_button);
        locationTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFragmentManager.findFragmentById(R.id.main_activity_container) instanceof LocationFragment)
                    return;
                mFragmentManager.beginTransaction().replace(R.id.main_activity_container, new LocationFragment(), LOCATION_FRAGMENT).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        Button chatListTabButton = findViewById(R.id.chat_tab_button);
        chatListTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFragmentManager.findFragmentById(R.id.main_activity_container) instanceof ChatListFragment)
                    return;
                mFragmentManager.beginTransaction().replace(R.id.main_activity_container, new ChatListFragment(), CHAT_LIST_FRAGMENT).commitAllowingStateLoss();
            }
        });

        if (getIntent().getStringExtra(MESSAGE_FROM_SERVICE) != null) {
            String roomName = getIntent().getStringExtra(ROOM_NAME);
            mFragmentManager.beginTransaction().replace(R.id.main_activity_container, ChatListFragment.newChattingFragmentInstance(roomName)).commitAllowingStateLoss();
        }

        Button manage_travel_tab_button = findViewById(R.id.manage_travel_tab_button);
        manage_travel_tab_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFragmentManager.findFragmentById(R.id.main_activity_container) instanceof TravelPlanFragment)
                    return;
                mFragmentManager.beginTransaction().replace(R.id.main_activity_container, new TravelPlanFragment(),TRAVEL_LIST_FRAGMENT ).commitAllowingStateLoss();

            }
        });


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mFragmentManager.putFragment(outState, "fragment", mFragmentManager.findFragmentById(R.id.main_activity_container));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Fragment fragment = mFragmentManager.getFragment(savedInstanceState, "fragment");
        mFragmentManager.beginTransaction().replace(R.id.main_activity_container, fragment);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getStringExtra(MESSAGE_FROM_SERVICE) != null) {
            String roomName = intent.getStringExtra(ROOM_NAME);
            mFragmentManager.beginTransaction().replace(R.id.main_activity_container, ChatListFragment.newChattingFragmentInstance(roomName)).commitAllowingStateLoss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResponseReceived(String onEvent, Object[] object) {
        switch (onEvent) {
            case CHAT_CONNECT_SUCCESS:
                mFragmentManager.beginTransaction().replace(R.id.main_activity_container, new LocationFragment(), LOCATION_FRAGMENT).commitAllowingStateLoss();
                break;
            case CHAT_CONNECT_FAILED:
                break;
        }
    }

    public void emitToServerConnectedToChatNameSpace() {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        JSONObject requestChatConnectData = new JSONObject();
        try {
            requestChatConnectData.put(USER_NAME, sharedPreferences.getString(USER_NAME, null));
            requestChatConnectData.put(USER_UUID, sharedPreferences.getString(USER_UUID, null));
            requestChatConnectData.put(IS_FOREGROUND, true);
            mAppManager.emitRequestToServer(CHAT_CONNECT, requestChatConnectData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAppManager = null;
    }

    @Override
    public void onChatItemClicked(JSONObject chatListModel) {
        try {
            String roomName = chatListModel.getString(ROOM_NAME);
//            mFragmentManager.beginTransaction().replace(R.id.main_activity_container, ChattingFragment.newChattingFragmentInstance(roomName)).addToBackStack(null).commitAllowingStateLoss();
            Intent goToChatting = new Intent(getApplicationContext(), ChattingActivity.class);
            goToChatting.putExtra(ROOM_NAME, roomName);
            startActivity(goToChatting);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onChatNewChatMessage(String roomName) {
//        mFragmentManager.beginTransaction().replace(R.id.main_activity_container, ChatListFragment.newChattingFragmentInstance(roomName)).commitAllowingStateLoss();
        Intent goToChattingActivity = new Intent(getApplicationContext(), ChattingActivity.class);
        goToChattingActivity.putExtra(ROOM_NAME, roomName);
        goToChattingActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP & Intent.FLAG_ACTIVITY_SINGLE_TOP & Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goToChattingActivity);
    }

    @Override
    public void onNewChatRoomCreated(String roomName) {
        mFragmentManager.beginTransaction().replace(R.id.main_activity_container, ChatListFragment.newChattingFragmentInstance(roomName)).commitAllowingStateLoss();
    }
}
