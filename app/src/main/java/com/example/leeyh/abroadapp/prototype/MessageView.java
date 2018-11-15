//package com.example.leeyh.abroadapp.view;
//
//import android.content.Context;
//import android.support.annotation.Nullable;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.example.leeyh.abroadapp.R;
//
//public class MessageView extends LinearLayout {
//
//    private ImageView mProfileImageView;
//    private TextView mOtherMessageView;
//    private TextView mMyMessageView;
//
//    public MessageView(Context context) {
//        super(context);
//        init(context);
//    }
//
//    public MessageView(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//        init(context);
//    }
//
//    private void init(Context context) {
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
//        inflater.inflate(R.layout.other_chat_message_item, this, true);
//
//        mProfileImageView = findViewById(R.id.other_profile_image_view);
//        mOtherMessageView = findViewById(R.id.other_chat_message_item_text_view);
//        mMyMessageView = findViewById(R.id.my_chat_message_item_text_view);
//    }
//
//    public ImageView getProfileImageView() {
//        return mProfileImageView;
//    }
//
//    public void setmProfileImageView(ImageView mProfileImageView) {
//        this.mProfileImageView = mProfileImageView;
//    }
//
//    public TextView getOtherMessageView() {
//        return mOtherMessageView;
//    }
//
//    public void setOtherMessageView(TextView mOtherMessageView) {
//        this.mOtherMessageView = mOtherMessageView;
//    }
//
//    public TextView getMyMessageView() {
//        return mMyMessageView;
//    }
//
//    public void setmMyMessageView(TextView mMyMessageView) {
//        this.mMyMessageView = mMyMessageView;
//    }
//
//    public void setOtherMessage(String otherMessage) {
//        mOtherMessageView.setText(otherMessage);
//    }
//
//    public void setMyMessage(String myMessage) {
//        mMyMessageView.setText(myMessage);
//    }
//}
