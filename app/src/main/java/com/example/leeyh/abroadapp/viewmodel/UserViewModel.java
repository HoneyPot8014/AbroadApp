package com.example.leeyh.abroadapp.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.example.leeyh.abroadapp.repository.UserRepository;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class UserViewModel extends AndroidViewModel {

    private Application mApplication;
    private UserRepository mRepository;
    private FusedLocationProviderClient mLocationProvider;


    public UserViewModel(@NonNull Application application, UserRepository repository) {
        super(application);
        this.mApplication = application;
        this.mRepository = repository;
        this.mLocationProvider = LocationServices.getFusedLocationProviderClient(application);
    }

    @SuppressLint("MissingPermission")
    public void saveUserLocation() {
        mLocationProvider.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                mRepository.saveLocation(latitude, longitude);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
    public static class UserViewModelFactory extends ViewModelProvider.NewInstanceFactory {

        private Application mApplication;
        private UserRepository mRepository;

        public UserViewModelFactory(Application application, UserRepository repository) {
            this.mApplication = application;
            this.mRepository = repository;
        }
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(UserViewModel.class)) {
                return (T) new UserViewModel(mApplication, mRepository);
            }
            throw new Fragment.InstantiationException("not viewModel class", null);
        }

    }


}
