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
import java.io.InputStream;
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
/*
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
)*/




import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leeyh.abroadapp.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class SignUpActivity extends AppCompatActivity implements OnResponseReceivedListener{
    private ApplicationManagement mAppManager;
    private Bitmap mProfileBitmap;
    private ImageView mProfileImageView;
    private byte[] mProfileByteArray;

    EditText password;
    EditText passwordConfirm;
    ImageView check;
    EditText editText_name;
    EditText editText_email;
    EditText editText_password;
    RadioGroup radioGroup;
    Spinner yearSpinner;
    Spinner monthSpinner;
    Spinner daySpinner;
    Button nextBtn;
    ImageView checkEmailAddress;
    private Typeface mTypeface;
    private boolean isCreateUserSuccessed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        password = (EditText) findViewById(R.id.password);
        passwordConfirm = (EditText) findViewById(R.id.passwordConfirm);

//set the default according to value


        passwordConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String PasswordCheck = password.getText().toString();
                String confirmCheck = passwordConfirm.getText().toString();

                if (PasswordCheck.equals(confirmCheck)) {
                    check.setVisibility(View.VISIBLE);
                    check.setImageResource(R.drawable.checked);
                } else {
                    //check.setVisibility(View.VISIBLE);
                   // check.setImageResource(R.drawable.check);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mAppManager = (ApplicationManagement) getApplication();
        mAppManager.routeSocket(ROUTE_SIGN_UP);
        mAppManager.setOnResponseReceivedListener(this);
        mAppManager.onResponseFromServer(SQL_ERROR);
        mAppManager.onResponseFromServer(SIGN_UP_SUCCESS);
        mAppManager.onResponseFromServer(SIGN_UP_FAILED);


        mProfileImageView = findViewById(R.id.signUpImageView);
        mProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getPhoto = new Intent(Intent.ACTION_PICK);
                getPhoto.setType(MediaStore.Images.Media.CONTENT_TYPE);
                getPhoto.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(getPhoto, CAMERA_CODE);
            }
        });

        Button signUpRequestButton = findViewById(R.id.signUpBtn);
        signUpRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignUpButtonClicked();
            }
        });



    }// end of onCreate


    void setGlobalFont(ViewGroup root) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);
            if (child instanceof TextView)
                ((TextView) child).setTypeface(mTypeface);
            else if (child instanceof ViewGroup)
                setGlobalFont((ViewGroup) child);
        }
    }


    protected void onResume() {

        super.onResume();
        check = findViewById(R.id.check);
        check.setVisibility(View.INVISIBLE);


        nextBtn = findViewById(R.id.signUpBtn);
        // nextBtn.setEnabled(false);
       // checkEmailAddress = (ImageView) findViewById(R.id.checkEmail);

        //spinner
        yearSpinner = (Spinner) findViewById(R.id.year);
        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(this,
                R.array.date_year, R.layout.simple_spinner_item);

        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        monthSpinner = (Spinner) findViewById(R.id.month);
        ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(this,
                R.array.date_month, R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        daySpinner = (Spinner) findViewById(R.id.day);
        ArrayAdapter dayAdapter = ArrayAdapter.createFromResource(this,
                R.array.date_day, R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        daySpinner.setAdapter(dayAdapter);
        //spinner

        editText_name = findViewById(R.id.name);
        editText_email = findViewById(R.id.email);
        editText_password = findViewById(R.id.password);
        radioGroup = (RadioGroup) findViewById(R.id.radio);


    }


    public void onClickedSend(View v) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("회원가입");
        builder.setMessage("해당 정보로 가입 하시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (!editText_email.getText().toString().equals("") && !editText_name.getText().toString().equals("")
                                && editText_password.getText().toString().length() >= 6) {
                            UserModel user = new UserModel();

                            String email = editText_email.getText().toString();
                            String password = editText_password.getText().toString();

                            int id = radioGroup.getCheckedRadioButtonId();
                            //getCheckedRadioButtonId() 의 리턴값은 선택된 RadioButton 의 id 값.
                            RadioButton radioButton = (RadioButton) findViewById(id);


                            user.setUserName(editText_name.getText().toString());
                            user.setUserId(editText_email.getText().toString());
                            user.setUserPassword(password);
                            user.setUserGender(radioButton.getText().toString());
                            user.setUserBirthDay(yearSpinner.getSelectedItem().toString() + monthSpinner.getSelectedItem().toString() + daySpinner.getSelectedItem().toString());

                        /*Toast.makeText(SignUpActivity.this,editText_name.getText().toString()+editText_store_name.getText().toString()+editText_email.getText().toString()+
                                password+radioButton.getText().toString()+editText_phonenumber.getText().toString()+yearSpinner.getSelectedItem().toString()+monthSpinner.getSelectedItem().toString()+daySpinner.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
*/

                            if (isCreateUserSuccessed) {
                                //Intent intent = new Intent(SignUpActivity.this, EmailVerifyActivity.class);
                                // startActivity(intent);
                                // finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "아이디가 중복되었습니다.", Toast.LENGTH_LONG).show();

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "회원가입 정보를 다시한번 확인해주세요", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
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
                String id = editText_email.getText().toString();
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

        String id = editText_email.getText().toString();
        String password = editText_password.getText().toString();
        String nickName = editText_name.getText().toString();
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

