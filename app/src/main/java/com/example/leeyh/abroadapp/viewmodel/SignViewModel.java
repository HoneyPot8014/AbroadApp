package com.example.leeyh.abroadapp.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.leeyh.abroadapp.model.UserModel;
import com.example.leeyh.abroadapp.repository.SignRepository;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.io.ByteArrayOutputStream;

import static com.example.leeyh.abroadapp.constants.StaticString.CAMERA_CODE;
import static com.example.leeyh.abroadapp.constants.StaticString.INSUFFICIENT_DATA;
import static com.example.leeyh.abroadapp.constants.StaticString.NOT_MATCH_PASSWORD;
import static com.example.leeyh.abroadapp.constants.StaticString.WEAK_PASSWORD;

public class SignViewModel extends AndroidViewModel {

    private String mGender;
    //    private String mSignInMethod;
    private Application mApplication;
    private SignRepository mRepository;
    //    private AuthCredential credential;
//    private GoogleSignInAccount googleAccount;
    public MutableLiveData<String> mName;
    public MutableLiveData<String> mEMail;
    public MutableLiveData<String> mPassword;
    public MutableLiveData<String> mConfirmPassword;
    private MutableLiveData<String> mAge;
    private MutableLiveData<String> mCountry;
    public MutableLiveData<Bitmap> mProfileBitmap;

    public SignViewModel(Application application, SignRepository repository) {
        super(application);
        mApplication = application;
        mRepository = repository;
        mName = new MutableLiveData<>();
        mEMail = new MutableLiveData<>();
        mPassword = new MutableLiveData<>();
        mConfirmPassword = new MutableLiveData<>();
        mAge = new MutableLiveData<>();
        mCountry = new MutableLiveData<>();
        mProfileBitmap = new MutableLiveData<>();
    }

    public void onAgeSpinnerItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mAge.setValue(parent.getSelectedItem().toString());
    }

    public void onCountrySpinnerItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mCountry.setValue(parent.getSelectedItem().toString());
    }

    public void setGender(String gender) {
        mGender = gender;
    }

    public void getImageBitmap(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Glide.with(mApplication).asBitmap().load(data.getData()).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        mProfileBitmap.setValue(Bitmap.createScaledBitmap(resource, (int) (resource.getWidth() / 2), (int) (resource.getHeight() / 2), true));
                    }
                });
            }
        }
    }

    private byte[] getBitmapArray() {
        if (mProfileBitmap.getValue() == null) {
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mProfileBitmap.getValue().compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return outputStream.toByteArray();
    }

//    public GoogleSignInClient getGoogleSignUpInfo() {
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(mApplication.getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        return GoogleSignIn.getClient(mApplication, gso);
//    }

//    public void onGoogleSignUpResult(int requestCode, int resultCode, Intent data) throws ApiException {
//        if (resultCode == Activity.RESULT_OK) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            googleAccount = task.getResult(ApiException.class);
//            credential = GoogleAuthProvider.getCredential(googleAccount.getIdToken(), null);
//            mSignInMethod = GOOGLE;
//            mEMail.setValue(googleAccount.getEmail());
//        }
//    }

//    public void getFacebookSignUpInfo(AccessToken token) {
//        credential = FacebookAuthProvider.getCredential(token.getToken());
//        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()) {
//                    Toast.makeText(mApplication, "성공", Toast.LENGTH_SHORT).show();
//                } else {
//                    Log.d("에러2", "onComplete: " + task.getException().toString());
//                    Toast.makeText(mApplication, "실패", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        mSignInMethod = FACEBOOK;
//    }

    public void onCreateUser(View view) {
        if (mName.getValue() == null || mEMail.getValue() == null || mPassword.getValue() == null || mConfirmPassword.getValue() == null
                || mGender == null) {
            mRepository.getListener().onTaskFinished(INSUFFICIENT_DATA);
            return;
        }
//        if (mDOBYear.getValue().equals("Year") || mDOBMonth.getValue().equals("Month") || mDOBMonth.getValue().equals("day")) {
//            mRepository.getListener().onTaskFinished(DOB_ERROR);
//            return;
//        }
        if (mPassword.getValue().length() < 6) {
            mRepository.getListener().onTaskFinished(WEAK_PASSWORD);
            return;
        }
        if (!mPassword.getValue().equals(mConfirmPassword.getValue())) {
            mRepository.getListener().onTaskFinished(NOT_MATCH_PASSWORD);
            return;
        }
        mRepository.getListener().onTaskStarted();
        UserModel user = new UserModel(null, mEMail.getValue(), null, mName.getValue(), mAge.getValue(), mGender, mCountry.getValue());
        mRepository.emailCreateUser(mPassword.getValue(), getBitmapArray(), user);
//        switch (mSignInMethod) {
//            case GOOGLE:
//                mRepository.credentialCreateUser(credential, getBitmapArray(), user);
//                break;
//            case FACEBOOK:
//                mRepository.credentialCreateUser(credential, getBitmapArray(), user);
//                break;
//            default:
//                mRepository.emailCreateUser(mPassword.getValue(), getBitmapArray(), user);
//
//        }
    }

    public static class SignViewModelFactory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private Application mApplication;
        private SignRepository mRepository;

        public SignViewModelFactory(@NonNull Application application, SignRepository repository) {
            this.mApplication = application;
            this.mRepository = repository;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(SignViewModel.class)) {
                return (T) new SignViewModel(mApplication, mRepository);
            }
            throw new Fragment.InstantiationException("not viewModel class", null);
        }
    }
}
