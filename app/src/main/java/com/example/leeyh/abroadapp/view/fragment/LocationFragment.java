package com.example.leeyh.abroadapp.view.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.background.OnResponseReceivedListener;
import com.example.leeyh.abroadapp.background.ServiceEventInterface;
import com.example.leeyh.abroadapp.helper.ApplicationManagement;
import com.example.leeyh.abroadapp.controller.NearLocationListViewAdapter;
import com.example.leeyh.abroadapp.model.UserModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.leeyh.abroadapp.constants.SocketEvent.CHAT_CONNECT_SUCCESS;
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
import static com.example.leeyh.abroadapp.constants.StaticString.ON_EVENT;
import static com.example.leeyh.abroadapp.constants.StaticString.RECEIVED_DATA;
import static com.example.leeyh.abroadapp.constants.StaticString.TARGET_ID;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_ID;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_INFO;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_UUID;

public class LocationFragment extends Fragment implements OnResponseReceivedListener {

    private String userId;
    private ServiceEventInterface mSocketListener;
    private TextView mMyLocationTextView;
    private ListView mNearLocationListView;
    private NearLocationListViewAdapter mAdapter;
    private ApplicationManagement mAppStatic;
    private FusedLocationProviderClient mFusedLocationClient;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSocketListener = new ServiceEventInterface(getContext());
        mSocketListener.socketRouting(ROUTE_MAP);
        mSocketListener.setResponseListener(this);
        mAppStatic = (ApplicationManagement) getContext().getApplicationContext();
        mSocketListener.socketOnEvent(SAVE_LOCATION_SUCCESS);
        mSharedPreferences = getActivity().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(USER_ID, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        View view = inflater.inflate(R.layout.fragment_location, container, false);
        mMyLocationTextView = view.findViewById(R.id.my_location_text_view);
        mNearLocationListView = view.findViewById(R.id.near_location_list_view);
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
                        JSONObject makeChatData = new JSONObject();
                        try {
                            makeChatData.put(USER_ID, userId);
                            makeChatData.put(USER_UUID, mSharedPreferences.getString(USER_UUID, null));
                            makeChatData.put(TARGET_ID, user.getUserId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mSocketListener.socketEmitEvent(MAKE_CHAT_ROOM, makeChatData.toString());
                    }
                }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).create().show();
            }
        });
        getLocation();
        return view;
    }

    @Override
    public void onResponseReceived(Intent intent) {
        String event = intent.getStringExtra(ON_EVENT);
        switch (event) {
            case SQL_ERROR:
                Toast.makeText(mAppStatic, "SQL 에러", Toast.LENGTH_SHORT).show();
                break;
            case SAVE_LOCATION_SUCCESS:
                mAdapter.deleteAllItem();
                try {
                    JSONArray memberDataArray = new JSONArray(intent.getStringExtra(RECEIVED_DATA));
                    for (int i = 0; i < memberDataArray.length(); i++) {
                        JSONObject memberData = new JSONObject(memberDataArray.get(i).toString());
                        mAdapter.addItem(memberData);
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSocketListener.socketRouting(ROUTE_CHAT);
                break;
            case CHAT_CONNECT_SUCCESS:
                mSocketListener.socketOnEvent(SQL_ERROR);
                mSocketListener.socketOnEvent(MAKE_CHAT_ROOM_SUCCESS);
                mSocketListener.socketOnEvent(MAKE_CHAT_ROOM_FAILED);
                mSocketListener.socketOnEvent(NEW_ROOM_CHAT);
                break;
            case MAKE_CHAT_ROOM_SUCCESS:
                //Client who request make Room Chat received Success onEvent here.
                Toast.makeText(mAppStatic, "요청한 사람 룸챗 만들기 성공", Toast.LENGTH_SHORT).show();
                break;
            case MAKE_CHAT_ROOM_FAILED:
                //Client who request make Room Chat received Failed onEvent here because target user is not connected
                Toast.makeText(mAppStatic, "요청한 사람 룸챗 만들기 실패", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getLocation() {
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
//                    List<String> addressData = DataConverter.convertAddress(getContext(), latitude, longitude);
                    mMyLocationTextView.setText(latitude + "\n" + longitude);
                    sendUserLocation(latitude, longitude);
                }
            }
        });
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
        String locationDataToString = locationData.toString();
        mSocketListener.socketEmitEvent(SAVE_LOCATION, locationDataToString);
    }
}
