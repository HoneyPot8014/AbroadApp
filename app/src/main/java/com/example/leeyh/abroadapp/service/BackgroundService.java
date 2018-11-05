package com.example.leeyh.abroadapp.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.example.leeyh.abroadapp.constants.SocketEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Arrays;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTING;
import static com.example.leeyh.abroadapp.constants.StaticString.BROADCAST;
import static com.example.leeyh.abroadapp.constants.StaticString.EMIT_EVENT;
import static com.example.leeyh.abroadapp.constants.StaticString.JSON_DATA;
import static com.example.leeyh.abroadapp.constants.StaticString.ON_EVENT;
import static com.example.leeyh.abroadapp.constants.StaticString.RECEIVED_DATA;

public class BackgroundService extends Service {

    private Socket mSocket;
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
                String event = intent.getStringExtra(EMIT_EVENT);
                String StringData = intent.getStringExtra(JSON_DATA);
                try {
                    JSONObject jsonData = new JSONObject(StringData);
                    mSocket.emit(event, jsonData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if(intent.getStringExtra(ON_EVENT) != null) {
                String event = intent.getStringExtra(ON_EVENT);
                setOnEvent(mSocket, event);
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
