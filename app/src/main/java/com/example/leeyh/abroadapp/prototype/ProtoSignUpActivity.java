//package com.example.leeyh.abroadapp.prototype;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.example.leeyh.abroadapp.R;
//
//import static com.example.leeyh.abroadapp.prototype.Constants.CHECK_USER;
//import static com.example.leeyh.abroadapp.prototype.Constants.SIGN_UP;
//import static com.example.leeyh.abroadapp.prototype.Constants.SIGN_UP_FAILED;
//import static com.example.leeyh.abroadapp.prototype.Constants.SIGN_UP_SUCCESS;
//
//public class ProtoSignUpActivity extends AppCompatActivity {
//
//    private EditText mIdSignUp;
//    private EditText mPasswordSignUp;
//    private EditText mNicknameSingUp;
//    private BroadcastReceiver singUpSuccessReceiver;
//    private BroadcastReceiver signUpFailedReceiver;
//    public static final String ROUTING = "routing";
//    public static final String USER_ID = "userId";
//    public static final String PASSWORD = "password";
//    public static final String NICKNAME = "nickName";
//    public static final String PROFILE = "profile";
//    public static final String EVENT = "event";
//    private BroadcastReceiver goToChatReceiver;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_proto_sign_up);
//
//        Intent service = new Intent(getApplicationContext(), ProtoBackgroundChat.class);
//        service.putExtra(EVENT, "firstStart");
//        startService(service);
//
//        goToChat();
//
//        mIdSignUp = findViewById(R.id.id_sign_in_edit_text_view);
//        mPasswordSignUp = findViewById(R.id.password_sign_in_edit_text_view);
//        mNicknameSingUp = findViewById(R.id.nickname_signt_in_edit_text);
//        Button signUpButton = findViewById(R.id.sign_up_button_proto);
//        signUpButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onSignUpButtonClicked();
//            }
//        });
//
//        Button goToLoginButton = findViewById(R.id.sign_up_login);
//        goToLoginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent goToChatRoom = new Intent(getApplicationContext(), ProtoSignInActivity.class);
//                startActivity(goToChatRoom);
//                finish();
//            }
//        });
//
//        checkUser();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        onSignUpSuccess();
//        onSignUpFailed();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        unregisterReceiver(signUpFailedReceiver);
//        unregisterReceiver(singUpSuccessReceiver);
//        unregisterReceiver(goToChatReceiver);
//    }
//
//    private void onSignUpButtonClicked() {
//        String id = mIdSignUp.getText().toString();
//        String password = mPasswordSignUp.getText().toString();
//        String nickName = mNicknameSingUp.getText().toString();
//        Intent service = new Intent(getApplicationContext(), ProtoBackgroundChat.class);
//        service.putExtra(EVENT, SIGN_UP);
//        service.putExtra(USER_ID, id);
//        service.putExtra(PASSWORD, password);
//        service.putExtra(NICKNAME, nickName);
//        startService(service);
//    }
//
//    private void checkUser() {
//        Intent service = new Intent(getApplicationContext(), ProtoBackgroundChat.class);
//        service.putExtra(EVENT, CHECK_USER);
//        startService(service);
//    }
//
//    private void onSignUpSuccess() {
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(SIGN_UP_SUCCESS);
//        singUpSuccessReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Intent goToLoginActivity = new Intent(getApplicationContext(), ProtoSignInActivity.class);
//                goToLoginActivity.putExtra(USER_ID, mIdSignUp.getText().toString());
//                startActivity(goToLoginActivity);
//                finish();
//            }
//        };
//        registerReceiver(singUpSuccessReceiver, intentFilter);
//    }
//
//    private void onSignUpFailed() {
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(SIGN_UP_FAILED);
//        signUpFailedReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Toast.makeText(context, "이미 존재하는 ID 입니다.", Toast.LENGTH_SHORT).show();
//            }
//        };
//        registerReceiver(signUpFailedReceiver, intentFilter);
//    }
//
//    private void goToChat() {
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("goToChat");
//        goToChatReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Intent goToChat = new Intent(getApplicationContext(), ProtoChatListActivity.class);
//                goToChat.putExtra("storedUser", 101);
//                startActivity(goToChat);
//            }
//        };
//        registerReceiver(goToChatReceiver, intentFilter);
//    }
//}
