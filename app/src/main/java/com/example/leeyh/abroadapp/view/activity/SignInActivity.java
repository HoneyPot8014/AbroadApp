package com.example.leeyh.abroadapp.view.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.background.OnResponseReceivedListener;
import com.example.leeyh.abroadapp.helper.ApplicationManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.leeyh.abroadapp.constants.SocketEvent.CHECK_SIGNED;
import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTE_SIGN_IN;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SIGNED_USER;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SIGN_IN;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SIGN_IN_FAILED;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SIGN_IN_SUCCESS;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SQL_ERROR;
import static com.example.leeyh.abroadapp.constants.StaticString.LOCATION_CODE;
import static com.example.leeyh.abroadapp.constants.StaticString.PASSWORD;
import static com.example.leeyh.abroadapp.constants.StaticString.SAVED_INSTANCE;
import static com.example.leeyh.abroadapp.constants.StaticString.SIGN_UP_CODE;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_ID;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_INFO;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_UUID;

public class SignInActivity extends AppCompatActivity implements OnResponseReceivedListener {

    private SharedPreferences mSharedPreferences;
    private EditText mIdEditTextView;
    private EditText mPasswordEditTextView;
    private ApplicationManagement mAppManagement;
    private String packageName;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        packageName = getPackageName();
        //request location permission
        requestLocationPermission();

        mIdEditTextView = findViewById(R.id.editTextId);
        mPasswordEditTextView = findViewById(R.id.editTextPassword);
        TextView mSignUpTextView = findViewById(R.id.signUpTextView);
        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToSignUp = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(goToSignUp, SIGN_UP_CODE);
            }
        });

        TextView mSignInTextView = findViewById(R.id.signInTextView);
        mSignInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emitRequestSignIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAppManagement = (ApplicationManagement) getApplication();
        mAppManagement.routeSocket(ROUTE_SIGN_IN);
        mSharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        mAppManagement.setOnResponseReceivedListener(this);
        setOnResponseSignIn();
        emitRequestUserIsSigned();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_UP_CODE) {
            if (resultCode == RESULT_OK) {
                mIdEditTextView.setText(data.getStringExtra(USER_ID));
            }
        }
    }

    @Override
    public void onResponseReceived(String onEvent, Object[] args) {
        switch (onEvent) {
            case SQL_ERROR:
                Toast.makeText(mAppManagement, "sql error // server error", Toast.LENGTH_SHORT).show();
                break;
            case SIGN_IN_SUCCESS:
                onResponseSignInSuccess(args);
                break;
            case SIGN_IN_FAILED:
                Looper.prepare();
                Toast.makeText(mAppManagement, "signInFailed", Toast.LENGTH_SHORT).show();
                Looper.loop();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestLocationPermission() {
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager != null && (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || powerManager.isIgnoringBatteryOptimizations("package:" + packageName))) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this)
                    .setMessage("Abroad를 사용하기 위해서는 위치권한과 배터리 사용량 최적화 설정 안함이 필요합니다. 설정화면으로 이동??")
                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_CODE);
                        }
                    }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(mAppManagement, "권한설정을 취소했습니다.", Toast.LENGTH_SHORT).show();
                            dialogInterface.cancel();
                        }
                    });
            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent getIgnoreBatteryPermission = new Intent();
                getIgnoreBatteryPermission.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                getIgnoreBatteryPermission.setData(Uri.parse("package:" + packageName));
                startActivity(getIgnoreBatteryPermission);
            }

        }
    }

    public void emitRequestUserIsSigned() {
        try {
            if (mSharedPreferences.getString(USER_ID, null) != null && mSharedPreferences.getString(PASSWORD, null) != null
                    && mSharedPreferences.getString(USER_UUID, null) != null) {
                JSONObject checkUserSignedData = new JSONObject();
                checkUserSignedData.put(USER_ID, mSharedPreferences.getString(USER_ID, null));
                checkUserSignedData.put(PASSWORD, mSharedPreferences.getString(PASSWORD, null));
                checkUserSignedData.put(USER_UUID, mSharedPreferences.getString(USER_UUID, null));
                mAppManagement.emitRequestToServer(CHECK_SIGNED, checkUserSignedData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void emitRequestSignIn() {
        String id = mIdEditTextView.getText().toString();
        String password = mPasswordEditTextView.getText().toString();
        JSONObject signInData = new JSONObject();
        try {
            signInData.put(USER_ID, id);
            signInData.put(PASSWORD, password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAppManagement.emitRequestToServer(SIGN_IN, signInData);
    }

    public void setOnResponseSignIn() {
        mAppManagement.onResponseFromServer(SQL_ERROR);
        mAppManagement.onResponseFromServer(SIGN_IN_SUCCESS);
        mAppManagement.onResponseFromServer(SIGN_IN_FAILED);
        mAppManagement.onResponseFromServer(SIGNED_USER);
    }

    public void goToMainActivity() {
        Intent goToMainActivity = new Intent(getApplicationContext(), TabBarMainActivity.class);
        startActivity(goToMainActivity);
        finish();
    }

    public void onResponseSignInSuccess(Object[] args) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        try {
            JSONArray receivedArray = (JSONArray) args[0];
            JSONObject receivedObject = (JSONObject) receivedArray.get(0);
            editor.putString(USER_ID, receivedObject.optString(USER_ID));
            editor.putString(PASSWORD, receivedObject.optString(PASSWORD));
            editor.putString(USER_UUID, receivedObject.optString(USER_UUID));
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //save userInfo in system environment
        goToMainActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//       mAppManagement.disconnectSocket();
    }
}
