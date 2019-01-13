package com.example.leeyh.abroadapp.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.leeyh.abroadapp.model.LocationModel;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserRepository {

    private DatabaseReference locationDB;
    private GeoFire mGeoFire;

    public UserRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        locationDB = database.getReference("location");
        mGeoFire = new GeoFire(locationDB);
    }

    public void saveLocation(final double latitude, final double longitude) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mGeoFire.setLocation(uid, new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if(error != null) {
                    //handle error
                } else {
                    GeoQuery geoQuery = mGeoFire.queryAtLocation(new GeoLocation(latitude, longitude), 0.6);
                    geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                        @Override
                        public void onKeyEntered(String key, GeoLocation location) {
                            Log.d("지도1", "onKeyEntered: " + key + location.latitude + location.longitude);
                        }

                        @Override
                        public void onKeyExited(String key) {
                            Log.d("지도2", "onKeyExited: " + key);
                        }

                        @Override
                        public void onKeyMoved(String key, GeoLocation location) {
                            Log.d("지도3", "onKeyEntered: " + key + location.latitude + location.longitude);
                        }

                        @Override
                        public void onGeoQueryReady() {
                            Log.d("지도4", "onKeyEntered: ");
                        }

                        @Override
                        public void onGeoQueryError(DatabaseError error) {
                            Log.d("지도5", "onKeyEntered: ");
                        }
                    });
                }
            }
        });
    }
}
