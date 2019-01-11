package com.example.leeyh.abroadapp.bindadapters;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.leeyh.abroadapp.R;

public class BindingAdapters {

    @BindingAdapter({"setProfileImage"})
    public static void setProfile(ImageView imageView, Bitmap bitmap) {
        if(bitmap == null) {
            imageView.setImageResource(R.drawable.user);
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    @BindingAdapter({"setSignUpExtraProfile"})
    public static void setExtraSignUpProfile(ImageView imageView, Bitmap bitmap) {
        if(bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
