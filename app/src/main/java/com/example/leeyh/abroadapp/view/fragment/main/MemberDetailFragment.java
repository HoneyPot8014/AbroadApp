package com.example.leeyh.abroadapp.view.fragment.main;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.databinding.FragmentMemberDetailBinding;
import com.example.leeyh.abroadapp.model.UserModel;
import com.example.leeyh.abroadapp.viewmodel.UserViewModel;

import static com.example.leeyh.abroadapp.constants.StaticString.MODEL;

public class MemberDetailFragment extends Fragment {

    private int mResizeFlag = 1;
    private static int BITMAP_ORIGINAL_HEIGHT;
    private FragmentMemberDetailBinding mBinding;
    private Bitmap mBitmap;
    private UserModel user;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(getArguments() != null) {
            if(getArguments().getParcelable(MODEL) != null) {
                user = getArguments().getParcelable(MODEL);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_member_detail, container, false);
        UserViewModel viewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        mBinding.setHandler(viewModel);

        Glide.with(getContext())
                .asBitmap()
                .load("https://firebasestorage.googleapis.com/v0/b/abroadapp-22417.appspot.com/o/userProfileImage%2FA1iuG0SDo0W7v2pIDrK6jeA1S5p2.jpg?alt=media&token=bef1012a-4e5d-43d6-9832-36e8ed468545")
                .addListener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        Toast.makeText(getContext(), "Failed To load Profile", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        mBitmap = resource;
                        mBinding.detailMemberProfileImageView.setImageBitmap(resource);
                        BITMAP_ORIGINAL_HEIGHT = mBinding.detailMemberProfileImageView.getLayoutParams().height;
                        return false;
                    }
                }).into(mBinding.detailMemberProfileImageView);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mBinding.detailMemberProfileImageView.setOnTouchListener(new View.OnTouchListener() {
            float oldY;
            float newY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        oldY = event.getY();
                        newY = event.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (newY < event.getY()) {
                            if (mResizeFlag <= 50) {
                                newY = (int) event.getY();
                                mBinding.detailMemberProfileImageView.setImageBitmap(createBlurBitmap(mBitmap, mResizeFlag));
                                ViewGroup.LayoutParams params = mBinding.detailMemberProfileImageView.getLayoutParams();
                                params.height += 10;
                                mBinding.detailMemberProfileImageView.setLayoutParams(params);
                                mResizeFlag += 2;
                            }
                        } else {
                            if (mResizeFlag > 1) {
                                mBinding.detailMemberProfileImageView.setImageBitmap(createBlurBitmap(mBitmap, mResizeFlag));
                                ViewGroup.LayoutParams params = mBinding.detailMemberProfileImageView.getLayoutParams();
                                params.height -= 10;
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
                return true;
            }
        });
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
