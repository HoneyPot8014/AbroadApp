package com.example.leeyh.abroadapp.view.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.databinding.ActivitySignInBinding;
import com.example.leeyh.abroadapp.repository.RepositoryListener;
import com.example.leeyh.abroadapp.repository.SignRepository;
import com.example.leeyh.abroadapp.view.fragment.signup.SignInFragment;
import com.example.leeyh.abroadapp.view.fragment.signup.SignUpDefaultFragment;
import com.example.leeyh.abroadapp.viewmodel.SignViewModel;

import static com.example.leeyh.abroadapp.constants.StaticString.ALREADY_EXIST_EMAIL;
import static com.example.leeyh.abroadapp.constants.StaticString.DOB_ERROR;
import static com.example.leeyh.abroadapp.constants.StaticString.ERROR;
import static com.example.leeyh.abroadapp.constants.StaticString.INSUFFICIENT_DATA;
import static com.example.leeyh.abroadapp.constants.StaticString.LOCATION_CODE;
import static com.example.leeyh.abroadapp.constants.StaticString.NOT_FORMATTED_EMAIL;
import static com.example.leeyh.abroadapp.constants.StaticString.NOT_MATCH_PASSWORD;
import static com.example.leeyh.abroadapp.constants.StaticString.SIGN_UP;
import static com.example.leeyh.abroadapp.constants.StaticString.SUCCESS;
import static com.example.leeyh.abroadapp.constants.StaticString.WEAK_PASSWORD;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding mBinding;
    private FragmentManager mFragmentManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);

        init();
        requestLocationPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void init() {
        SignRepository repository = new SignRepository();
        repository.setRepositoryListener(new RepositoryListener() {
            @Override
            public void onTaskStarted() {
                mBinding.getRoot().setAlpha(0.6f);
                mBinding.signInProgressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

            @Override
            public void onTaskFinished(String status) {
                mBinding.getRoot().setAlpha(1.0f);
                mBinding.signInProgressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                switch (status) {
                    case ALREADY_EXIST_EMAIL:
                        Toast.makeText(SignInActivity.this, "already exist email use another", Toast.LENGTH_SHORT).show();
                        break;
                    case INSUFFICIENT_DATA:
                        Toast.makeText(SignInActivity.this, "fill the blank", Toast.LENGTH_SHORT).show();
                        break;
                    case DOB_ERROR:
                        Toast.makeText(SignInActivity.this, "check Date Of Birth", Toast.LENGTH_SHORT).show();
                        break;
                    case WEAK_PASSWORD:
                        Toast.makeText(SignInActivity.this, "password require more than 6", Toast.LENGTH_SHORT).show();
                        break;
                    case NOT_MATCH_PASSWORD:
                        Toast.makeText(SignInActivity.this, "not match password. check the password", Toast.LENGTH_SHORT).show();
                        break;
                    case NOT_FORMATTED_EMAIL:
                        Toast.makeText(SignInActivity.this, "not formatted email", Toast.LENGTH_SHORT).show();
                        break;
                    case ERROR:
                        Toast.makeText(SignInActivity.this, "failed to create", Toast.LENGTH_SHORT).show();
                        break;
                    case SUCCESS:
                        int backStackCount = mFragmentManager.getBackStackEntryCount();
                        for(int i = 0; i < backStackCount; i++) {
                            mFragmentManager.popBackStack();
                        }
                        break;
                }
            }
        });

        SignViewModel.SignViewModelFactory factory = new SignViewModel.SignViewModelFactory(getApplication(), repository);
        final SignViewModel viewModel = ViewModelProviders.of(this, factory).get(SignViewModel.class);
        mBinding.setHandler(viewModel);
        mBinding.setLifecycleOwner(this);

        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().replace(R.id.sign_in_container, new SignInFragment()).commit();
        if(getIntent().getStringExtra(SIGN_UP) != null) {
            mFragmentManager.beginTransaction().replace(R.id.sign_in_container, new SignUpDefaultFragment()).addToBackStack(null).commit();
        }
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
