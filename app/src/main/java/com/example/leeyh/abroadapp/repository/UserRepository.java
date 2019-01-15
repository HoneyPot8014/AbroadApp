package com.example.leeyh.abroadapp.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.leeyh.abroadapp.model.UserModel;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.leeyh.abroadapp.constants.StaticString.ERROR;
import static com.example.leeyh.abroadapp.constants.StaticString.SUCCESS;

public class UserRepository {

    private String mStatus;
    private GeoFire mGeoFire;
    private DatabaseReference mUserDB;
    private QueryFinishedListener mQueryListener;

    public UserRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference locationDB = database.getReference("location");

        mGeoFire = new GeoFire(locationDB);
        mUserDB = database.getReference("users");
    }

    public void saveLocationQuery(final double latitude, final double longitude, final int distance) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mGeoFire.setLocation(uid, new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    mStatus = ERROR;
                } else {
                    GeoQuery geoQuery = mGeoFire.queryAtLocation(new GeoLocation(latitude, longitude), distance);
                    geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                        @Override
                        public void onKeyEntered(String key, final GeoLocation location) {
                            mUserDB.child(key).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    mStatus = SUCCESS;
                                    UserModel user = dataSnapshot.getValue(UserModel.class);
                                    if(user != null) {
                                        user.setLatitude(location.latitude);
                                        user.setLongitude(location.longitude);
                                        mQueryListener.onQueryFinished(user);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    mStatus = ERROR;
                                }
                            });
                        }

                        @Override
                        public void onKeyExited(String key) {
                        }

                        @Override
                        public void onKeyMoved(String key, GeoLocation location) {
                        }

                        @Override
                        public void onGeoQueryReady() {
                        }

                        @Override
                        public void onGeoQueryError(DatabaseError error) {
                            mStatus = ERROR;
                        }
                    });
                }
            }
        });
    }

    public String getStatus() {
        return mStatus;
    }

    public void setQueryListener(QueryFinishedListener listener) {
        this.mQueryListener = listener;
    }

}
