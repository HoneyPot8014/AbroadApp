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
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.databinding.ActivitySignInBinding;
import com.example.leeyh.abroadapp.repository.SignRepository;
import com.example.leeyh.abroadapp.view.fragment.signup.SignInFragment;
import com.example.leeyh.abroadapp.view.fragment.signup.SignUpDefaultFragment;
import com.example.leeyh.abroadapp.viewmodel.SignViewModel;

import static com.example.leeyh.abroadapp.constants.StaticString.E_MAIL;
import static com.example.leeyh.abroadapp.constants.StaticString.GOOGLE_SIGN_IN;
import static com.example.leeyh.abroadapp.constants.StaticString.LOCATION_CODE;
import static com.example.leeyh.abroadapp.constants.StaticString.SIGN_UP_CODE;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding mBinding;
    private FragmentManager mFragmentManager;

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
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().add(R.id.sign_in_container, new SignInFragment()).commit();
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
}
