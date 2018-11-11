package com.example.leeyh.abroadapp.view.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.background.OnResponseReceivedListener;
import com.example.leeyh.abroadapp.helper.ApplicationManagement;
import com.example.leeyh.abroadapp.view.fragment.ChatListFragment;
import com.example.leeyh.abroadapp.view.fragment.LocationFragment;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.leeyh.abroadapp.constants.SocketEvent.CHAT_CONNECT;
import static com.example.leeyh.abroadapp.constants.SocketEvent.CHAT_CONNECT_SUCCESS;
import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTE_CHAT;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_ID;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_INFO;

public class TabBarMainActivity extends AppCompatActivity implements OnResponseReceivedListener {

    private LocationFragment mLocationFragment;
    private ApplicationManagement mAppManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_bar_main);

        mAppManager = (ApplicationManagement) getApplication();
        mAppManager.setOnResponseReceivedListener(this);
        mAppManager.routeSocket(ROUTE_CHAT);
//        mAppManager.onResponseFromServer(CHAT_CONNECT_SUCCESS);

        emitToServerConnectedToChatNameSpace();
        mLocationFragment = new LocationFragment();
        Button locationTabButton = findViewById(R.id.location_tab_button);
        locationTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_container, mLocationFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        Button chatListTabButton = findViewById(R.id.chat_tab_button);
        chatListTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_container, new ChatListFragment()).commitAllowingStateLoss();
            }
        });
    }

    @Override
    public void onResponseReceived(String onEvent, Object[] object) {
        switch (onEvent) {
            case CHAT_CONNECT_SUCCESS:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_container, mLocationFragment).commitAllowingStateLoss();
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
//        mAppManager.unRegisterOnResponse(CHAT_CONNECT_SUCCESS);
    }
}
