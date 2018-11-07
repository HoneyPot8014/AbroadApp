package com.example.leeyh.abroadapp.constants;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.LruCache;

public class Statistical extends Application{

    LruCache<String, Bitmap> bitmapLruCache;

    @Override
    public void onCreate() {
        super.onCreate();

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 6;

        bitmapLruCache = new LruCache<String, Bitmap>(cacheSize) {
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
