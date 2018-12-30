package com.example.leeyh.abroadapp.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioGroup;

import com.example.leeyh.abroadapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignViewModel extends ViewModel {

    public MutableLiveData<String> mName;
    public MutableLiveData<String> mEMail;
    public MutableLiveData<String> mPassword;
    public MutableLiveData<String> mConfirmPassword;
    public MutableLiveData<String> mGender;
    public MutableLiveData<String> mDOBYear;
    public MutableLiveData<String> mDOBMonth;
    public MutableLiveData<String> mDOBDay;
    private FirebaseAuth mAuth;
    public SignViewModel() {
        mName = new MutableLiveData<>();
        mEMail = new MutableLiveData<>();
        mPassword = new MutableLiveData<>();
        mConfirmPassword = new MutableLiveData<>();
        mGender = new MutableLiveData<>();
        mDOBYear = new MutableLiveData<>();
        mDOBMonth = new MutableLiveData<>();
        mDOBDay = new MutableLiveData<>();
        mAuth = FirebaseAuth.getInstance();
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
        mDOBMonth.setValue(parent.getSelectedItem().toString());
    }

    public void onCreateUser(View view) {
        if(mName.getValue() == null && mEMail.getValue() == null && mPassword.getValue() == null && mConfirmPassword.getValue() == null
                && mGender.getValue() == null && mDOBYear.getValue() == null && mDOBMonth.getValue() == null && mDOBDay.getValue() == null) {
            return;
        }
        if(mDOBYear.getValue().equals("Year") || mDOBMonth.getValue().equals("Month") || mDOBMonth.getValue().equals("day")) {
            return;
        }
        mAuth.createUserWithEmailAndPassword(mEMail.getValue(), mPassword.getValue()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    //insert local db
                    //request remote db
                } else {

                }
            }
        });
    }
}
