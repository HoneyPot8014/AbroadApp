package com.example.leeyh.abroadapp.bindadapters;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.adapters.LocationUserAdapter;
import com.example.leeyh.abroadapp.model.UserModel;

import java.util.ArrayList;

public class BindingAdapters {


    @BindingAdapter({"setProfileImage"})
    public static void setProfile(ImageView imageView, Bitmap bitmap) {
        if (bitmap == null) {
            imageView.setImageResource(R.drawable.user);
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    @BindingAdapter({"setSignUpExtraProfile"})
    public static void setExtraSignUpProfile(ImageView imageView, Bitmap bitmap) {
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
    }

    @BindingAdapter({"setUser"})
    public static void setUser(RecyclerView recyclerView, MutableLiveData<ArrayList<UserModel>> userList) {
        final LocationUserAdapter adapter = (LocationUserAdapter) recyclerView.getAdapter();
        userList.observeForever(new Observer<ArrayList<UserModel>>() {
            @Override
            public void onChanged(@Nullable ArrayList<UserModel> userModels) {
                adapter.setItems(userModels);
            }
        });
    }
}
