package com.example.leeyh.abroadapp.view.fragment.main;


import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.databinding.FragmentMemberDetailBinding;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class MemberDetailFragment extends Fragment {

    FragmentMemberDetailBinding mBinding;
    int i = 1;
    int originalHeight;
    Bitmap bitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_member_detail, container, false);
        Glide.with(getContext())
                .asBitmap()
                .load("https://firebasestorage.googleapis.com/v0/b/abroadapp-22417.appspot.com/o/userProfileImage%2FA1iuG0SDo0W7v2pIDrK6jeA1S5p2.jpg?alt=media&token=bef1012a-4e5d-43d6-9832-36e8ed468545")
                .addListener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        Toast.makeText(getContext(), "이미지 로딩 실패" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        bitmap = resource;
                        mBinding.detailMemberProfileImageView.setImageBitmap(resource);
                        originalHeight = mBinding.detailMemberProfileImageView.getLayoutParams().height;
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
                            if (i <= 80) {
                                newY = (int) event.getY();
                                mBinding.detailMemberProfileImageView.setImageBitmap(createBlurBitmap(bitmap, i));
                                ViewGroup.LayoutParams params = mBinding.detailMemberProfileImageView.getLayoutParams();
                                params.height += 5;
                                mBinding.detailMemberProfileImageView.setLayoutParams(params);
                                i += 2;
                            }
                        } else {
                            if (i > 1) {
                                mBinding.detailMemberProfileImageView.setImageBitmap(createBlurBitmap(bitmap, i));
                                ViewGroup.LayoutParams params = mBinding.detailMemberProfileImageView.getLayoutParams();
                                params.height -= 5;
                                mBinding.detailMemberProfileImageView.setLayoutParams(params);
                                i -= 2;
                            }
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        Toast.makeText(getContext(), "포인터 업", Toast.LENGTH_SHORT).show();
                        mBinding.detailMemberProfileImageView.setImageBitmap(createBlurBitmap(bitmap, 0));
                        ViewGroup.LayoutParams params = mBinding.detailMemberProfileImageView.getLayoutParams();
                        params.height = originalHeight;
                        mBinding.detailMemberProfileImageView.setLayoutParams(params);
                        i = 0;
                        break;
                }
                return true;
            }
        });
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
