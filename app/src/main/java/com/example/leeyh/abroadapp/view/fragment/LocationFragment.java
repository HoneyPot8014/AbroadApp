package com.example.leeyh.abroadapp.view.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< HEAD
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
=======
import android.view.Window;
import android.view.WindowManager;
>>>>>>> bff0a053998a0d6f026a88f2b18d3687d33170ed
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.background.OnResponseReceivedListener;
import com.example.leeyh.abroadapp.controller.MemberListAdapter;
import com.example.leeyh.abroadapp.controller.RecyclerItemClickListener;
import com.example.leeyh.abroadapp.helper.ApplicationManagement;
import com.example.leeyh.abroadapp.view.activity.MemberDetailViewActivity;
import com.example.leeyh.abroadapp.view.activity.TabBarMainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.leeyh.abroadapp.constants.SocketEvent.MAKE_CHAT_ROOM;
import static com.example.leeyh.abroadapp.constants.SocketEvent.MAKE_CHAT_ROOM_FAILED;
import static com.example.leeyh.abroadapp.constants.SocketEvent.MAKE_CHAT_ROOM_SUCCESS;
import static com.example.leeyh.abroadapp.constants.SocketEvent.NEW_ROOM_CHAT;
import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTE_CHAT;
import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTE_MAP;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SAVE_LOCATION;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SAVE_LOCATION_SUCCESS;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SQL_ERROR;
import static com.example.leeyh.abroadapp.constants.StaticString.DISTANCE;
import static com.example.leeyh.abroadapp.constants.StaticString.LATITUDE;
import static com.example.leeyh.abroadapp.constants.StaticString.LOCATION_CODE;
import static com.example.leeyh.abroadapp.constants.StaticString.LONGITUDE;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_INFO;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_NAME;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_UUID;

public class LocationFragment extends Fragment implements OnResponseReceivedListener {

    private ApplicationManagement mAppManager;
    private FusedLocationProviderClient mFusedLocationClient;
    private MemberListAdapter mAdapter;
    private SharedPreferences mSharedPreferences;
    private ImageButton mSettingButton;
    private boolean mIsShown = false;
    private double mDistance = 5.0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAppManager = (ApplicationManagement) getContext().getApplicationContext();
        mAppManager.setOnResponseReceivedListener(this);
        mAppManager.routeSocket(ROUTE_MAP);
        mAppManager.onResponseFromServer(SQL_ERROR);
        mAppManager.onResponseFromServer(SAVE_LOCATION_SUCCESS);
        mAppManager.onResponseFromServer(NEW_ROOM_CHAT);
        mSharedPreferences = getActivity().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        RecyclerView recyclerView = view.findViewById(R.id.memberRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager recyclerViewManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(recyclerViewManager);
        mAdapter = new MemberListAdapter(new RecyclerItemClickListener() {
            //when recycler view clicked handle here onItemClicked.
            @Override
            public void onItemClicked(View view, int position, final JSONObject item) {
                try {
                    Intent intent = new Intent(getActivity(), MemberDetailViewActivity.class);
                    intent.putExtra("name",item.getString(USER_UUID));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                /*AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("채팅방 개설 ㄱㄱ?").setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        JSONArray makeRoomIdsData = new JSONArray();
                        try {
                            makeRoomIdsData.put(mSharedPreferences.getString(USER_UUID, null));
                            makeRoomIdsData.put(item.getString(USER_UUID));
                            Log.d("여기2", "onClick: " + item.getString(USER_UUID));
                            mAppManager.emitRequestToServer(MAKE_CHAT_ROOM, makeRoomIdsData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).create().show();*/
            }
        });
        recyclerView.setAdapter(mAdapter);
        DividerItemDecoration myDivider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(myDivider);
        getLocationPermission();

        View toolView = inflater.inflate(R.layout.fragment_location, container, false);
        Toolbar toolbar = (Toolbar) toolView.findViewById(R.id.toolbarLoc);
        toolbar.setTitle("hello");

        //for crate home button
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSettingButton = view.findViewById(R.id.imageButton);
        final LinearLayout settingMenu = view.findViewById(R.id.location_menu_layout);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDistance = ((TextView) view).getText().toString().charAt(0);
                mAppManager.routeSocket(ROUTE_MAP);
                mAppManager.onResponseFromServer(SQL_ERROR);
                mAppManager.onResponseFromServer(SAVE_LOCATION_SUCCESS);
                mAppManager.onResponseFromServer(NEW_ROOM_CHAT);
                getLocationPermission();
                settingMenu.setVisibility(View.GONE);
                mIsShown = !mIsShown;
            }
        };
        TextView distance3 = view.findViewById(R.id.location_distance_3);
        distance3.setOnClickListener(clickListener);
        TextView distance5 = view.findViewById(R.id.location_distance_5);
        distance5.setOnClickListener(clickListener);
        TextView distance7 = view.findViewById(R.id.location_distance_7);
        distance7.setOnClickListener(clickListener);

        mSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mIsShown) {
                    settingMenu.setVisibility(View.VISIBLE);
                    mIsShown = !mIsShown;
                } else {
                    settingMenu.setVisibility(View.GONE);
                    mIsShown = !mIsShown;
                }
            }
        });

        return view;
    }

    @Override
    public void onResponseReceived(String onEvent, Object[] object) {
        switch (onEvent) {
            case SQL_ERROR:
                break;
            case SAVE_LOCATION_SUCCESS:
                mAdapter.deleteAllItem();
                JSONArray receivedData = (JSONArray) object[0];
                mAdapter.addList(receivedData);
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        mAdapter.notifyDataSetChanged();
                    }
                }.sendEmptyMessage(0);
                routeSocketToChatting();
                break;
            case NEW_ROOM_CHAT:
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        Toast.makeText(mAppManager, "새로운 채팅이 도착했어요", Toast.LENGTH_SHORT).show();
                    }
                }.sendEmptyMessage(0);
                break;
            case MAKE_CHAT_ROOM_SUCCESS:
                JSONObject makeRoomSuccessObject = (JSONObject) object[0];
                Log.d("룸생성", "onResponseReceived: " + makeRoomSuccessObject.optString("roomName"));
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        Toast.makeText(mAppManager, "성공", Toast.LENGTH_SHORT).show();
                    }
                }.sendEmptyMessage(0);
                break;
            case MAKE_CHAT_ROOM_FAILED:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext()
                , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            int permission = getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (permission == PackageManager.PERMISSION_DENIED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("위치권한이 필요합니다.")
                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_CODE);
                                }
                            })
                            .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .create().show();
                }
            }
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
//                    mMyLocationTextView.setText(latitude + "\n" + longitude);
                    sendUserLocation(latitude, longitude);
                }
            }
        });
    }

    public void sendUserLocation(double latitude, double longitude) {
        JSONObject locationData = new JSONObject();
        try {
            locationData.put(USER_UUID, mSharedPreferences.getString(USER_UUID, null));
            locationData.put(USER_NAME, mSharedPreferences.getString(USER_NAME, null));
            locationData.put(DISTANCE, mDistance);
            locationData.put(LATITUDE, latitude);
            locationData.put(LONGITUDE, longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAppManager.emitRequestToServer(SAVE_LOCATION, locationData);
    }

    public void routeSocketToChatting() {
        mAppManager.routeSocket(ROUTE_CHAT);
        mAppManager.onResponseFromServer(NEW_ROOM_CHAT);
        mAppManager.onResponseFromServer(MAKE_CHAT_ROOM_SUCCESS);
        mAppManager.onResponseFromServer(MAKE_CHAT_ROOM_FAILED);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
