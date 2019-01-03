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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.databinding.FragmentSignUpListBinding;
import com.example.leeyh.abroadapp.viewmodel.SignViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import static android.app.Activity.RESULT_OK;
import static com.example.leeyh.abroadapp.constants.StaticString.GOOGLE_SIGN_IN;

public class SignUpListFragment extends Fragment {

    private FragmentSignUpListBinding mBinding;
    private FragmentManager mFragmentManager;
    private SignViewModel mViewModel;

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
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.signUpListGoogleCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = mViewModel.getGoogleSignInClient().getSignInIntent();
                startActivityForResult(intent, GOOGLE_SIGN_IN);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GOOGLE_SIGN_IN:

                    mFragmentManager.beginTransaction().replace(R.id.sign_in_container, new SignUpDefaultFragment()).commit();
                    break;
            }
        }
    }
}
