package com.example.leeyh.abroadapp.prototype;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.leeyh.abroadapp.R;

import static com.example.leeyh.abroadapp.prototype.Constants.LOGIN_ROUTE;
import static com.example.leeyh.abroadapp.prototype.Constants.SIGN_IN;
import static com.example.leeyh.abroadapp.prototype.Constants.SIGN_IN_FAILED;
import static com.example.leeyh.abroadapp.prototype.Constants.SIGN_IN_SUCCESS;
import static com.example.leeyh.abroadapp.prototype.ProtoSignUpActivity.EVENT;
import static com.example.leeyh.abroadapp.prototype.ProtoSignUpActivity.NICKNAME;
import static com.example.leeyh.abroadapp.prototype.ProtoSignUpActivity.PASSWORD;
import static com.example.leeyh.abroadapp.prototype.ProtoSignUpActivity.ROUTING;
import static com.example.leeyh.abroadapp.prototype.ProtoSignUpActivity.USER_ID;

public class ProtoSignInActivity extends AppCompatActivity {

    private EditText mIdEditTextView;
    private EditText mPasswordEditTextView;
    private EditText mNicknameEditTextView;
    private ImageView profile;
    private BroadcastReceiver signInFailedReceiver;
    private BroadcastReceiver signInSuccessReceiver;
//    private BroadcastReceiver test;
    private String mUserId;
    public static String TAG = "생명주기";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proto__sign_in);
        profile = findViewById(R.id.profile_image);
        Intent service = new Intent(getApplicationContext(), ProtoBackgroundChat.class);
        service.putExtra(ROUTING, LOGIN_ROUTE);
        startService(service);

        mUserId = getIntent().getStringExtra(USER_ID);
        mIdEditTextView = findViewById(R.id.id_sign_in_edit_text_view);
        mPasswordEditTextView = findViewById(R.id.password_sign_in_edit_text_view);
        mNicknameEditTextView = findViewById(R.id.nickname_signt_in_edit_text);
        mIdEditTextView.setText(mUserId);
        Button signInButton = findViewById(R.id.sign_up_button_proto);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignInButtonClicked();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        onSignInFailed();
        onSignInSuccess();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(signInFailedReceiver);
        unregisterReceiver(signInSuccessReceiver);
//        unregisterReceiver(test);
    }

    private void onSignInButtonClicked() {
        String id = mIdEditTextView.getText().toString();
        String password = mPasswordEditTextView.getText().toString();
        String nickName = mNicknameEditTextView.getText().toString();

        Intent service = new Intent(getApplicationContext(), ProtoBackgroundChat.class);
        service.putExtra(EVENT, SIGN_IN);
        service.putExtra(USER_ID, id);
        service.putExtra(PASSWORD, password);
        service.putExtra(NICKNAME, nickName);
        startService(service);
    }

    private void onSignInFailed() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SIGN_IN_FAILED);
        signInFailedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, "ID, PASSWORD를 확인하세요", Toast.LENGTH_SHORT).show();
            }
        };
        registerReceiver(signInFailedReceiver, intentFilter);
    }

    private void onSignInSuccess() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SIGN_IN_SUCCESS);
        signInSuccessReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Intent goToChatListActivity = new Intent(getApplicationContext(), ProtoChatListActivity.class);
                goToChatListActivity.putExtra(USER_ID, mIdEditTextView.getText().toString());
                startActivity(goToChatListActivity);
                finish();
            }
        };
        registerReceiver(signInSuccessReceiver, intentFilter);
    }

//    private void test() {
////        IntentFilter intentFilter = new IntentFilter();
////        intentFilter.addAction("test");
////        test = new BroadcastReceiver() {
////            @Override
////            public void onReceive(Context context, Intent intent) {
//////                byte[] byteArrayExtra = intent.getByteArrayExtra("test");
//////                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArrayExtra, 0, byteArrayExtra.length);
////                profile.setImageBitmap((Bitmap) intent.getParcelableExtra("test"));
////            }
////        };
////        registerReceiver(test, intentFilter);
//    }
}
