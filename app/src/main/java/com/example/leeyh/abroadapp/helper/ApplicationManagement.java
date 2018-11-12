package com.example.leeyh.abroadapp.helper;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.LruCache;

import com.example.leeyh.abroadapp.background.OnResponseReceivedListener;
import com.example.leeyh.abroadapp.constants.SocketEvent;

import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.example.leeyh.abroadapp.constants.StaticString.CACHE_SIZE;

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

        try {
            mSocket = IO.socket(SocketEvent.DEFAULT_HOST, socketOptions);
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        bitmapLruCache = new LruCache<String, Bitmap>(CACHE_SIZE) {
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
        mSocket.off();
        mSocket = mSocket.io().socket(route).connect();
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

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            bitmapLruCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemoryCache(String key) {
        return bitmapLruCache.get(key);
    }
}
