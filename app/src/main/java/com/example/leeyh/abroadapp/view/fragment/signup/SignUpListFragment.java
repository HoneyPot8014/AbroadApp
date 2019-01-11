package com.example.leeyh.abroadapp.view.fragment.signup;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.databinding.FragmentSignUpListBinding;
import com.example.leeyh.abroadapp.viewmodel.SignViewModel;

public class SignUpListFragment extends Fragment {

    private FragmentSignUpListBinding mBinding;
    private FragmentManager mFragmentManager;
    private SignViewModel mViewModel;
//    private CallbackManager mCallbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        mFragmentManager = getActivity().getSupportFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up_list, container, false);
        mViewModel = ViewModelProviders.of(getActivity()).get(SignViewModel.class);
        mBinding.setHandler(mViewModel);

//        mCallbackManager = CallbackManager.Factory.create();
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        mBinding.signUpListGoogleCardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
////                Intent intent = mViewModel.getGoogleSignUpInfo().getSignInIntent();
//                startActivityForResult(intent, GOOGLE_SIGN_IN);
//            }
//        });
//
//        mBinding.signUpListFacebookCardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mBinding.signUpFacebookLoginButton.callOnClick();
//            }
//        });
//
//        mBinding.signUpFacebookLoginButton.setReadPermissions("email", "public_profile");
//        mBinding.signUpFacebookLoginButton.setFragment(this);
//        mBinding.signUpFacebookLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Toast.makeText(getContext(), "성공11111111111", Toast.LENGTH_SHORT).show();
//                mViewModel.getFacebookSignUpInfo(loginResult.getAccessToken());
//                mFragmentManager.beginTransaction().replace(R.id.sign_in_container, new SignUpDefaultFragment()).commit();
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//        });


//        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                mViewModel.getFacebookSignUpInfo(AccessToken.getCurrentAccessToken());
//                mFragmentManager.beginTransaction().replace(R.id.sign_in_container, new SignUpDefaultFragment()).commit();
//            }
//
//            @Override
//            public void onCancel() {
//                Toast.makeText(getContext(), "canceled", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Toast.makeText(getContext(), "failed" + error.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case GOOGLE_SIGN_IN:
//                try {
//                    mViewModel.onGoogleSignUpResult(requestCode, resultCode, data);
//                    mFragmentManager.beginTransaction().replace(R.id.sign_in_container, new SignUpDefaultFragment()).commit();
//                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                } catch (ApiException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
//                }
//                return;
//        }
//        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
