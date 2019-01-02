package com.example.leeyh.abroadapp.view.fragment.signup;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.databinding.FragmentSignInBinding;
import com.example.leeyh.abroadapp.view.activity.TabBarMainActivity;
import com.example.leeyh.abroadapp.viewmodel.SignViewModel;

public class SignInFragment extends Fragment {

    FragmentSignInBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false);
        SignViewModel viewModel = ViewModelProviders.of(getActivity()).get(SignViewModel.class);
        mBinding.setHandler(viewModel);
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
                Intent goToMainTabActivity = new Intent(getContext(), TabBarMainActivity.class);
                startActivity(goToMainTabActivity);
                getActivity().finish();
            }
        });

        mBinding.signUpRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.sign_up_list_enter);
                animation.setInterpolator(getContext(), android.R.anim.bounce_interpolator);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        SignUpListFragment fragment = new SignUpListFragment();

                        Slide slideAnim = new Slide(Gravity.LEFT);
                        slideAnim.setDuration(500);

                        Transition transform = TransitionInflater.from(getContext()).inflateTransition(R.transition.change_image_transform);
                        Transition explode = TransitionInflater.from(getContext()).inflateTransition(android.R.transition.explode);

                        fragment.setEnterTransition(slideAnim);
                        fragment.setSharedElementEnterTransition(transform);
                        fragment.setSharedElementReturnTransition(transform);
                        setReturnTransition(explode);
                        setExitTransition(explode);

//                        setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.change_image_transform));
//                        setExitTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.explode));
//
//                        fragment.setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.change_image_transform));

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.sign_in_container, fragment).addToBackStack(null)
                                .addSharedElement(mBinding.signUpRequestButton, "transition_title")
                                .addSharedElement(mBinding.signInGoogleImageView, "google")
                                .addSharedElement(mBinding.signInFacebookImageView, "facebook")
                                .addSharedElement(mBinding.signInEmailImageView, "mail")
                                .commit();
//                        mBinding.singUpListCardView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mBinding.singUpListCardView.setVisibility(View.VISIBLE);
                mBinding.singUpListCardView.setAnimation(animation);
            }
        });
    }
}
