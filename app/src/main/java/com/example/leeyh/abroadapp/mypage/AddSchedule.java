package com.example.leeyh.abroadapp.mypage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.leeyh.abroadapp.R;

public class AddSchedule extends AppCompatActivity implements View.OnClickListener{

    RelativeLayout relativeLayout;
    Button add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        add_button = (Button)findViewById(R.id.btn_addschedule);
        add_button.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        System.out.println("click");
//        relativeLayout.setBackgroundColor(Color.BLACK);
    }
}
