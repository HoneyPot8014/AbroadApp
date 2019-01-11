package com.example.leeyh.abroadapp.view.fragment.signup;


import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.databinding.FragmentSignUpExtraBinding;
import com.example.leeyh.abroadapp.viewmodel.SignViewModel;

import static com.example.leeyh.abroadapp.constants.StaticString.CAMERA_CODE;

public class SignUpExtraFragment extends Fragment {

    private FragmentSignUpExtraBinding mBinding;
    private SignViewModel mViewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up_extra, container, false);
        mBinding.setLifecycleOwner(this);
        mViewModel = ViewModelProviders.of(getActivity()).get(SignViewModel.class);
        mBinding.setHandler(mViewModel);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayAdapter spinnerGenderAdapter = ArrayAdapter.createFromResource(getContext(), R.array.age, R.layout.simple_spinner_item);
        spinnerGenderAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mBinding.signUpAgeSpinner.setAdapter(spinnerGenderAdapter);

        ArrayAdapter spinnerCountryAdapter = ArrayAdapter.createFromResource(getContext(), R.array.country, R.layout.simple_spinner_item);
        spinnerCountryAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mBinding.signUpCountrySpinner.setAdapter(spinnerCountryAdapter);

        mBinding.signUpExtraProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getPhoto = new Intent(Intent.ACTION_GET_CONTENT);
                getPhoto.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(getPhoto, CAMERA_CODE);
            }
        });

        mBinding.signUpFemaleCardView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (!mBinding.signUpFemaleCardView.isSelected()) {
                    mBinding.signUpFemaleImageView.setImageResource(R.drawable.female);
                    mBinding.signUpFemaleCardView.setSelected(true);
                    mViewModel.setGender("female");
                    if (mBinding.signUpMaleCardView.isSelected()) {
                        mBinding.signUpMaleImageView.setImageResource(R.drawable.male_gray);
                        mBinding.signUpMaleCardView.setSelected(false);
                    }
                }
            }
        });

        mBinding.signUpMaleCardView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (!mBinding.signUpMaleCardView.isSelected()) {
                    mBinding.signUpMaleImageView.setImageResource(R.drawable.male);
                    mBinding.signUpMaleCardView.setSelected(true);
                    mViewModel.setGender("male");
                    if (mBinding.signUpFemaleCardView.isSelected()) {
                        mBinding.signUpFemaleImageView.setImageResource(R.drawable.female_gray);
                        mBinding.signUpFemaleCardView.setSelected(false);
                    }
                }
            }
        });

        mBinding.signUpExtraPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mViewModel.getImageBitmap(requestCode, resultCode, data);
    }
}
