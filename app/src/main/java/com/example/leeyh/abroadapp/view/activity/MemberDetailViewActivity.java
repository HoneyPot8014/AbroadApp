package com.example.leeyh.abroadapp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.leeyh.abroadapp.R;

import android.os.Bundle;
         import android.support.v7.app.AppCompatActivity;
         import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leeyh.abroadapp.helper.ApplicationManagement;
import com.google.android.gms.maps.CameraUpdateFactory;
         import com.google.android.gms.maps.GoogleMap;
         import com.google.android.gms.maps.MapView;
         import com.google.android.gms.maps.OnMapReadyCallback;
         import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;

import static com.example.leeyh.abroadapp.constants.SocketEvent.MAKE_CHAT_ROOM;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_INFO;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_UUID;

public class MemberDetailViewActivity extends AppCompatActivity implements OnMapReadyCallback{
    //implements OnMapReadyCallback
    private MapView mapView;
    private GoogleMap gmap;
    private String UUID;
    private ImageView profileImg;
    private static final String MAP_VIEW_BUNDLE_KEY = "AIzaSyD3q4iAopnm9lr3CHbGXpfBFfnhBAY4w2c";
    private Button messageButton;
    private SharedPreferences mSharedPreferences;
    private ApplicationManagement mAppManager;
    private TextView nameAndAgeTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_detail_view);
        nameAndAgeTextView = findViewById(R.id.nameAndAgeTextView);
        messageButton = findViewById(R.id.messageButton);
        mSharedPreferences = getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        mAppManager = (ApplicationManagement) getApplicationContext();
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray makeRoomIdsData = new JSONArray();

                    makeRoomIdsData.put(mSharedPreferences.getString(USER_UUID, null));
                    makeRoomIdsData.put(UUID);

                    mAppManager.emitRequestToServer(MAKE_CHAT_ROOM, makeRoomIdsData);

            }
        });
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }
    @Override
    protected void onResume() {
        super.onResume();
       mapView.onResume();
        Intent intent = getIntent();
        UUID = intent.getStringExtra("name");
        profileImg = findViewById(R.id.profileImg);
        nameAndAgeTextView.setText(mSharedPreferences+", 25(M)");
        Glide.with(getApplicationContext()).load("http://49.236.137.55/profile?id=" + UUID + ".jpeg").into(profileImg);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMinZoomPreference(12);
        LatLng ny = new LatLng(40.7143528, -74.0059731);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }
}