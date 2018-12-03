package com.example.leeyh.abroadapp.background;

import android.Manifest;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.leeyh.abroadapp.constants.SocketEvent;
import com.example.leeyh.abroadapp.helper.ApplicationManagement;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.example.leeyh.abroadapp.constants.SocketEvent.GET_LOCATION_USER;
import static com.example.leeyh.abroadapp.constants.SocketEvent.GET_LOCATION_USER_SUCCESS;
import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTE_MAP;
import static com.example.leeyh.abroadapp.constants.StaticString.LATITUDE;
import static com.example.leeyh.abroadapp.constants.StaticString.LONGITUDE;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_INFO;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_NAME;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_UUID;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ProfileCachingJobService extends JobService {

    private Socket mSocket;
    private ApplicationManagement mAppManager = (ApplicationManagement) getApplication();
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d("잡서비스", "onStartJob: ");
        IO.Options socketOptions = new IO.Options();
        socketOptions.reconnection = true;
        socketOptions.forceNew = false;
        socketOptions.multiplex = false;
        socketOptions.secure = true;
        socketOptions.timeout = 1000 * 60 * 100;
        try {
            mSocket = IO.socket(SocketEvent.DEFAULT_HOST, socketOptions);
            mSocket = mSocket.io().socket(ROUTE_MAP).connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        final SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        if(sharedPreferences.getString(USER_UUID, null) != null && sharedPreferences.getString(USER_NAME, null) != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        JSONObject getUserLocationData = new JSONObject();
                        try {
                            getUserLocationData.put(USER_UUID, sharedPreferences.getString(USER_UUID, null));
                            getUserLocationData.put(USER_NAME, sharedPreferences.getString(USER_NAME, null));
                            getUserLocationData.put(LATITUDE, latitude);
                            getUserLocationData.put(LONGITUDE, longitude);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mAppManager.emitRequestToServer(GET_LOCATION_USER, getUserLocationData);
                    }
                }
            });
        }
        mSocket.on(GET_LOCATION_USER_SUCCESS, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray receivedData = (JSONArray) args[0];
                for(int i = 0; i < receivedData.length(); i++) {
                    try {
                        JSONObject userData = receivedData.getJSONObject(i);
                        Glide.with(getApplicationContext()).load("http://49.236.137.55/profile?id=" + userData.getString(USER_UUID) + ".jpeg").submit();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mSocket.disconnect();
            }
        });
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        onStartJob(jobParameters);
        return false;
    }
}
