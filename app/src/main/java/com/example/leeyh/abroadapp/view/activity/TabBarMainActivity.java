package com.example.leeyh.abroadapp.view.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.databinding.ActivityTabBarMainBinding;
import com.example.leeyh.abroadapp.model.UserModel;
import com.example.leeyh.abroadapp.repository.UserRepository;
import com.example.leeyh.abroadapp.view.fragment.ChatListFragment;
import com.example.leeyh.abroadapp.view.fragment.main.LocationFragment;
import com.example.leeyh.abroadapp.view.fragment.MyPageFragment;
import com.example.leeyh.abroadapp.view.fragment.TravelPlanFragment;
import com.example.leeyh.abroadapp.viewmodel.UserViewModel;

import java.util.ArrayList;

import static com.example.leeyh.abroadapp.constants.StaticString.ERROR;
import static com.example.leeyh.abroadapp.constants.StaticString.LOCATION_FAILED;

public class TabBarMainActivity extends AppCompatActivity {

    private ActivityTabBarMainBinding mBinding;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_bar_main);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_tab_bar_main);
        UserRepository userRepository = new UserRepository();
        UserViewModel.UserViewModelFactory factory = new UserViewModel.UserViewModelFactory(getApplication(), userRepository);
        UserViewModel viewModel = ViewModelProviders.of(this, factory).get(UserViewModel.class);
        mBinding.setHandler(viewModel);
        mBinding.setLifecycleOwner(this);

        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().replace(R.id.main_activity_container, new LocationFragment()).commit();

        mBinding.locationTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(mFragmentManager.findFragmentById(R.id.main_activity_container) instanceof LocationFragment))
                    mFragmentManager.beginTransaction().replace(R.id.main_activity_container, new LocationFragment()).commit();
            }
        });

        mBinding.chatTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(mFragmentManager.findFragmentById(R.id.main_activity_container) instanceof ChatListFragment))
                    mFragmentManager.beginTransaction().replace(R.id.main_activity_container, new ChatListFragment()).commit();
            }
        });

        mBinding.manageTravelTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(mFragmentManager.findFragmentById(R.id.main_activity_container) instanceof TravelPlanFragment))
                    mFragmentManager.beginTransaction().replace(R.id.main_activity_container, new TravelPlanFragment()).commit();
            }
        });

        mBinding.myPageTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(mFragmentManager.findFragmentById(R.id.main_activity_container) instanceof MyPageFragment))
                    mFragmentManager.beginTransaction().replace(R.id.main_activity_container, new MyPageFragment()).commit();
            }
        });

        viewModel.saveUserLocation();

        viewModel.getStatus().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                switch (s) {
                    case LOCATION_FAILED:
                        Toast.makeText(TabBarMainActivity.this, "Failed to catch user Location", Toast.LENGTH_SHORT).show();
                        break;
                    case ERROR:
                        Toast.makeText(TabBarMainActivity.this, "Failed to load User", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}
