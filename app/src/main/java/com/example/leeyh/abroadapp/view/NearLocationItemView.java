package com.example.leeyh.abroadapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.constants.Statistical;

public class NearLocationItemView extends LinearLayout {

    private ImageView mImageView;
    private TextView mNickNameTextView;
    private TextView mLocateTextView;
    private Statistical mAppStatic;

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

        mAppStatic = (Statistical) context.getApplicationContext();
        mImageView = findViewById(R.id.location_profile_image_view);
        mNickNameTextView = findViewById(R.id.location_nick_name_text_view);
        mLocateTextView = findViewById(R.id.location_locate_text_view);
    }

    public void setImageView(String id) {
        Bitmap bitmap = mAppStatic.getBitmapFromMemoryCache(id);
        mImageView.setImageBitmap(bitmap);

    }

    public void setNickName(String nickName) {
        mNickNameTextView.setText(nickName);
    }

    public void setLocate(String locate) {
        mLocateTextView.setText(locate);
    }

}
