package com.example.leeyh.abroadapp.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.leeyh.abroadapp.model.UserModel;
import com.example.leeyh.abroadapp.repository.QueryFinishedListener;
import com.example.leeyh.abroadapp.repository.UserRepository;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import static com.example.leeyh.abroadapp.constants.StaticString.LOAD_IMAGE_FAILED;
import static com.example.leeyh.abroadapp.constants.StaticString.LOCATION_FAILED;

public class UserViewModel extends AndroidViewModel {

    private Application mApplication;
    private FirebaseAuth mAuth;
    private UserRepository mRepository;
    private FusedLocationProviderClient mLocationProvider;
    private MutableLiveData<String> mStatus;
    private MutableLiveData<Bitmap> mProfileBitmap;
    private ArrayList<UserModel> userList;
    public MutableLiveData<UserModel> mMutableUser;

    public UserViewModel(@NonNull Application application, UserRepository repository) {
        super(application);
        this.mApplication = application;
        this.mAuth = FirebaseAuth.getInstance();
        this.mRepository = repository;
        this.mLocationProvider = LocationServices.getFusedLocationProviderClient(application);

        this.userList = new ArrayList<>();
        this.mProfileBitmap = new MutableLiveData<>();
        this.mMutableUser = new MutableLiveData<>();
        this.mStatus = new MutableLiveData<>();

        mRepository.setQueryListener(new QueryFinishedListener() {
            @Override
            public void onQueryFinished(UserModel userModel) {
                if(userModel != null) {
                    if (!mAuth.getCurrentUser().getUid().equals(userModel.getUid())) {
                        mMutableUser.setValue(userModel);
                        userList.add(userModel);
                        mStatus.setValue(mRepository.getStatus());
                    }
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    public void saveUserLocation() {
        mLocationProvider.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                mRepository.saveLocationQuery(latitude, longitude, 5);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mStatus.setValue(LOCATION_FAILED);
            }
        });

    }

    public void getProfileBitmap(UserModel userModel) {
        if(userModel.getUserImageUrl() != null) {
            Glide.with(mApplication)
                    .asBitmap()
                    .load(userModel.getUserImageUrl())
                    .addListener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            mStatus.setValue(LOAD_IMAGE_FAILED);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            mProfileBitmap.setValue(resource);
                            return false;
                        }
                    }).submit();
        }

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

    public LiveData<UserModel> getUser() {
        return mMutableUser;
    }

    public MutableLiveData<String> getStatus() {
        return mStatus;
    }


}
