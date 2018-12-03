package com.example.leeyh.abroadapp.view.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
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

import com.bumptech.glide.Glide;
import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.background.OnResponseReceivedListener;
import com.example.leeyh.abroadapp.dataconverter.DataConverter;
import com.example.leeyh.abroadapp.helper.ApplicationManagement;
import com.example.leeyh.abroadapp.model.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTE_SIGN_UP;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SIGN_UP;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SIGN_UP_FAILED;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SIGN_UP_SUCCESS;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SQL_ERROR;
import static com.example.leeyh.abroadapp.constants.StaticString.CAMERA_CODE;
import static com.example.leeyh.abroadapp.constants.StaticString.DOB;
import static com.example.leeyh.abroadapp.constants.StaticString.GENDER;
import static com.example.leeyh.abroadapp.constants.StaticString.E_MAIL;
import static com.example.leeyh.abroadapp.constants.StaticString.PASSWORD;
import static com.example.leeyh.abroadapp.constants.StaticString.PROFILE;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_NAME;

public class SignUpActivity extends AppCompatActivity implements OnResponseReceivedListener {
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

                            JSONObject signUpData = new JSONObject();
                            try {
                                signUpData.put(USER_NAME, editText_name.getText().toString());
                                signUpData.put(E_MAIL, email);
                                signUpData.put(PASSWORD, password);
                                signUpData.put(GENDER, radioButton.getText());
                                signUpData.put(DOB, yearSpinner.getSelectedItem().toString() + monthSpinner.getSelectedItem().toString() + daySpinner.getSelectedItem().toString());
                                if (mProfileByteArray != null) {
                                    signUpData.put(PROFILE, mProfileByteArray);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d("회원가입", "onClick: " + signUpData.toString());
                            mAppManager.emitRequestToServer(SIGN_UP, signUpData);
                        } else {
                            Toast.makeText(getApplicationContext(), "회원가입 정보를 다시한번 확인해주세요 (비밀번호 6자리 이상)", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "아니오를 선택했습니다.", Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                String imagePath = uri.getPath();
                Bitmap image = BitmapFactory.decodeFile(imagePath);

                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(imagePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int exifOrientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                int exifDegree = exifOrientationToDegrees(exifOrientation);
//                image = rotate(image, exifDegree);
                mProfileImageView.setImageBitmap(image);
            }
//                try {
//                    mProfileBitmap = DataConverter.handleSamplingAndRotationBitmap(getApplicationContext(), uri);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                mProfileBitmap = DataConverter.getRotatedBitmap(uri, getApplicationContext());
//                    mProfileBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
//                mProfileByteArray = DataConverter.getByteArrayToStringFromBitmap(mProfileBitmap);
            mProfileImageView.setImageBitmap(mProfileBitmap);
        }
    }

    @Override
    public void onResponseReceived(String onEvent, Object[] object) {
        switch (onEvent) {
            case SQL_ERROR:
                Toast.makeText(mAppManager, "SQL // Server Error", Toast.LENGTH_SHORT).show();
                break;
            case SIGN_UP_SUCCESS:
                Intent signUpSuccessIntent = new Intent();
                signUpSuccessIntent.putExtra(USER_NAME, editText_email.getText().toString());
                setResult(RESULT_OK, signUpSuccessIntent);
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

    public int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAppManager = null;
    }
}

