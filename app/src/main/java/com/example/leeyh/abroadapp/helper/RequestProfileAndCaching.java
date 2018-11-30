package com.example.leeyh.abroadapp.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class RequestProfileAndCaching extends AsyncTask<String, Void, String> {

    private ImageView mImageView;
    private ApplicationManagement mAppManager;

    public RequestProfileAndCaching(ImageView mImageView, Context mContext) {
        this.mImageView = mImageView;
        this.mAppManager = (ApplicationManagement) mContext.getApplicationContext();
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d("에이싱크", "doInBackground: " + strings[0]);
        InputStream inputStream = null;
        try {
            inputStream= (InputStream) new URL("http://49.236.137.55/profile?id=" + strings[0] + ".jpeg").getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        mAppManager.addBitmapToMemoryCache(strings[0], bitmap);
//        onPostExecute(strings[0]);
        return strings[0];
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d("에이싱크2", "onPostExecute: " + s);
        mImageView.setImageBitmap(mAppManager.getBitmapFromMemoryCache(s));
        super.onPostExecute(s);
    }
}
