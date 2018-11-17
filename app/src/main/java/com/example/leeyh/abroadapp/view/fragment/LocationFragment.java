package com.example.leeyh.abroadapp.view.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.background.OnResponseReceivedListener;
import com.example.leeyh.abroadapp.controller.NearLocationListViewAdapter;
import com.example.leeyh.abroadapp.helper.ApplicationManagement;
import com.example.leeyh.abroadapp.model.UserModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;

import static com.example.leeyh.abroadapp.constants.SocketEvent.MAKE_CHAT_ROOM;
import static com.example.leeyh.abroadapp.constants.SocketEvent.MAKE_CHAT_ROOM_FAILED;
import static com.example.leeyh.abroadapp.constants.SocketEvent.MAKE_CHAT_ROOM_SUCCESS;
import static com.example.leeyh.abroadapp.constants.SocketEvent.NEW_ROOM_CHAT;
import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTE_CHAT;
import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTE_MAP;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SAVE_LOCATION;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SAVE_LOCATION_SUCCESS;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SQL_ERROR;
import static com.example.leeyh.abroadapp.constants.StaticString.LATITUDE;
import static com.example.leeyh.abroadapp.constants.StaticString.LOCATION_CODE;
import static com.example.leeyh.abroadapp.constants.StaticString.LONGITUDE;
import static com.example.leeyh.abroadapp.constants.StaticString.PROFILE;
import static com.example.leeyh.abroadapp.constants.StaticString.TARGET_ID;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_ID;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_INFO;

public class LocationFragment extends Fragment implements OnResponseReceivedListener {

    private String userId;
    private TextView mMyLocationTextView;
    private NearLocationListViewAdapter mAdapter;
    private ApplicationManagement mAppManager;
    private FusedLocationProviderClient mFusedLocationClient;
    private SharedPreferences mSharedPreferences;

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
        userId = mSharedPreferences.getString(USER_ID, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        mMyLocationTextView = view.findViewById(R.id.my_location_text_view);
        ListView mNearLocationListView = view.findViewById(R.id.near_location_list_view);
        mAdapter = new NearLocationListViewAdapter();
        mNearLocationListView.setAdapter(mAdapter);

        mNearLocationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final UserModel user = (UserModel) mAdapter.getItem(i);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("채팅방 개설 ㄱㄱ?").setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        JSONArray makeRoomIdsData = new JSONArray();
                        makeRoomIdsData.put(userId);
                        makeRoomIdsData.put(user.getUserId());
                        mAppManager.emitRequestToServer(MAKE_CHAT_ROOM, makeRoomIdsData);
                    }
                }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).create().show();
            }
        });
        getLocationPermission();
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
//                for (int i = 0; i < receivedData.length(); i++) {
//                    try {
//                        JSONObject nearLocationUser = receivedData.getJSONObject(i);
//                        byte[] profileByteArray = (byte[]) nearLocationUser.get(PROFILE);
//                        if(profileByteArray != null) {
//                            Bitmap nearUserProfile = BitmapFactory.decodeByteArray(profileByteArray, 0, profileByteArray.length);
//
//                            mAppManager.addBitmapToMemoryCache(nearLocationUser.getString(USER_ID), nearUserProfile);
//                        }
////                        mAdapter.addItem(nearLocationUser);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
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
                    mMyLocationTextView.setText(latitude + "\n" + longitude);
                    sendUserLocation(latitude, longitude);
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
//        mAppManager.unregisterSocket();
    }

    public void sendUserLocation(double latitude, double longitude) {
        JSONObject locationData = new JSONObject();
        try {
            locationData.put(USER_ID, userId);
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
