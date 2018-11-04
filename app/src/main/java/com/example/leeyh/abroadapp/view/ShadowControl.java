package com.example.leeyh.abroadapp.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.example.leeyh.abroadapp.R;

/**
 * Created by bum on 2018. 10. 2..
 */

public class ShadowControl extends LinearLayout  {

    public ShadowControl(Context context) {
        super(context);
        initBackground();
    }

    public ShadowControl(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initBackground();
    }

    public ShadowControl(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBackground();
    }

    private void initBackground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(ViewUtils.generateBackgroundWithShadow(this,R.color.cardview_light_background,
                    R.dimen.cardview_default_radius,R.color.cardview_shadow_start_color,R.dimen.cardview_default_elevation, Gravity.BOTTOM));
        }
    }


}

