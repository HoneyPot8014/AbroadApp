package com.example.leeyh.abroadapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.constants.Statistical;
import com.example.leeyh.abroadapp.dataconverter.DataConverter;
import com.example.leeyh.abroadapp.service.OnResponseReceivedListener;
import com.example.leeyh.abroadapp.service.ServiceEventInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.example.leeyh.abroadapp.constants.NameSpacing.ROUTE_SIGN_UP;
import static com.example.leeyh.abroadapp.constants.NameSpacing.SIGN_UP;
import static com.example.leeyh.abroadapp.constants.NameSpacing.SIGN_UP_FAILED;
import static com.example.leeyh.abroadapp.constants.NameSpacing.SIGN_UP_SUCCESS;
import static com.example.leeyh.abroadapp.constants.NameSpacing.SQL_ERROR;
import static com.example.leeyh.abroadapp.constants.StaticString.CAMERA_CODE;
import static com.example.leeyh.abroadapp.constants.StaticString.EMIT_EVENT;
import static com.example.leeyh.abroadapp.constants.StaticString.NICKNAME;
import static com.example.leeyh.abroadapp.constants.StaticString.PASSWORD;
import static com.example.leeyh.abroadapp.constants.StaticString.PROFILE;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_Id;

public class SignUpActivity extends AppCompatActivity implements OnResponseReceivedListener {

    private ServiceEventInterface mSocketListener;
    private Statistical mAppStatic;
    private ImageView mProfileImageView;
    private EditText mIdEditEditTextView;
    private EditText mPasswordEditTextView;
    private EditText mNickNameEditTextView;
    private Button mSignUpRequestButton;
    private byte[] mProfileByteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mSocketListener = new ServiceEventInterface(getApplicationContext());
        mSocketListener.socketRouting(ROUTE_SIGN_UP);
        mSocketListener.setResponseListener(this);
        mSocketListener.socketOnEvent(SIGN_UP_SUCCESS);
        mSocketListener.socketOnEvent(SIGN_UP_FAILED);
        mSocketListener.socketOnEvent(SQL_ERROR);

        mAppStatic = (Statistical) getApplication();

        mProfileImageView = findViewById(R.id.sign_up_profile_image_view);
        mIdEditEditTextView = findViewById(R.id.sign_up_id_edit_text);
        mPasswordEditTextView = findViewById(R.id.sign_up_password_edit_text);
        mNickNameEditTextView = findViewById(R.id.sign_up_nick_name_edit_text);
        mSignUpRequestButton = findViewById(R.id.sign_up_button);

        mProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getPhoto = new Intent(Intent.ACTION_PICK);
                getPhoto.setType(MediaStore.Images.Media.CONTENT_TYPE);
                getPhoto.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(getPhoto, CAMERA_CODE);
            }
        });

        mSignUpRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignUpButtonClicked();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocketListener = null;
    }

    @Override
    public void onResponseReceived(Intent intent) {
        String event = intent.getStringExtra(EMIT_EVENT);
        switch (event) {
            case SIGN_UP_SUCCESS:
                //sign up success handle here
                Toast.makeText(mAppStatic, "회원가입 성공", Toast.LENGTH_SHORT).show();
                break;
            case SIGN_UP_FAILED:
                //sign up failed handle here
                Toast.makeText(mAppStatic, "회원가입 실패", Toast.LENGTH_SHORT).show();
                break;
            case SQL_ERROR:
                //server can't handle this data or server problem so handle here
                Toast.makeText(mAppStatic, "SQL ERROR", Toast.LENGTH_SHORT).show();
                break;
            default:
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Bitmap profileBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    mProfileByteArray = DataConverter.getByteArrayFromBitmap(profileBitmap);
                    mProfileImageView.setImageBitmap(profileBitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void onSignUpButtonClicked() {
        String id = mIdEditEditTextView.getText().toString();
        String password = mPasswordEditTextView.getText().toString();
        String nickName = mNickNameEditTextView.getText().toString();

        if (!id.equals("") && !password.equals("") && !nickName.equals("")) {
            JSONObject signUpData = new JSONObject();
            try {
                signUpData.put(USER_Id, id);
                signUpData.put(PASSWORD, password);
                signUpData.put(NICKNAME, nickName);
                if (mProfileByteArray != null) {
                    signUpData.put(PROFILE, mProfileByteArray);
                } else {
                    Toast.makeText(getApplicationContext(), "사진이 비어있슴", Toast.LENGTH_SHORT).show();
                }
                String signUpDataToString = signUpData.toString();
                mSocketListener.socketEmitEvent(SIGN_UP, signUpDataToString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "빈칸없이 작성하세요~", Toast.LENGTH_SHORT).show();
        }
    }
}