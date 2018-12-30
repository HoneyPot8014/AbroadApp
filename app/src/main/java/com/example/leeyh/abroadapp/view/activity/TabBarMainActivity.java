package com.example.leeyh.abroadapp.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.background.OnResponseReceivedListener;
import com.example.leeyh.abroadapp.databinding.ActivityTabBarMainBinding;
import com.example.leeyh.abroadapp.helper.ApplicationManagement;
import com.example.leeyh.abroadapp.view.fragment.ChatListFragment;
import com.example.leeyh.abroadapp.view.fragment.LocationFragment;
import com.example.leeyh.abroadapp.view.fragment.MyPageFragment;
import com.example.leeyh.abroadapp.view.fragment.OnChatListItemClicked;
import com.example.leeyh.abroadapp.view.fragment.OnNewChatRoomMaked;
import com.example.leeyh.abroadapp.view.fragment.TravelPlanFragment;
import com.example.leeyh.abroadapp.viewmodel.UserViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.leeyh.abroadapp.constants.SocketEvent.CHAT_CONNECT;
import static com.example.leeyh.abroadapp.constants.SocketEvent.CHAT_CONNECT_FAILED;
import static com.example.leeyh.abroadapp.constants.SocketEvent.CHAT_CONNECT_SUCCESS;
import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTE_CHAT;
import static com.example.leeyh.abroadapp.constants.StaticString.CHAT_LIST_FRAGMENT;
import static com.example.leeyh.abroadapp.constants.StaticString.IS_FOREGROUND;
import static com.example.leeyh.abroadapp.constants.StaticString.LOCATION_FRAGMENT;
import static com.example.leeyh.abroadapp.constants.StaticString.MESSAGE_FROM_SERVICE;
import static com.example.leeyh.abroadapp.constants.StaticString.MYPAGE_FRAGMENT;
import static com.example.leeyh.abroadapp.constants.StaticString.ROOM_NAME;
import static com.example.leeyh.abroadapp.constants.StaticString.TRAVEL_LIST_FRAGMENT;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_INFO;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_NAME;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_UUID;

public class TabBarMainActivity extends AppCompatActivity {

    private ActivityTabBarMainBinding mBinding;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_bar_main);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_tab_bar_main);
        UserViewModel viewModel = ViewModelProviders.of(this).get(UserViewModel.class);
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

    }

//        if (getIntent().getStringExtra(MESSAGE_FROM_SERVICE) != null) {
//            String roomName = getIntent().getStringExtra(ROOM_NAME);
//            mFragmentManager.beginTransaction().replace(R.id.main_activity_container, ChatListFragment.newChattingFragmentInstance(roomName)).commitAllowingStateLoss();
//        }
//
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        mFragmentManager.putFragment(outState, "fragment", mFragmentManager.findFragmentById(R.id.main_activity_container));
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        Fragment fragment = mFragmentManager.getFragment(savedInstanceState, "fragment");
//        mFragmentManager.beginTransaction().replace(R.id.main_activity_container, fragment);
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        if (intent.getStringExtra(MESSAGE_FROM_SERVICE) != null) {
//            String roomName = intent.getStringExtra(ROOM_NAME);
//            mFragmentManager.beginTransaction().replace(R.id.main_activity_container, ChatListFragment.newChattingFragmentInstance(roomName)).commitAllowingStateLoss();
//        }
//    }
//
//    @Override
//    public void onChatItemClicked(JSONObject chatListModel) {
//        try {
//            String roomName = chatListModel.getString(ROOM_NAME);
////            mFragmentManager.beginTransaction().replace(R.id.main_activity_container, ChattingFragment.newChattingFragmentInstance(roomName)).addToBackStack(null).commitAllowingStateLoss();
//            Intent goToChatting = new Intent(getApplicationContext(), ChattingActivity.class);
//            goToChatting.putExtra(ROOM_NAME, roomName);
//            startActivity(goToChatting);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onChatNewChatMessage(String roomName) {
////        mFragmentManager.beginTransaction().replace(R.id.main_activity_container, ChatListFragment.newChattingFragmentInstance(roomName)).commitAllowingStateLoss();
//        Intent goToChattingActivity = new Intent(getApplicationContext(), ChattingActivity.class);
//        goToChattingActivity.putExtra(ROOM_NAME, roomName);
//        goToChattingActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP & Intent.FLAG_ACTIVITY_SINGLE_TOP & Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(goToChattingActivity);
//    }
//
//    @Override
//    public void onNewChatRoomCreated(String roomName) {
//        mFragmentManager.beginTransaction().replace(R.id.main_activity_container, ChatListFragment.newChattingFragmentInstance(roomName)).commitAllowingStateLoss();
//    }
}
