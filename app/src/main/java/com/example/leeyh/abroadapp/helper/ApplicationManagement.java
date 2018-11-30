package com.example.leeyh.abroadapp.helper;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.example.leeyh.abroadapp.background.BackgroundChattingService;
import com.example.leeyh.abroadapp.background.OnResponseReceivedListener;
import com.example.leeyh.abroadapp.background.SocketResponseListener;
import com.example.leeyh.abroadapp.constants.SocketEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.example.leeyh.abroadapp.constants.SocketEvent.CHAT_CONNECT;
import static com.example.leeyh.abroadapp.constants.SocketEvent.CHECK_SIGNED;
import static com.example.leeyh.abroadapp.constants.SocketEvent.RECEIVE_MESSAGE;
import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTE_CHAT;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SIGNED_USER;
import static com.example.leeyh.abroadapp.constants.StaticString.CACHE_SIZE;
import static com.example.leeyh.abroadapp.constants.StaticString.PASSWORD;
import static com.example.leeyh.abroadapp.constants.StaticString.RECEIVED_DATA;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_ID;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_INFO;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_UUID;

public class ApplicationManagement extends Application implements SocketResponseListener {

    private LruCache<String, Bitmap> bitmapLruCache;
    private Socket mSocket;
    private OnResponseReceivedListener onResponseReceivedListener;
    private SharedPreferences mSharedPreferences;

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
            Log.d("서비스10", "onCreate: ");
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
        Log.d("서비스", "onCreate: 서비스 시작됨");
    }

    public void autoSignIn(Socket socket) {
        mSharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        if (mSharedPreferences.getString(USER_ID, null) != null && mSharedPreferences.getString(PASSWORD, null) != null
                && mSharedPreferences.getString(USER_UUID, null) != null) {
            JSONObject checkSignedData = new JSONObject();
            try {
                checkSignedData.put(USER_ID, mSharedPreferences.getString(USER_ID, null));
                checkSignedData.put(PASSWORD, mSharedPreferences.getString(PASSWORD, null));
                checkSignedData.put(USER_UUID, mSharedPreferences.getString(USER_UUID, null));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            socket.emit(CHECK_SIGNED, checkSignedData);
        }

        socket.on(SIGNED_USER, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("서비스 채팅으로 가세요", "call: ");
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
        if (route.equals(ROUTE_CHAT)) {
            JSONObject chatConnectData = new JSONObject();
            try {
                chatConnectData.put(USER_ID, mSharedPreferences.getString(USER_ID, null));
                chatConnectData.put(USER_UUID, mSharedPreferences.getString(USER_UUID, null));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mSocket.emit(CHAT_CONNECT, chatConnectData);
            mSocket.on(RECEIVE_MESSAGE, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject receivedData = (JSONObject) args[0];
                    Intent service = new Intent(getApplicationContext(), BackgroundChattingService.class);
                    service.putExtra(RECEIVED_DATA, receivedData.toString());
                    startService(service);
                }
            });
        }
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

    @Override
    public void onResponseFromServer(String onEvent, Object... args) {
        onResponseReceivedListener.onResponseReceived(onEvent, args);
    }
}
