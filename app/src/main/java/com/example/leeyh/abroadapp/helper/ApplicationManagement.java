package com.example.leeyh.abroadapp.helper;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.example.leeyh.abroadapp.background.BackgroundChattingService;
import com.example.leeyh.abroadapp.background.OnResponseReceivedListener;
import com.example.leeyh.abroadapp.background.SocketRequestListener;
import com.example.leeyh.abroadapp.background.SocketResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.example.leeyh.abroadapp.constants.SocketEvent.CHECK_SIGNED;
import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTE_CHAT;
import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTING;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SIGNED_USER;
import static com.example.leeyh.abroadapp.constants.StaticString.CACHE_SIZE;
import static com.example.leeyh.abroadapp.constants.StaticString.PASSWORD;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_ID;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_INFO;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_UUID;

public class ApplicationManagement extends Application implements SocketResponseListener{

    private LruCache<String, Bitmap> bitmapLruCache;
//    private Socket mSocket;
    private OnResponseReceivedListener onResponseReceivedListener;
    private SocketRequestListener mSocketRequestListener;

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(getApplicationContext(), BackgroundChattingService.class);
        startService(intent);
        bitmapLruCache = new LruCache<String, Bitmap>(CACHE_SIZE) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
        Log.d("서비스", "onCreate: 서비스 시작됨");
    }

    public void setListener(SocketRequestListener listener) {
        this.mSocketRequestListener = listener;
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
        Intent service = new Intent(getApplicationContext(), BackgroundChattingService.class);
        service.putExtra(ROUTING, route);
        startService(service);
//        mSocketRequestListener.socketRouting(route);
//        mSocket.off();
//        mSocket = mSocket.io().socket(route).connect();
//        if(route.equals(ROUTE_CHAT)) {
//            Log.d("서비스4", "call: 라우팅.");
//            mSocket.on(RECEIVE_MESSAGE, new Emitter.Listener() {
//                @Override
//                public void call(Object... args) {
//                    Log.d("서비스3", "call: 메세지 받는 곳.");
//                    JSONObject receivedData = (JSONObject) args[0];
//                    Intent service = new Intent(getApplicationContext(), BackgroundChattingService.class);
//                    service.putExtra(RECEIVED_DATA, receivedData.toString());
//                    startService(service);
//                }
//            });
//        }
    }

    public void emitRequestToServer(String emitEvent, JSONObject jsonObject) {
//        mSocketRequestListener.socketEmitEvent(emitEvent, jsonObject);
//        mSocket.emit(emitEvent, jsonObject);
    }

    public void emitRequestToServer(String emitEvent, JSONArray jsonArray) {
//        mSocketRequestListener.socketEmitEvent(emitEvent, jsonArray);
//        mSocket.emit(emitEvent, jsonArray);
    }

    public void onResponseFromServer(final String onEvent) {
//        mSocketRequestListener.socketOnEvent(onEvent);
//        mSocket.on(onEvent, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                onResponseReceivedListener.onResponseReceived(onEvent, args);
//            }
//        });
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
