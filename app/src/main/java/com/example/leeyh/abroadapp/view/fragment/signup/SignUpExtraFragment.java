package com.example.leeyh.abroadapp.view.fragment.signup;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.databinding.FragmentSignUpExtraBinding;
import com.example.leeyh.abroadapp.viewmodel.SignViewModel;

public class SignUpExtraFragment extends Fragment {

    private FragmentSignUpExtraBinding mBinding;
    private SignViewModel mViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up_extra, container, false);
        mBinding.setLifecycleOwner(this);
        mViewModel = ViewModelProviders.of(getActivity()).get(SignViewModel.class);
        mBinding.setHandler(mViewModel);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String s = mViewModel.mEMail.getValue();
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }
}
