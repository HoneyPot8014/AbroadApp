package com.example.leeyh.abroadapp.helper;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.example.leeyh.abroadapp.constants.SocketEvent;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

import static com.example.leeyh.abroadapp.constants.StaticString.CHACHE_SIZE;

public class ApplicationManagement extends Application {

    private LruCache<String, Bitmap> bitmapLruCache;
    private Socket mSocket;

    @Override
    public void onCreate() {
        super.onCreate();

        IO.Options socketOptions = new IO.Options();
        socketOptions.forceNew = false;
        try {
            mSocket = IO.socket(SocketEvent.DEFAULT_HOST, socketOptions);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            //not connect handling
        }
        mSocket.connect();

        bitmapLruCache = new LruCache<String, Bitmap>(CHACHE_SIZE) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
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
