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
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.repository.SignRepository;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.io.ByteArrayOutputStream;

import static com.example.leeyh.abroadapp.constants.StaticString.CAMERA_CODE;
import static com.example.leeyh.abroadapp.constants.StaticString.DOB_ERROR;
import static com.example.leeyh.abroadapp.constants.StaticString.INSUFFICIENT_DATA;
import static com.example.leeyh.abroadapp.constants.StaticString.NOT_MATCH_PASSWORD;
import static com.example.leeyh.abroadapp.constants.StaticString.WEAK_PASSWORD;

public class SignViewModel extends AndroidViewModel {

    private Application mApplication;
    private SignRepository mRepository;
    public MutableLiveData<String> mName;
    public MutableLiveData<String> mEMail;
    public MutableLiveData<String> mPassword;
    public MutableLiveData<String> mConfirmPassword;
    private MutableLiveData<String> mGender;
    private MutableLiveData<String> mDOBYear;
    private MutableLiveData<String> mDOBMonth;
    private MutableLiveData<String> mDOBDay;
    public MutableLiveData<Bitmap> mProfileBitmap;

    public SignViewModel(Application application, SignRepository repository) {
        super(application);
        mApplication = application;
        mRepository = repository;
        mName = new MutableLiveData<>();
        mEMail = new MutableLiveData<>();
        mPassword = new MutableLiveData<>();
        mConfirmPassword = new MutableLiveData<>();
        mGender = new MutableLiveData<>();
        mDOBYear = new MutableLiveData<>();
        mDOBMonth = new MutableLiveData<>();
        mDOBDay = new MutableLiveData<>();
        mProfileBitmap = new MutableLiveData<>();
    }

    public void onGenderCheckChanged(RadioGroup group, int id) {
        switch (id) {
            case R.id.sign_up_man_radio_button:
                mGender.setValue("man");
                break;
            case R.id.sign_up_woman_radio_button:
                mGender.setValue("woman");
                break;
        }
    }

    public void onYearSpinnerItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mDOBYear.setValue(parent.getSelectedItem().toString());
    }

    public void onMonthSpinnerItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mDOBMonth.setValue(parent.getSelectedItem().toString());
    }

    public void onDaySpinnerItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mDOBDay.setValue(parent.getSelectedItem().toString());
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
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mProfileBitmap.getValue().compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return outputStream.toByteArray();
    }

    public void onCreateUser(View view) {
        if (mName.getValue() == null || mEMail.getValue() == null || mPassword.getValue() == null || mConfirmPassword.getValue() == null
                || mGender.getValue() == null || mDOBYear.getValue() == null || mDOBMonth.getValue() == null || mDOBDay.getValue() == null) {
            mRepository.getListener().onTaskFinished(INSUFFICIENT_DATA);
            return;
        }
        if (mDOBYear.getValue().equals("Year") || mDOBMonth.getValue().equals("Month") || mDOBMonth.getValue().equals("day")) {
            mRepository.getListener().onTaskFinished(DOB_ERROR);
            return;
        }
        if (mPassword.getValue().length() < 6) {
            mRepository.getListener().onTaskFinished(WEAK_PASSWORD);
            return;
        }
        if (!mPassword.getValue().equals(mConfirmPassword.getValue())) {
            mRepository.getListener().onTaskFinished(NOT_MATCH_PASSWORD);
            return;
        }
        mRepository.getListener().onTaskStarted();
        mRepository.createUser(mEMail.getValue(), mPassword.getValue(), getBitmapArray());
    }

    public GoogleSignInClient getGoogleSignInClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mApplication.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        return GoogleSignIn.getClient(mApplication, gso);
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
