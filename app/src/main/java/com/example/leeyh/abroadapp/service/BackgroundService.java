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
import static com.example.leeyh.abroadapp.constants.StaticString.SOCKET_ROUTED;
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
        mAppStatic = (Statistical) getApplicationContext();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.getStringExtra(ROUTING) != null) {
                String routeString = intent.getStringExtra(ROUTING);
                mSocket = mSocket.io().socket(routeString);
                mSocket.connect();
                Log.d("소켓이 라우팅 되어있냐??", "onStartCommand: " + mSocket.connected());
                onSocketRouted(mSocket, routeString);
            } else if (intent.getStringExtra(EMIT_EVENT) != null) {
                if (intent.getStringExtra(EMIT_EVENT).equals(SIGN_UP)) {
                    String event = intent.getStringExtra(EMIT_EVENT);
                    String data = intent.getStringExtra(JSON_DATA);
                    try {
                        JSONObject jsonData = new JSONObject(data);
                        String id = jsonData.getString(USER_Id);
                        jsonData.put(PROFILE, DataConverter.getByteArrayToStringFromBitmap(mAppStatic.getBitmapFromMemoryCache(id)));
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
                            JSONArray memberDataArray = (JSONArray) args[0];
                            for (int i = 0; i < memberDataArray.length(); i++) {
                                try {
                                    JSONObject jsonObject = (JSONObject) memberDataArray.get(i);
                                    String id = jsonObject.getString(USER_Id);
                                    byte[] bytes = (byte[]) jsonObject.get(PROFILE);
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    mAppStatic.addBitmapToMemoryCache(id, bitmap);

                                    Intent broadCast = new Intent(BROADCAST);
                                    broadCast.putExtra(ON_EVENT, SAVE_LOCATION_SUCCESS);
                                    broadCast.putExtra(RECEIVED_DATA, memberDataArray.toString());
                                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadCast);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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

    public void onSocketRouted(Socket socket, String route) {
        socket.on(route, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Intent broadCast = new Intent(SOCKET_ROUTED);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadCast);
            }
        });
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
