package com.example.leeyh.abroadapp.service;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import static com.example.leeyh.abroadapp.constants.StaticString.MIN_DISTANCE_FOR_UPDATE;
import static com.example.leeyh.abroadapp.constants.StaticString.MIN_TIME_FOR_UPDATE;

public class GpsService extends Service implements LocationListener {

    private Context mContext;
    private Location mLocation;
    private double mLatitude;
    private double mLongitude;
    private boolean isEnabled = false;


    public GpsService(Context context) {
        mContext = context;
        getLocation();
    }

    public GpsService() {

    }

    @TargetApi(23)
    public Location getLocation() {
        try {
            LocationManager mLocationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            boolean isGpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGpsEnabled && !isNetworkEnabled) {
                //can't get location handle here
            } else {
                isEnabled = true;
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return null;
                    }
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_DISTANCE_FOR_UPDATE,
                            MIN_TIME_FOR_UPDATE,
                            this);
                    mLocation = mLocationManager.getLastKnownLocation(mLocationManager.NETWORK_PROVIDER);
                    if (mLocation != null) {
                        mLatitude = mLocation.getLatitude();
                        mLongitude = mLocation.getLongitude();
                    }
                }
                if (isGpsEnabled) {
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MIN_DISTANCE_FOR_UPDATE,
                            MIN_TIME_FOR_UPDATE,
                            this);
                    mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (mLocation != null) {
                        mLatitude = mLocation.getLatitude();
                        mLongitude = mLocation.getLongitude();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mLocation;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

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

    public double getLatitude() {
        Log.d("위치정보", "getLatitude: " + mLongitude);
        return mLatitude;
    }

    public double getLongitude() {
        Log.d("위치정보", "getLatitude: " + mLongitude);
        return mLongitude;
    }

    public boolean gpsIsEnabled() {
        return this.isEnabled;
    }
}
