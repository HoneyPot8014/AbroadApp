package com.example.leeyh.abroadapp.helper;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.example.leeyh.abroadapp.background.OnResponseReceivedListener;
import com.example.leeyh.abroadapp.constants.SocketEvent;

import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.example.leeyh.abroadapp.constants.StaticString.CHACHE_SIZE;

public class ApplicationManagement extends Application {

    private LruCache<String, Bitmap> bitmapLruCache;
    private Socket mSocket;
    private OnResponseReceivedListener onResponseReceivedListener;

    @Override
    public void onCreate() {

        super.onCreate();

        IO.Options socketOptions = new IO.Options();
        socketOptions.forceNew = false;
        socketOptions.reconnection = true;
        socketOptions.multiplex = false;
        socketOptions.secure = true;
        try {
            mSocket = IO.socket(SocketEvent.DEFAULT_HOST, socketOptions);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            //not connect handling
        }
        mSocket.connect();
        Log.d("계속 만들어짐?", "onCreate: ");

        bitmapLruCache = new LruCache<String, Bitmap>(CHACHE_SIZE) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
    }

    public void setOnResponseReceivedListener(OnResponseReceivedListener listener) {
        onResponseReceivedListener = listener;
    }

    public void routeSocket(String route) {
        IO.Options options = new IO.Options();
        options.forceNew = false;
        options.reconnection = true;
        options.multiplex = false;
        options.secure = true;
        mSocket.disconnect();
        try {
            Socket newSocket = IO.socket(SocketEvent.DEFAULT_HOST + route, options);
            mSocket = newSocket;
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void emitRequestToServer(String emitEvent, JSONObject jsonObject) {
        mSocket.emit(emitEvent, jsonObject);
    }

    public void onResponseFromServer(final String onEvent) {
        mSocket.on(onEvent, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                onResponseReceivedListener.onResponseReceived(onEvent, args);
            }
        });
    }

    public void unRegisterOnResponse(String onEvent) {
        mSocket.off(onEvent);
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
