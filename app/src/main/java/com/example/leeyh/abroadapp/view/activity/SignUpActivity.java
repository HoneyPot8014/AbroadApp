package com.example.leeyh.abroadapp.view.activity;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.databinding.ActivitySignUpBinding;
import com.example.leeyh.abroadapp.viewmodel.SignViewModel;

import static com.example.leeyh.abroadapp.constants.StaticString.CAMERA_CODE;
import static com.example.leeyh.abroadapp.constants.StaticString.E_MAIL;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding mBinding;
    private SignViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        SignViewModel.SignViewModelFactory factory = new SignViewModel.SignViewModelFactory(this);
        viewModel = ViewModelProviders.of(this, factory).get(SignViewModel.class);
        mBinding.setHandler(viewModel);
        mBinding.setLifecycleOwner(this);

        //set spinner adapter
        ArrayAdapter yearSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext()
                , R.array.date_year, R.layout.simple_spinner_item);
        yearSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter monthSpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext()
                , R.array.date_month, R.layout.simple_spinner_item);
        monthSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter daySpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext()
                , R.array.date_day, R.layout.simple_spinner_item);
        daySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mBinding.signUpYearSpinner.setAdapter(yearSpinnerAdapter);
        mBinding.signUpMonthSpinner.setAdapter(monthSpinnerAdapter);
        mBinding.signUpDaySpinner.setAdapter(daySpinnerAdapter);

        mBinding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra(E_MAIL, mBinding.signUpEmailEditTextView.getText().toString());
                setResult(RESULT_OK, result);
                finish();
            }
        });

        mBinding.signUpProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getPhoto = new Intent(Intent.ACTION_GET_CONTENT);
                getPhoto.setType(MediaStore.Images.Media.CONTENT_TYPE);
                getPhoto.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(getPhoto, CAMERA_CODE);
            }
        });
    }// end of onCreate


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.getImageBitmap(requestCode, resultCode, data);
//        if (requestCode == CAMERA_CODE) {
//            if (resultCode == Activity.RESULT_OK) {
//                Glide.with(this).asBitmap().load(data.getData()).into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                        Bitmap profileBitmap = Bitmap.createScaledBitmap(resource, (int) (resource.getWidth() / 2), (int) (resource.getHeight() / 2), true);
//                        mBinding.signUpProfileImageView.setImageBitmap(profileBitmap);
////                        mProfileByteArray = DataConverter.getByteArrayToStringFromBitmap(mProfileBitmap);
////                        mProfileImageView.setImageBitmap(mProfileBitmap);
//                    }
//                });
//            }
//        }
    }

//    public void onClickedSend(View v) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("회원가입");
//        builder.setMessage("해당 정보로 가입 하시겠습니까?");
//        builder.setPositiveButton("예",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (!editText_email.getText().toString().equals("") && !editText_name.getText().toString().equals("")
//                                && editText_password.getText().toString().length() >= 6) {
//                            UserModel2 user = new UserModel2();
//
//                            String email = editText_email.getText().toString();
//                            String password = editText_password.getText().toString();
//
//                            int id = radioGroup.getCheckedRadioButtonId();
//                            //getCheckedRadioButtonId() 의 리턴값은 선택된 RadioButton 의 id 값.
//                            RadioButton radioButton = (RadioButton) findViewById(id);
//
//                            user.setUserName(editText_name.getText().toString());
//                            user.setUserId(editText_email.getText().toString());
//                            user.setUserPassword(password);
//                            user.setUserGender(radioButton.getText().toString());
//                            user.setUserBirthDay(yearSpinner.getSelectedItem().toString() + monthSpinner.getSelectedItem().toString() + daySpinner.getSelectedItem().toString());
//
//                            JSONObject signUpData = new JSONObject();
//                            try {
//                                signUpData.put(USER_NAME, editText_name.getText().toString());
//                                signUpData.put(E_MAIL, email);
//                                signUpData.put(PASSWORD, password);
//                                signUpData.put(GENDER, radioButton.getText());
//                                signUpData.put(DOB, yearSpinner.getSelectedItem().toString() + monthSpinner.getSelectedItem().toString() + daySpinner.getSelectedItem().toString());
//                                if (mProfileByteArray != null) {
//                                    signUpData.put(PROFILE, mProfileByteArray);
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            Log.d("회원가입", "onClick: " + signUpData.toString());
//                            mAppManager.emitRequestToServer(SIGN_UP, signUpData);
//                        } else {
//                            Toast.makeText(getApplicationContext(), "회원가입 정보를 다시한번 확인해주세요 (비밀번호 6자리 이상)", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//        builder.setNegativeButton("아니오",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getApplicationContext(), "아니오를 선택했습니다.", Toast.LENGTH_LONG).show();
//                    }
//                });
//        builder.show();
//    }
}

