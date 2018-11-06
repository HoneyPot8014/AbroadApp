package com.example.leeyh.abroadapp.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Base64;
import android.util.Log;

import com.example.leeyh.abroadapp.constants.SocketEvent;
import com.example.leeyh.abroadapp.constants.Statistical;
import com.example.leeyh.abroadapp.dataconverter.DataConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTING;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SAVE_LOCATION_SUCCESS;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SIGN_UP;
import static com.example.leeyh.abroadapp.constants.StaticString.BROADCAST;
import static com.example.leeyh.abroadapp.constants.StaticString.CITY;
import static com.example.leeyh.abroadapp.constants.StaticString.EMIT_EVENT;
import static com.example.leeyh.abroadapp.constants.StaticString.JSON_DATA;
import static com.example.leeyh.abroadapp.constants.StaticString.NICKNAME;
import static com.example.leeyh.abroadapp.constants.StaticString.ON_EVENT;
import static com.example.leeyh.abroadapp.constants.StaticString.PROFILE;
import static com.example.leeyh.abroadapp.constants.StaticString.RECEIVED_DATA;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_Id;

public class BackgroundService extends Service {

    private Socket mSocket;
    private Statistical mAppStatic;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mNotificationBuilder;
    private PendingIntent mPedningIntent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        IO.Options socketOptions = new IO.Options();
        socketOptions.reconnection = true;
        socketOptions.forceNew = true;
        try {
            mSocket = IO.socket(SocketEvent.DEFAULT_HOST);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            //not connect handling

        }
        mSocket.connect();
        mAppStatic = (Statistical) getApplication();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.getStringExtra(ROUTING) != null) {
                String routeString = intent.getStringExtra(ROUTING);
                mSocket = mSocket.io().socket(routeString);
                mSocket.connect();
            } else if (intent.getStringExtra(EMIT_EVENT) != null) {

                if (intent.getStringExtra(EMIT_EVENT).equals(SIGN_UP)) {
                    String event = intent.getStringExtra(EMIT_EVENT);
                    String data = intent.getStringExtra(JSON_DATA);
                    try {
                        JSONObject jsonData = new JSONObject(data);
                        String id = jsonData.getString(USER_Id);
                        String profileByteArrayToString = DataConverter.getByteArrayToStringFromBitmap(mAppStatic.getBitmapFromMemoryCache(id));
                        jsonData.put(PROFILE, profileByteArrayToString);
                        mSocket.emit(event, jsonData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    String event = intent.getStringExtra(EMIT_EVENT);
                    String StringData = intent.getStringExtra(JSON_DATA);
                    try {
                        JSONObject jsonData = new JSONObject(StringData);
                        mSocket.emit(event, jsonData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (intent.getStringExtra(ON_EVENT) != null) {
                if (intent.getStringExtra(ON_EVENT).equals(SAVE_LOCATION_SUCCESS)) {
                    mSocket.on(SAVE_LOCATION_SUCCESS, new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            try {
                                JSONArray memberDataArray = new JSONArray();
                                JSONArray receivedMemberDataArray = new JSONArray(args[0].toString());
                                Log.d("여기 뭐지 2", "call: " + receivedMemberDataArray + receivedMemberDataArray.length());
                                for (int i = 0; i < receivedMemberDataArray.length(); i++) {
                                    JSONObject receivedMemberData = new JSONObject(receivedMemberDataArray.get(i).toString());
                                    JSONObject memberData = new JSONObject();
                                    memberData.put(USER_Id, receivedMemberData.getString(USER_Id));
                                    memberData.put(NICKNAME, receivedMemberData.getString(NICKNAME));
                                    memberData.put(CITY, receivedMemberData.getString(CITY));
                                    memberDataArray.put(memberData);
                                    Log.d("여기 뭐지", "call: " + receivedMemberData.getString(PROFILE));
                                    if (receivedMemberData.getString(PROFILE) != null) {

                                        byte[] memberProfileByteArray = DataConverter.getByteArrayFromString(receivedMemberData.getString(PROFILE));
                                        Bitmap memberProfileBitmap = BitmapFactory.decodeByteArray(memberProfileByteArray, 0, memberProfileByteArray.length);
                                        Log.d("여기 뭐지 bitmap", "call: " + memberProfileBitmap);
                                        Log.d("여기 뭐지 id", "call: " + receivedMemberData.getString(USER_Id));
                                        mAppStatic.addBitmapToMemoryCache(receivedMemberData.getString(USER_Id), memberProfileBitmap);
                                    }
                                }
                                Intent broadCast = new Intent(BROADCAST);
                                broadCast.putExtra(ON_EVENT, SAVE_LOCATION_SUCCESS);
                                broadCast.putExtra(RECEIVED_DATA, memberDataArray.toString());
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadCast);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });


                } else {
                    String event = intent.getStringExtra(ON_EVENT);
                    setOnEvent(mSocket, event);
                }
            }
        }
        return START_STICKY;
    }

    public void setOnEvent(Socket socket, final String event) {
        socket.on(event, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String receivedDataToString = Arrays.toString(args);
                Intent broadCast = new Intent(BROADCAST);
                broadCast.putExtra(ON_EVENT, event);
                broadCast.putExtra(RECEIVED_DATA, receivedDataToString);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadCast);
            }
        });
    }
}
