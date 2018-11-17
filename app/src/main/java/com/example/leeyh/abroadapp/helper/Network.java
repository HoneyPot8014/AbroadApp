package com.example.leeyh.abroadapp.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class Network {

    public static void getProfile(final ApplicationManagement appManager, final Context context, final ImageView imageView, final String id) {
        Glide.with(context).asBitmap().load("http://49.236.137.55/profile?id=" + id + ".jpeg").listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                Toast.makeText(context, "이미지 로딩 실패", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                appManager.addBitmapToMemoryCache(id, resource);
                imageView.setImageBitmap(resource);
                return false;
            }
        }).submit();
    }
}
