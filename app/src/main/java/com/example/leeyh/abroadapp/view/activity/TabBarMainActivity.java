package com.example.leeyh.abroadapp.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.background.OnResponseReceivedListener;
import com.example.leeyh.abroadapp.background.ServiceEventInterface;
import com.example.leeyh.abroadapp.view.fragment.LocationFragment;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.leeyh.abroadapp.constants.SocketEvent.CHAT;
import static com.example.leeyh.abroadapp.constants.SocketEvent.CHAT_CONNECT;
import static com.example.leeyh.abroadapp.constants.SocketEvent.CHAT_CONNECT_FAILED;
import static com.example.leeyh.abroadapp.constants.SocketEvent.CHAT_CONNECT_SUCCESS;
import static com.example.leeyh.abroadapp.constants.StaticString.ON_EVENT;
import static com.example.leeyh.abroadapp.prototype.ProtoSignUpActivity.USER_ID;

public class TabBarMainActivity extends AppCompatActivity implements OnResponseReceivedListener {

    private ServiceEventInterface mSocketListener;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_bar_main);

        mSocketListener = new ServiceEventInterface(getApplicationContext());
        //Routing namespace to '/signIn'
        mSocketListener.socketRouting(CHAT);
        mSocketListener.setResponseListener(this);

        //when socket connected namespace '/chat' emit event
        id = getIntent().getStringExtra(USER_ID);
        onChatNameSpaceConnected(id);

        mSocketListener.socketOnEvent(CHAT_CONNECT_SUCCESS);
        mSocketListener.socketOnEvent(CHAT_CONNECT_FAILED);

        Button locationTabButton = findViewById(R.id.location_tab_button);
        locationTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_container, LocationFragment.newInstance(id)).commit();
            }
        });

    }

    @Override
    public void onResponseReceived(Intent intent) {
        String event = intent.getStringExtra(ON_EVENT);
        switch (event) {
            case CHAT_CONNECT_SUCCESS:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_container
                        , LocationFragment.newInstance(id)).commit();
            case CHAT_CONNECT_FAILED:
                //handle here when chat connect failed
                break;
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mSocketListener.socketRouting(CHAT);
    }

    public void onChatNameSpaceConnected(String id) {
        JSONObject connectedConfirmData = new JSONObject();
        try {
            connectedConfirmData.put(USER_ID, id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String connectedConfirmDataToString = connectedConfirmData.toString();
        mSocketListener.socketEmitEvent(CHAT_CONNECT, connectedConfirmDataToString);
    }
}
