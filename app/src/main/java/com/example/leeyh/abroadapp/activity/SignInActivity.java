package com.example.leeyh.abroadapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.service.OnResponseReceivedListener;
import com.example.leeyh.abroadapp.service.ServiceEventInterface;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.leeyh.abroadapp.constants.NameSpacing.ROUTE_SIGN_IN;
import static com.example.leeyh.abroadapp.constants.NameSpacing.SIGN_IN;
import static com.example.leeyh.abroadapp.constants.NameSpacing.SIGN_IN_FAILED;
import static com.example.leeyh.abroadapp.constants.NameSpacing.SIGN_IN_SUCCESS;
import static com.example.leeyh.abroadapp.constants.NameSpacing.SQL_ERROR;
import static com.example.leeyh.abroadapp.constants.StaticString.EMIT_EVENT;
import static com.example.leeyh.abroadapp.constants.StaticString.PASSWORD;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_Id;

public class SignInActivity extends AppCompatActivity implements OnResponseReceivedListener {

    private ServiceEventInterface mSocketListener;
    private EditText mIdEditTextView;
    private EditText mPasswordEditTextView;
    private TextView mSignUpTextView;
    private TextView mSignInTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mSocketListener = new ServiceEventInterface(getApplicationContext());
        //Routing namespace to '/signIn'
        mSocketListener.socketRouting(ROUTE_SIGN_IN);
        mSocketListener.setResponseListener(this);

        mSocketListener.socketOnEvent(SIGN_IN_SUCCESS);
        mSocketListener.socketOnEvent(SIGN_IN_FAILED);
        mSocketListener.socketOnEvent(SQL_ERROR);

        mIdEditTextView = findViewById(R.id.editTextId);
        mPasswordEditTextView = findViewById(R.id.editTextPassword);
        mSignUpTextView = findViewById(R.id.signUpTextView);
        mSignInTextView = findViewById(R.id.signInTextView);

        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToSignUp = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(goToSignUp);
            }
        });

        mSignInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignInButtonClicked();
            }
        });
    }

    public void onSignInButtonClicked() {
        String id = mIdEditTextView.getText().toString();
        String password = mPasswordEditTextView.getText().toString();
        JSONObject signInData = new JSONObject();
        try {
            signInData.put(USER_Id, id);
            signInData.put(PASSWORD, password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String signInDataToString = signInData.toString();
        mSocketListener.socketEmitEvent(SIGN_IN, signInDataToString);
    }

    @Override
    public void onResponseReceived(Intent intent) {
        String event = intent.getStringExtra(EMIT_EVENT);
        switch (event) {
            case SIGN_IN_SUCCESS:
                //signInSuccess handle
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
    protected void onDestroy() {
        super.onDestroy();
        mSocketListener = null;
    }
}
