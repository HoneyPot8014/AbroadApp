package com.example.leeyh.abroadapp.view.fragment.signup;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.databinding.FragmentSignUpDefaultBinding;
import com.example.leeyh.abroadapp.viewmodel.SignViewModel;

import static com.example.leeyh.abroadapp.constants.StaticString.CAMERA_CODE;

public class SignUpDefaultFragment extends Fragment {

    private FragmentSignUpDefaultBinding mBinding;
    private SignViewModel mViewModel;
    private FragmentManager mFragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getActivity().getSupportFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up_default, container, false);
        mViewModel = ViewModelProviders.of(getActivity()).get(SignViewModel.class);
        mBinding.setHandler(mViewModel);
        mBinding.setLifecycleOwner(this);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.signUpProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getPhoto = new Intent(Intent.ACTION_GET_CONTENT);
                getPhoto.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(getPhoto, CAMERA_CODE);
            }
        });
        mBinding.signUpDefaultNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Transition sharedElementAnim = TransitionInflater.from(getContext()).inflateTransition(R.transition.shared_image_transition);

                SignUpExtraFragment signUpExtraFragment = new SignUpExtraFragment();
                signUpExtraFragment.setSharedElementEnterTransition(sharedElementAnim);
                signUpExtraFragment.setReturnTransition(sharedElementAnim);

                mFragmentManager.beginTransaction().replace(R.id.sign_in_container, signUpExtraFragment).addToBackStack(null)
                        .addSharedElement(mBinding.signUpProfileImageView, "profile").commit();
            }
        });

        mBinding.signUpDefaultCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentManager.popBackStack();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mViewModel.getImageBitmap(requestCode, resultCode, data);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
