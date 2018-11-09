package com.example.leeyh.abroadapp.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.helper.ApplicationManagement;
import com.example.leeyh.abroadapp.background.OnResponseReceivedListener;
import com.example.leeyh.abroadapp.background.ServiceEventInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTE_SIGN_UP;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SIGN_UP;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SIGN_UP_FAILED;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SIGN_UP_SUCCESS;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SQL_ERROR;
import static com.example.leeyh.abroadapp.constants.StaticString.CAMERA_CODE;
import static com.example.leeyh.abroadapp.constants.StaticString.NICKNAME;
import static com.example.leeyh.abroadapp.constants.StaticString.ON_EVENT;
import static com.example.leeyh.abroadapp.constants.StaticString.PASSWORD;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_ID;

public class SignUpActivity extends AppCompatActivity implements OnResponseReceivedListener {

    private ServiceEventInterface mSocketListener;
    private ApplicationManagement mAppStatic;
    private Bitmap mProfileBitmap;
    private ImageView mProfileImageView;
    private EditText mIdEditEditTextView;
    private EditText mPasswordEditTextView;
    private EditText mNickNameEditTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mSocketListener = new ServiceEventInterface(getApplicationContext());
        //Routing namespace to '/signUp'
        mSocketListener.socketRouting(ROUTE_SIGN_UP);
        mSocketListener.setResponseListener(this);

        //set socket on event listener
        mSocketListener.socketOnEvent(SIGN_UP_SUCCESS);
        mSocketListener.socketOnEvent(SIGN_UP_FAILED);
        mSocketListener.socketOnEvent(SQL_ERROR);

        mAppStatic = (ApplicationManagement) getApplication();

        mProfileImageView = findViewById(R.id.sign_up_profile_image_view);
        mIdEditEditTextView = findViewById(R.id.sign_up_id_edit_text);
        mPasswordEditTextView = findViewById(R.id.sign_up_password_edit_text);
        mNickNameEditTextView = findViewById(R.id.sign_up_nick_name_edit_text);
        Button signUpRequestButton = findViewById(R.id.sign_up_button);

        mProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getPhoto = new Intent(Intent.ACTION_PICK);
                getPhoto.setType(MediaStore.Images.Media.CONTENT_TYPE);
                getPhoto.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(getPhoto, CAMERA_CODE);
            }
        });

        signUpRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignUpButtonClicked();
            }
        });

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        //Routing namespace to '/signUp'
        mSocketListener.socketRouting(ROUTE_SIGN_UP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocketListener = null;
    }

//    @Override
//    public void onSocketRouted() {
//
//    }

    @Override
    public void onResponseReceived(Intent intent) {
        String event = intent.getStringExtra(ON_EVENT);
        switch (event) {
            case SIGN_UP_SUCCESS:
                //sign up success handle here
//                SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                try {
//                    JSONArray receivedUuid = new JSONArray(intent.getStringExtra(RECEIVED_DATA));
//                    JSONObject receivedUuidObject = new JSONObject(receivedUuid.toString());
//                    editor.putString(USER_UUID, receivedUuidObject.getString(USER_UUID));
//                    Log.d("값은?", "onResponseReceived: " + receivedUuidObject.getString(USER_UUID));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();
                String id = mIdEditEditTextView.getText().toString();
                Intent setResultToSignIn = new Intent(getApplicationContext(), SignInActivity.class);
//                setResultToSignIn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK & Intent.FLAG_ACTIVITY_CLEAR_TOP & Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(setResultToSignIn);
                setResultToSignIn.putExtra(USER_ID, id);
                setResult(RESULT_OK, setResultToSignIn);

                finish();
                break;
            case SIGN_UP_FAILED:
                //sign up failed handle here
                Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
                break;
            case SQL_ERROR:
                //server can't handle this data or server problem so handle here
                Toast.makeText(getApplicationContext(), "SQL ERROR", Toast.LENGTH_SHORT).show();
                break;
            default:
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    mProfileBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    mProfileImageView.setImageBitmap(mProfileBitmap);
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
        if (mProfileBitmap != null) {
            mAppStatic.addBitmapToMemoryCache(id, mProfileBitmap);
        }

        if (!id.equals("") && !password.equals("") && !nickName.equals("")) {
            JSONObject signUpData = new JSONObject();
            try {
                signUpData.put(USER_ID, id);
                signUpData.put(PASSWORD, password);
                signUpData.put(NICKNAME, nickName);
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
