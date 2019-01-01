package com.example.leeyh.abroadapp.view.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.databinding.ActivitySignInBinding;
import com.example.leeyh.abroadapp.repository.SignRepository;
import com.example.leeyh.abroadapp.viewmodel.SignViewModel;

import static com.example.leeyh.abroadapp.constants.StaticString.E_MAIL;
import static com.example.leeyh.abroadapp.constants.StaticString.GOOGLE_SIGN_IN;
import static com.example.leeyh.abroadapp.constants.StaticString.LOCATION_CODE;
import static com.example.leeyh.abroadapp.constants.StaticString.SIGN_UP_CODE;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding mBinding;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);
        SignRepository repository = new SignRepository();
        SignViewModel.SignViewModelFactory factory = new SignViewModel.SignViewModelFactory(getApplication(), repository);
        final SignViewModel viewModel = ViewModelProviders.of(this, factory).get(SignViewModel.class);
        mBinding.setHandler(viewModel);
        mBinding.setLifecycleOwner(this);
        requestLocationPermission();
        mBinding.showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mBinding.showPassword.isChecked()) {
                    // show password
                    mBinding.signInPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // hide password
                    mBinding.signInPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        mBinding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMainTabActivity = new Intent(getApplicationContext(), TabBarMainActivity.class);
                startActivity(goToMainTabActivity);
                finish();
            }
        });

        mBinding.signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSignUp = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(goToSignUp, SIGN_UP_CODE);
            }
        });

        mBinding.signInGoogleCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = viewModel.getGoogleSignInClient().getSignInIntent();
                startActivityForResult(intent, GOOGLE_SIGN_IN);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestLocationPermission() {
        if ((checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this)
                    .setMessage(R.string.request_location_permission)
                    .setPositiveButton(R.string.permission_positive, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_CODE);
                        }
                    }).setNegativeButton(R.string.permission_negative, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getApplicationContext(), R.string.permission_negative_response, Toast.LENGTH_SHORT).show();
                            dialogInterface.cancel();
                        }
                    });
            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_UP_CODE && resultCode == RESULT_OK) {
            mBinding.signInEmailEditText.setText(data.getStringExtra(E_MAIL));
            return;
        }
        if (requestCode == GOOGLE_SIGN_IN && resultCode == RESULT_OK) {
            //sign up fragment show
        }
    }
}
