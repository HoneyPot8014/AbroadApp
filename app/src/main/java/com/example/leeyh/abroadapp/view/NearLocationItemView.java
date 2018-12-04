package com.example.leeyh.abroadapp.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.helper.ApplicationManagement;
import com.example.leeyh.abroadapp.helper.RequestProfileAndCaching;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class NearLocationItemView extends LinearLayout {

    private ImageView mImageView;
    private TextView mNickNameTextView;
    private TextView mLocateTextView;
    private ApplicationManagement mAppStatic;

    public NearLocationItemView(Context context) {
        super(context);
        init(context);
    }

    public NearLocationItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.near_location_item_view, this, true);

        mAppStatic = (ApplicationManagement) context.getApplicationContext();
        mImageView = findViewById(R.id.location_profile_image_view);
        mNickNameTextView = findViewById(R.id.location_nick_name_text_view);
        mLocateTextView = findViewById(R.id.location_locate_text_view);


    }

    public void setImageView(String id, Context context) {

        RequestProfileAndCaching requestProfileAndCaching = new RequestProfileAndCaching(mImageView, context);
        requestProfileAndCaching.execute(id);

    }

    public void setNickName(String nickName) {
        mNickNameTextView.setText(nickName);
    }

    public void setLocate(String locate) {
        mLocateTextView.setText(locate);
    }

}
