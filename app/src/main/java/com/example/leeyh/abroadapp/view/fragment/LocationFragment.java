package com.example.leeyh.abroadapp.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.constants.Statistical;
import com.example.leeyh.abroadapp.controller.NearLocationListViewAdapter;
import com.example.leeyh.abroadapp.dataconverter.DataConverter;
import com.example.leeyh.abroadapp.service.OnResponseReceivedListener;
import com.example.leeyh.abroadapp.service.ServiceEventInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTE_MAP;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SAVE_LOCATION;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SAVE_LOCATION_SUCCESS;
import static com.example.leeyh.abroadapp.constants.SocketEvent.USER_LOCATION;
import static com.example.leeyh.abroadapp.constants.StaticString.CITY;
import static com.example.leeyh.abroadapp.constants.StaticString.LOCATION_CODE;
import static com.example.leeyh.abroadapp.constants.StaticString.MIN_DISTANCE_FOR_UPDATE;
import static com.example.leeyh.abroadapp.constants.StaticString.MIN_TIME_FOR_UPDATE;
import static com.example.leeyh.abroadapp.constants.StaticString.NATION;
import static com.example.leeyh.abroadapp.constants.StaticString.ON_EVENT;
import static com.example.leeyh.abroadapp.constants.StaticString.RECEIVED_DATA;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_Id;

public class LocationFragment extends Fragment implements OnResponseReceivedListener {

    private String userId;
    private ServiceEventInterface mSocketListener;
    private TextView mMyLocationTextView;
    private ListView mNearLocationListView;
    private NearLocationListViewAdapter mAdapter;
    private Statistical mAppStatic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString(USER_Id);
        }
        mSocketListener = new ServiceEventInterface(getContext());
        mSocketListener.setResponseListener(this);
        mSocketListener.socketRouting(ROUTE_MAP);
        mSocketListener.socketOnEvent(SAVE_LOCATION_SUCCESS);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_location, container, false);
        mAppStatic = (Statistical) getContext().getApplicationContext();
        mMyLocationTextView = view.findViewById(R.id.my_location_text_view);
        mNearLocationListView = view.findViewById(R.id.near_location_list_view);
        mAdapter = new NearLocationListViewAdapter();
        mNearLocationListView.setAdapter(mAdapter);
//        getLocation();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getLocation() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        if (locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    MIN_TIME_FOR_UPDATE,
                    MIN_DISTANCE_FOR_UPDATE,
                    new LocationListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onLocationChanged(Location location) {
                            double longitude = location.getLongitude();
                            double latitude = location.getLatitude();
                            List<String> addressData = DataConverter.convertAddress(getContext(), latitude, longitude);
                            sendUserLocation(addressData);
                            mMyLocationTextView.setText(addressData.get(0) + " " + addressData.get(1));
                        }

                        @Override
                        public void onStatusChanged(String s, int i, Bundle bundle) {

                        }

                        @Override
                        public void onProviderEnabled(String s) {

                        }

                        @Override
                        public void onProviderDisabled(String s) {

                        }
                    });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSocketRouted() {
        mSocketListener.socketOnEvent(USER_LOCATION);
        mSocketListener.socketOnEvent(SAVE_LOCATION_SUCCESS);
    }

    public static LocationFragment newInstance(String param1) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putString(USER_Id, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResponseReceived(Intent intent) {
        String event = intent.getStringExtra(ON_EVENT);
        switch (event) {
            case USER_LOCATION:
                getLocation();
                break;
            case SAVE_LOCATION_SUCCESS:
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

                break;
        }

    }

    public void sendUserLocation(List<String> address) {
        JSONObject locationData = new JSONObject();
        try {
            locationData.put(USER_Id, userId);
            locationData.put(CITY, address.get(0));
            locationData.put(NATION, address.get(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String locationDataToString = locationData.toString();
        Log.d("여기는", "sendUserLocation: " + locationDataToString);
        mSocketListener.socketEmitEvent(SAVE_LOCATION, locationDataToString);
    }
}
