package com.example.leeyh.abroadapp.helper;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.LruCache;

import com.example.leeyh.abroadapp.background.BackgroundChattingService;
import com.example.leeyh.abroadapp.background.OnResponseReceivedListener;
import com.example.leeyh.abroadapp.constants.SocketEvent;
import com.example.leeyh.abroadapp.view.activity.TabBarMainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.logging.Handler;

import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.example.leeyh.abroadapp.constants.SocketEvent.CHECK_SIGNED;
import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTE_CHAT;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SIGNED_USER;
import static com.example.leeyh.abroadapp.constants.StaticString.CACHE_SIZE;
import static com.example.leeyh.abroadapp.constants.StaticString.PASSWORD;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_ID;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_INFO;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_UUID;

public class ApplicationManagement extends Application {

    private LruCache<String, Bitmap> bitmapLruCache;
    private Socket mSocket;
    private OnResponseReceivedListener onResponseReceivedListener;

    @Override
    public void onCreate() {
        super.onCreate();
        IO.Options socketOptions = new IO.Options();
        socketOptions.reconnection = true;
        socketOptions.forceNew = false;
        socketOptions.multiplex = false;
        socketOptions.secure = true;
        socketOptions.timeout = 1000 * 60 * 100;
        try {
            mSocket = IO.socket(SocketEvent.DEFAULT_HOST, socketOptions);
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        autoSignIn(mSocket);

        bitmapLruCache = new LruCache<String, Bitmap>(CACHE_SIZE) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };

        final Intent intent = new Intent(getApplicationContext(), BackgroundChattingService.class);
        startService(intent);
    }

    public void autoSignIn(Socket socket) {
        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        if(sharedPreferences.getString(USER_ID, null) != null && sharedPreferences.getString(PASSWORD, null) != null
                && sharedPreferences.getString(USER_UUID, null) != null) {
            JSONObject checkSignedData = new JSONObject();
            try {
                checkSignedData.put(USER_ID, sharedPreferences.getString(USER_ID, null));
                checkSignedData.put(PASSWORD, sharedPreferences.getString(PASSWORD, null));
                checkSignedData.put(USER_UUID, sharedPreferences.getString(USER_UUID, null));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            socket.emit(CHECK_SIGNED, checkSignedData);
        }

        socket.on(SIGNED_USER, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                routeSocket(ROUTE_CHAT);
            }
        });
    }

    public void setOnResponseReceivedListener(OnResponseReceivedListener listener) {
        onResponseReceivedListener = listener;
    }

    public void routeSocket(String route) {
        mSocket.off();
        mSocket = mSocket.io().socket(route).connect();
    }

    public void emitRequestToServer(String emitEvent, JSONObject jsonObject) {
        mSocket.emit(emitEvent, jsonObject);
    }

    public void emitRequestToServer(String emitEvent, JSONArray jsonArray) {
        mSocket.emit(emitEvent, jsonArray);
    }

    public void onResponseFromServer(final String onEvent) {
        mSocket.on(onEvent, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                onResponseReceivedListener.onResponseReceived(onEvent, args);
            }
        });
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            bitmapLruCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemoryCache(String key) {
        return bitmapLruCache.get(key);
    }
}
