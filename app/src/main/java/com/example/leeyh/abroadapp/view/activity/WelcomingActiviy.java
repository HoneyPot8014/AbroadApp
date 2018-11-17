package com.example.leeyh.abroadapp.view.activity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.view.CyclicTransitionDrawable;

/**
 * Created by bum on 2018. 11. 17..
 */

public class WelcomingActiviy extends AppCompatActivity {
    ImageView demoImage;
    int imagesToShow[] = {R.drawable.person, R.drawable.rough, R.drawable.tourist, R.drawable.balloon};

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcoming);
        demoImage = (ImageView) findViewById(R.id.animBackground);
        CyclicTransitionDrawable ctd = new CyclicTransitionDrawable(new Drawable[] {

                getDrawable(R.drawable.person),
                getDrawable(R.drawable.balloon),
                getDrawable(R.drawable.tourist),

});

        demoImage.setImageDrawable(ctd);

        ctd.startTransition(5000, 2000);
        //animate(demoImage, imagesToShow, 0,false);

    }
    
}