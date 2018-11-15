package com.example.leeyh.abroadapp.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.background.OnResponseReceivedListener;
import com.example.leeyh.abroadapp.dataconverter.DataConverter;
import com.example.leeyh.abroadapp.helper.ApplicationManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTE_SIGN_UP;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SIGN_UP;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SIGN_UP_FAILED;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SIGN_UP_SUCCESS;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SQL_ERROR;
import static com.example.leeyh.abroadapp.constants.StaticString.CAMERA_CODE;
import static com.example.leeyh.abroadapp.constants.StaticString.NICKNAME;
import static com.example.leeyh.abroadapp.constants.StaticString.PASSWORD;
import static com.example.leeyh.abroadapp.constants.StaticString.PROFILE;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_ID;

public class SignUpActivity extends AppCompatActivity implements OnResponseReceivedListener {

    private ApplicationManagement mAppManager;
    private Bitmap mProfileBitmap;
    private ImageView mProfileImageView;
    private EditText mIdEditEditTextView;
    private EditText mPasswordEditTextView;
    private EditText mNickNameEditTextView;
    private byte[] mProfileByteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAppManager = (ApplicationManagement) getApplication();
        mAppManager.routeSocket(ROUTE_SIGN_UP);
        mAppManager.setOnResponseReceivedListener(this);
        mAppManager.onResponseFromServer(SQL_ERROR);
        mAppManager.onResponseFromServer(SIGN_UP_SUCCESS);
        mAppManager.onResponseFromServer(SIGN_UP_FAILED);

        mIdEditEditTextView = findViewById(R.id.sign_up_id_edit_text);
        mPasswordEditTextView = findViewById(R.id.sign_up_password_edit_text);
        mNickNameEditTextView = findViewById(R.id.sign_up_nick_name_edit_text);
        mProfileImageView = findViewById(R.id.sign_up_profile_image_view);
        mProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getPhoto = new Intent(Intent.ACTION_PICK);
                getPhoto.setType(MediaStore.Images.Media.CONTENT_TYPE);
                getPhoto.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(getPhoto, CAMERA_CODE);
            }
        });

        Button signUpRequestButton = findViewById(R.id.sign_up_button);
        signUpRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignUpButtonClicked();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    mProfileBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    mProfileByteArray = DataConverter.getByteArrayToStringFromBitmap(mProfileBitmap);
                    mProfileImageView.setImageBitmap(mProfileBitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onResponseReceived(String onEvent, Object[] object) {
        switch (onEvent) {
            case SQL_ERROR:
                Toast.makeText(mAppManager, "SQL // Server Error", Toast.LENGTH_SHORT).show();
                break;
            case SIGN_UP_SUCCESS:
                Toast.makeText(mAppManager, "", Toast.LENGTH_SHORT).show();
                String id = mIdEditEditTextView.getText().toString();
                Intent setResultToSignIn = new Intent(getApplicationContext(), SignInActivity.class);
                setResultToSignIn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK & Intent.FLAG_ACTIVITY_CLEAR_TOP & Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(setResultToSignIn);
                setResultToSignIn.putExtra(USER_ID, id);
                setResult(RESULT_OK, setResultToSignIn);
                finish();
                break;
            case SIGN_UP_FAILED:
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        Toast.makeText(mAppManager, "SIGN_UP_FAILED", Toast.LENGTH_SHORT).show();
                    }
                }.sendEmptyMessage(0);

                break;
        }
    }

    private void onSignUpButtonClicked() {
        String id = mIdEditEditTextView.getText().toString();
        String password = mPasswordEditTextView.getText().toString();
        String nickName = mNickNameEditTextView.getText().toString();
        if (mProfileBitmap != null) {
            mAppManager.addBitmapToMemoryCache(id, mProfileBitmap);
        }

        if (!id.equals("") && !password.equals("") && !nickName.equals("")) {
            JSONObject signUpData = new JSONObject();
            try {
                signUpData.put(USER_ID, id);
                signUpData.put(PASSWORD, password);
                signUpData.put(NICKNAME, nickName);
                if(mProfileByteArray != null) {
                    signUpData.put(PROFILE, mProfileByteArray);
                }
                mAppManager.emitRequestToServer(SIGN_UP, signUpData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "빈칸없이 작성하세요~", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAppManager = null;
    }
}
