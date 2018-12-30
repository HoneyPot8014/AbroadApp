package com.example.leeyh.abroadapp.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.background.OnResponseReceivedListener;
import com.example.leeyh.abroadapp.helper.ApplicationManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.leeyh.abroadapp.constants.SocketEvent.GET_MY_PLAN;
import static com.example.leeyh.abroadapp.constants.SocketEvent.GET_MY_PLAN_SUCCESS;
import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTE_CHAT;
import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTE_MY_PAGE;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SAVE_PLAN;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SAVE_PLAN_SUCCESS;
import static com.example.leeyh.abroadapp.constants.StaticString.PLAN;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_INFO;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_UUID;

public class MyPageFragment extends Fragment implements OnResponseReceivedListener {

    private ApplicationManagement mAppManager;
    private Button mMakePlanButton;
    private SharedPreferences mSharedPreferences;
    private TextView mInputTextView;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChatListItemClicked) {
            //   mOnNewChatRoomMaked = (OnNewChatRoomMaked) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChatListItemClicked");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppManager = (ApplicationManagement) getContext().getApplicationContext();
        mAppManager.routeSocket(ROUTE_MY_PAGE);
        mAppManager.setOnResponseReceivedListener(this);
        mAppManager.onResponseFromServer(GET_MY_PLAN_SUCCESS);
        mAppManager.onResponseFromServer(SAVE_PLAN_SUCCESS);
        mSharedPreferences = getActivity().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        getMyPlan();
        // mAppManager.setOnResponseReceivedListener(this);
        //mAppManager.routeSocket(ROUTE_MAP);
        //  mAppManager.onResponseFromServer(SQL_ERROR);
        //   mAppManager.onResponseFromServer(SAVE_LOCATION_SUCCESS);
        //   mAppManager.onResponseFromServer(NEW_ROOM_CHAT);
        // mSharedPreferences = getActivity().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);
        mMakePlanButton = view.findViewById(R.id.button);
        ImageView profile = view.findViewById(R.id.profileImg);
        mInputTextView = view.findViewById(R.id.inputTextView);
        Glide.with(getContext()).load("http://49.236.137.55/profile?id="
                + mSharedPreferences.getString(USER_UUID, null) + ".jpeg").into(profile);
        mMakePlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveBtnClicked();
            }
        });
        // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    private void onSaveBtnClicked() {
        JSONObject savePlanData = new JSONObject();
        try {
            savePlanData.put(USER_UUID, mSharedPreferences.getString(USER_UUID, null));
            savePlanData.put(PLAN, mInputTextView.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAppManager.emitRequestToServer(SAVE_PLAN, savePlanData);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void getMyPlan() {
        JSONObject getMyPlanData = new JSONObject();
        try {
            getMyPlanData.put(USER_UUID, mSharedPreferences.getString(USER_UUID, null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAppManager.emitRequestToServer(GET_MY_PLAN, getMyPlanData);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (data != null) {
            }
        }
    }

    @Override
    public void onResponseReceived(String onEvent, Object[] object) {
        switch (onEvent) {
            case SAVE_PLAN_SUCCESS:
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        Toast.makeText(getContext(), "저장 완료", Toast.LENGTH_SHORT).show();
                    }
                };
                break;
            case GET_MY_PLAN_SUCCESS:
                JSONArray receivedArrayDat = (JSONArray) object[0];
                try {
                    JSONObject jsonObject = receivedArrayDat.getJSONObject(0);
                    final String data = jsonObject.getString(PLAN);
                    if(data != null || !data.equals("")) {
                        new Handler(Looper.getMainLooper()) {
                            @Override
                            public void handleMessage(Message msg) {
                                mInputTextView.setText(data);
                            }
                        }.sendEmptyMessage(0);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
