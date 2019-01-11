package com.example.leeyh.abroadapp.view.fragment.signup;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.databinding.FragmentSignInBinding;
import com.example.leeyh.abroadapp.view.activity.TabBarMainActivity;
import com.example.leeyh.abroadapp.viewmodel.SignViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInFragment extends Fragment {

    private FragmentSignInBinding mBinding;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false);
        SignViewModel mViewModel = ViewModelProviders.of(getActivity()).get(SignViewModel.class);
        mBinding.setHandler(mViewModel);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
                requestSignIn();
            }
        });

        mBinding.signUpRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.sign_in_container, new SignUpDefaultFragment()).addToBackStack(null).commit();
//                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.sign_up_list_enter);
//                animation.setInterpolator(getContext(), android.R.anim.bounce_interpolator);
//                animation.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        SignUpListFragment fragment = new SignUpListFragment();
//
//                        Slide slideAnim = new Slide(Gravity.LEFT);
//                        slideAnim.setDuration(500);
//
//                        Transition transform = TransitionInflater.from(getContext()).inflateTransition(R.transition.change_image_transform);
//                        Transition explode = TransitionInflater.from(getContext()).inflateTransition(android.R.transition.explode);
//
//                        fragment.setEnterTransition(slideAnim);
//                        fragment.setSharedElementEnterTransition(transform);
//                        fragment.setSharedElementReturnTransition(transform);
//                        setReturnTransition(explode);
//                        setExitTransition(explode);
//
//                        getActivity().getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.sign_in_container, fragment).addToBackStack(null)
//                                .addSharedElement(mBinding.signUpRequestButton, "transition_title")
//                                .addSharedElement(mBinding.signInGoogleImageView, "google")
//                                .addSharedElement(mBinding.signInFacebookImageView, "facebook")
//                                .addSharedElement(mBinding.signInEmailImageView, "mail")
//                                .commit();
//                        Log.d("백스택", "onCreate: 2번" +getActivity().getSupportFragmentManager().getBackStackEntryCount());
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//                });
//                mBinding.singUpListCardView.setVisibility(View.VISIBLE);
//                mBinding.singUpListCardView.setAnimation(animation);
            }
        });
    }

    private void requestSignIn() {
        mAuth.signInWithEmailAndPassword(mBinding.signInEmailEditText.getText().toString(), mBinding.signInPasswordEditText.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent goToMain = new Intent(getContext(), TabBarMainActivity.class);
                startActivity(goToMain);
                getActivity().finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Check Your E-Mail and Password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
