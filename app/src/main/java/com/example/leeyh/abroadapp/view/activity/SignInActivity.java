package com.example.leeyh.abroadapp.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.background.BackgroundService;
import com.example.leeyh.abroadapp.background.OnResponseReceivedListener;
import com.example.leeyh.abroadapp.background.ServiceEventInterface;

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
import static com.example.leeyh.abroadapp.constants.StaticString.ON_EVENT;
import static com.example.leeyh.abroadapp.constants.StaticString.PASSWORD;
import static com.example.leeyh.abroadapp.constants.StaticString.RECEIVED_DATA;
import static com.example.leeyh.abroadapp.constants.StaticString.SIGN_UP_CODE;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_ID;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_INFO;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_UUID;

public class SignInActivity extends AppCompatActivity implements OnResponseReceivedListener {

    private ServiceEventInterface mSocketListener;
    private SharedPreferences mSharedPreferences;
    private EditText mIdEditTextView;
    private EditText mPasswordEditTextView;
    private TextView mSignUpTextView;
    private TextView mSignInTextView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Intent service = new Intent(getApplicationContext(), BackgroundService.class);
        stopService(service);
        if (!isServiceRunningCheck()) {
            startService(service);
        }


        mSocketListener = new ServiceEventInterface(getApplicationContext());
        mSocketListener.socketRouting(ROUTE_SIGN_IN);
        mSocketListener.setResponseListener(this);
        mSharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        try {
            if (mSharedPreferences.getString(USER_ID, null) != null && mSharedPreferences.getString(PASSWORD, null) != null
                    && mSharedPreferences.getString(USER_UUID, null) != null) {
                JSONObject checkUserSignedData = new JSONObject();
                checkUserSignedData.put(USER_ID, mSharedPreferences.getString(USER_ID, null));
                checkUserSignedData.put(PASSWORD, mSharedPreferences.getString(PASSWORD, null));
                checkUserSignedData.put(USER_UUID, mSharedPreferences.getString(USER_UUID, null));
                mSocketListener.socketEmitEvent(CHECK_SIGNED, checkUserSignedData.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocketListener.socketOnEvent(SIGNED_USER);
        mSocketListener.socketOnEvent(SIGN_IN_SUCCESS);
        mSocketListener.socketOnEvent(SIGN_IN_FAILED);
        mSocketListener.socketOnEvent(SQL_ERROR);

        //request location permission
        requestLocationPermission();
        mSocketListener.socketOnEvent(SIGNED_USER);

        mIdEditTextView = findViewById(R.id.editTextId);
        mPasswordEditTextView = findViewById(R.id.editTextPassword);
        mSignUpTextView = findViewById(R.id.signUpTextView);
        mSignInTextView = findViewById(R.id.signInTextView);

        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToSignUp = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(goToSignUp, SIGN_UP_CODE);
            }
        });

        mSignInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignInButtonClicked();
            }
        });
    }

    @Override
    public void onResponseReceived(Intent intent) {
        String event = intent.getStringExtra(ON_EVENT);
        switch (event) {
            case SIGNED_USER:
                Log.d("여기눈", "onResponseReceived: ");
//                goToMainActivity();
                break;
            case SIGN_IN_SUCCESS:
                //signInSuccess handle
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                try {
                    JSONArray receivedArray = new JSONArray(intent.getStringExtra(RECEIVED_DATA));
                    JSONObject receivedObject = (JSONObject) receivedArray.get(0);
                    editor.putString(USER_ID, receivedObject.optString(USER_ID));
                    editor.putString(PASSWORD, receivedObject.optString(PASSWORD));
                    editor.putString(USER_UUID, receivedObject.optString(USER_UUID));
                    editor.commit();
                    Log.d("여기 ㅠ", "onResponseReceived: " + receivedObject.getString(USER_ID) + receivedObject.get(PASSWORD) + receivedObject.getString(USER_UUID));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //save userInfo in system environment
                goToMainActivity();
                break;
            case SIGN_IN_FAILED:
                //signInFailed handle - not match with Id or Password
                Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show();
                break;
            case SQL_ERROR:
                //handle error
                break;
            default:
                break;
        }
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestLocationPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_CODE);
    }

    public void goToMainActivity() {
        Intent goToMainActivity = new Intent(getApplicationContext(), TabBarMainActivity.class);
        startActivity(goToMainActivity);
        finish();
    }

    public void onSignInButtonClicked() {
        String id = mIdEditTextView.getText().toString();
        String password = mPasswordEditTextView.getText().toString();
        JSONObject signInData = new JSONObject();
        try {
            signInData.put(USER_ID, id);
            signInData.put(PASSWORD, password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String signInDataToString = signInData.toString();
        mSocketListener.socketEmitEvent(SIGN_IN, signInDataToString);
    }

    public boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (BackgroundService.class.getName().equals(service.service.getClassName()))
                return true;
        }
        return false;
    }

    public boolean isServiceRunningCheck() {
        ActivityManager manager = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.example.leeyh.abroadapp.background.BackgroundService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}
