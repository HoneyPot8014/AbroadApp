package com.example.leeyh.abroadapp.view.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.background.OnResponseReceivedListener;
import com.example.leeyh.abroadapp.helper.ApplicationManagement;
import com.example.leeyh.abroadapp.model.ChatListModel;
import com.example.leeyh.abroadapp.view.fragment.ChatListFragment;
import com.example.leeyh.abroadapp.view.fragment.ChattingFragment;
import com.example.leeyh.abroadapp.view.fragment.LocationFragment;
import com.example.leeyh.abroadapp.view.fragment.OnChatListItemClicked;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.leeyh.abroadapp.constants.SocketEvent.CHAT_CONNECT;
import static com.example.leeyh.abroadapp.constants.SocketEvent.CHAT_CONNECT_FAILED;
import static com.example.leeyh.abroadapp.constants.SocketEvent.CHAT_CONNECT_SUCCESS;
import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTE_CHAT;
import static com.example.leeyh.abroadapp.constants.StaticString.CHAT_LIST_FRAGMENT;
import static com.example.leeyh.abroadapp.constants.StaticString.LOCATION_FRAGMENT;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_ID;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_INFO;

public class TabBarMainActivity extends AppCompatActivity implements OnResponseReceivedListener, OnChatListItemClicked {

    private LocationFragment mLocationFragment;
    private ApplicationManagement mAppManager;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_bar_main);

        mAppManager = (ApplicationManagement) getApplication();
        mAppManager.setOnResponseReceivedListener(this);
        mAppManager.routeSocket(ROUTE_CHAT);
        mAppManager.onResponseFromServer(CHAT_CONNECT_SUCCESS);

        mFragmentManager = getSupportFragmentManager();

        emitToServerConnectedToChatNameSpace();
        mLocationFragment = new LocationFragment();
        Button locationTabButton = findViewById(R.id.location_tab_button);
        locationTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFragmentManager.findFragmentById(R.id.main_activity_container) instanceof LocationFragment)
                    return;
                mFragmentManager.beginTransaction().replace(R.id.main_activity_container, mLocationFragment, LOCATION_FRAGMENT).commitAllowingStateLoss();
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
    }

    @Override
    public void onResponseReceived(String onEvent, Object[] object) {
        switch (onEvent) {
            case CHAT_CONNECT_SUCCESS:
                mFragmentManager.beginTransaction().replace(R.id.main_activity_container, mLocationFragment, LOCATION_FRAGMENT).commitAllowingStateLoss();
                break;
            case CHAT_CONNECT_FAILED:
                break;
        }
    }

    public void emitToServerConnectedToChatNameSpace() {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        JSONObject requestChatConnectData = new JSONObject();
        try {
            requestChatConnectData.put(USER_ID, sharedPreferences.getString(USER_ID, null));
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
    public void onChatItemClicked(ChatListModel chatListModel) {
        String roomName = chatListModel.getRoomName();
        mFragmentManager.beginTransaction().replace(R.id.main_activity_container, ChattingFragment.newChattingFragmentInstance(roomName)).addToBackStack(null).commitAllowingStateLoss();
    }
}
