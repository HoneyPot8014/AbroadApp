package com.example.leeyh.abroadapp.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.databinding.ActivityTabBarMainBinding;
import com.example.leeyh.abroadapp.repository.UserRepository;
import com.example.leeyh.abroadapp.view.fragment.ChatListFragment;
import com.example.leeyh.abroadapp.view.fragment.main.LocationFragment;
import com.example.leeyh.abroadapp.view.fragment.MyPageFragment;
import com.example.leeyh.abroadapp.view.fragment.TravelPlanFragment;
import com.example.leeyh.abroadapp.viewmodel.UserViewModel;

public class TabBarMainActivity extends AppCompatActivity {

    private ActivityTabBarMainBinding mBinding;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_bar_main);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_tab_bar_main);
        UserViewModel.UserViewModelFactory factory = new UserViewModel.UserViewModelFactory(getApplication(), new UserRepository());
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

    }
}
