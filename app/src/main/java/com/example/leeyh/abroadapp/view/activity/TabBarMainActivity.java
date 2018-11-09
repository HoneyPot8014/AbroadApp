package com.example.leeyh.abroadapp.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.background.OnResponseReceivedListener;
import com.example.leeyh.abroadapp.background.ServiceEventInterface;
import com.example.leeyh.abroadapp.view.fragment.ChatListFragment;
import com.example.leeyh.abroadapp.view.fragment.LocationFragment;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.leeyh.abroadapp.constants.SocketEvent.CHAT_CONNECT;
import static com.example.leeyh.abroadapp.constants.SocketEvent.CHAT_CONNECT_SUCCESS;
import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTE_CHAT;
import static com.example.leeyh.abroadapp.constants.StaticString.ON_EVENT;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_ID;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_INFO;

public class TabBarMainActivity extends AppCompatActivity implements OnResponseReceivedListener {

    private LocationFragment mLocationFragment;
    private ServiceEventInterface mSocketListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_bar_main);

        mSocketListener = new ServiceEventInterface(getApplicationContext());
        mSocketListener.socketRouting(ROUTE_CHAT);
        mSocketListener.setResponseListener(this);
        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        try {
            JSONObject requestChatConnectData = new JSONObject();
            requestChatConnectData.put(USER_ID, sharedPreferences.getString(USER_ID, null));
            mSocketListener.socketOnEvent(CHAT_CONNECT_SUCCESS);
            mSocketListener.socketEmitEvent(CHAT_CONNECT, requestChatConnectData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
    public void onResponseReceived(Intent intent) {
        String event = intent.getStringExtra(ON_EVENT);
        switch (event) {
            case CHAT_CONNECT_SUCCESS:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_container, mLocationFragment).commitAllowingStateLoss();
                break;
        }

    }
}
