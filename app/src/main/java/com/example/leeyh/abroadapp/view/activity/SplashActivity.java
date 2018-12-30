package com.example.leeyh.abroadapp.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.databinding.ActivitySplashBinding;
import com.example.leeyh.abroadapp.view.CyclicTransitionDrawable;

/**
 * Created by bum on 2018. 11. 17..
 */

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding mBinding;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        mBinding.setLifecycleOwner(this);
        CyclicTransitionDrawable ctd = new CyclicTransitionDrawable(new Drawable[]{

                getDrawable(R.drawable.person),
                getDrawable(R.drawable.balloon),
                getDrawable(R.drawable.tourist),

        });
        ctd.startTransition(3000, 1000);
        mBinding.splashBackground.setImageDrawable(ctd);

        mBinding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSignIn = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(goToSignIn);
                finish();
            }
        });

        mBinding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSignUp = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(goToSignUp);
            }
        });

    }
}