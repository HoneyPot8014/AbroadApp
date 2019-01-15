package com.example.leeyh.abroadapp.view.fragment.main;


import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.databinding.FragmentMemberDetailBinding;
import com.example.leeyh.abroadapp.helper.Cache;
import com.example.leeyh.abroadapp.model.UserModel;
import com.example.leeyh.abroadapp.viewmodel.UserViewModel;

import static com.example.leeyh.abroadapp.constants.StaticString.MODEL;

public class MemberDetailFragment extends Fragment {

    private int mResizeFlag = 1;
    private static int BITMAP_ORIGINAL_HEIGHT;
    private FragmentMemberDetailBinding mBinding;
    private Bitmap mBitmap;
    private UserModel user;
    private UserViewModel mViewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getArguments() != null) {
            if (getArguments().getParcelable(MODEL) != null) {
                user = getArguments().getParcelable(MODEL);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_member_detail, container, false);
        mViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        mBinding.setHandler(mViewModel);
        getProfile();

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mBinding.detailMemberBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        mBinding.getRoot().setOnTouchListener(new View.OnTouchListener() {
            int oldY;
            int newY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mBitmap == null) {
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        oldY = (int) event.getY();
                        newY = (int) event.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (newY < event.getY()) {
                            if (mResizeFlag <= 50) {
                                newY = (int) event.getY();
                                mBinding.detailMemberProfileImageView.setImageBitmap(createBlurBitmap(mBitmap, mResizeFlag));
                                ViewGroup.LayoutParams params = mBinding.detailMemberProfileImageView.getLayoutParams();
                                params.height += Math.sqrt(newY - oldY);
                                mBinding.detailMemberProfileImageView.setLayoutParams(params);
                                mResizeFlag += 2;
                            }
                        } else {
                            if (mResizeFlag > 1) {
                                mBinding.detailMemberProfileImageView.setImageBitmap(createBlurBitmap(mBitmap, mResizeFlag));
                                ViewGroup.LayoutParams params = mBinding.detailMemberProfileImageView.getLayoutParams();
                                params.height -= Math.sqrt(newY - oldY);
                                mBinding.detailMemberProfileImageView.setLayoutParams(params);
                                mResizeFlag -= 2;
                            }
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        mBinding.detailMemberProfileImageView.setImageBitmap(createBlurBitmap(mBitmap, 0));
                        ViewGroup.LayoutParams params = mBinding.detailMemberProfileImageView.getLayoutParams();
                        params.height = BITMAP_ORIGINAL_HEIGHT;
                        mBinding.detailMemberProfileImageView.setLayoutParams(params);
                        mResizeFlag = 0;
                        break;
                }
                return false;
            }
        });

//        mBinding.detailMemberProfileImageView.setOnTouchListener(new View.OnTouchListener() {
//            float oldY;
//            float newY;
//            @SuppressLint("ClickableViewAccessibility")
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (mBitmap == null) {
//                    return false;
//                }
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        oldY = event.getY();
//                        newY = event.getY();
//                        break;
//
//                    case MotionEvent.ACTION_MOVE:
//                        if (newY < event.getY()) {
//                            if (mResizeFlag <= 50) {
//                                newY = (int) event.getY();
//                                mBinding.detailMemberProfileImageView.setImageBitmap(createBlurBitmap(mBitmap, mResizeFlag));
//                                ViewGroup.LayoutParams params = mBinding.detailMemberProfileImageView.getLayoutParams();
//                                params.height += 15;
//                                mBinding.detailMemberProfileImageView.setLayoutParams(params);
//                                mResizeFlag += 2;
//                            }
//                        } else {
//                            if (mResizeFlag > 1) {
//                                mBinding.detailMemberProfileImageView.setImageBitmap(createBlurBitmap(mBitmap, mResizeFlag));
//                                ViewGroup.LayoutParams params = mBinding.detailMemberProfileImageView.getLayoutParams();
//                                params.height -= 15;
//                                mBinding.detailMemberProfileImageView.setLayoutParams(params);
//                                mResizeFlag -= 2;
//                            }
//                        }
//                        break;
//
//                    case MotionEvent.ACTION_UP:
//                        mBinding.detailMemberProfileImageView.setImageBitmap(createBlurBitmap(mBitmap, 0));
//                        ViewGroup.LayoutParams params = mBinding.detailMemberProfileImageView.getLayoutParams();
//                        params.height = BITMAP_ORIGINAL_HEIGHT;
//                        mBinding.detailMemberProfileImageView.setLayoutParams(params);
//                        mResizeFlag = 0;
//                        break;
//                }
//                return true;
//            }
//        });
    }

    private void getProfile() {
        if (user.getUserImageUrl() == null || user.getUserImageUrl().equals("")) {
            mBinding.detailMemberProfileImageView.setImageResource(R.drawable.empty_profile);
            return;
        }
        if (Cache.getCache().get(user.getUid()) == null) {
            mViewModel.requestProfileBitmap(user);
            mViewModel.getProfileBitmap().observe(this, new Observer<Bitmap>() {
                @Override
                public void onChanged(@Nullable Bitmap bitmap) {
                    mBitmap = bitmap;
                    mBinding.detailMemberProfileImageView.setImageBitmap(mBitmap);
                    BITMAP_ORIGINAL_HEIGHT = mBitmap.getHeight();
                }
            });
        } else {
            mBitmap = Cache.getCache().get(user.getUid());
            mBinding.detailMemberProfileImageView.setImageBitmap(mBitmap);

        }
    }

    public static MemberDetailFragment newInstance(UserModel userModel) {
        Bundle data = new Bundle();
        data.putParcelable(MODEL, userModel);
        MemberDetailFragment fragment = new MemberDetailFragment();
        fragment.setArguments(data);
        return fragment;
    }

    private Bitmap createBlurBitmap(Bitmap src, float r) {
        if (r <= 0) {
            r = 0.1f;
        } else if (r > 25) {
            r = 25.0f;
        }

        Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        RenderScript renderScript = RenderScript.create(getContext());

        Allocation blurInput = Allocation.createFromBitmap(renderScript, src);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(r);
        blur.forEach(blurOutput);

        blurOutput.copyTo(bitmap);
        renderScript.destroy();

        return bitmap;
    }
}
