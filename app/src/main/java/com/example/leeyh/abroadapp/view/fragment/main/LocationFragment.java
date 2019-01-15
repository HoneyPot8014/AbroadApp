package com.example.leeyh.abroadapp.view.fragment.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.adapters.DividerDecorator;
import com.example.leeyh.abroadapp.adapters.LocationUserAdapter;
import com.example.leeyh.abroadapp.adapters.OnItemClickedListener;
import com.example.leeyh.abroadapp.databinding.BottomDialogBinding;
import com.example.leeyh.abroadapp.databinding.FragmentLocationBinding;
import com.example.leeyh.abroadapp.model.UserModel;
import com.example.leeyh.abroadapp.viewmodel.UserViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationFragment extends Fragment implements OnMapReadyCallback {

    private FragmentLocationBinding mBinding;
    private UserViewModel mViewModel;
    private GoogleMap mGoogleMap;
    private double mLatitude;
    private double mLongitude;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_location, container, false);
        View view = mBinding.getRoot();
        mViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        mBinding.setHandler(mViewModel);

        mViewModel.getLocation().observe(this, new Observer<double[]>() {
            @Override
            public void onChanged(@Nullable double[] doubles) {
                mLatitude = doubles[0];
                mLongitude = doubles[1];

                if (mGoogleMap != null) {
                    LatLng ny = new LatLng(mLatitude, mLongitude);
                    mGoogleMap.addMarker(new MarkerOptions().position(ny)
                            .title("I'm here!"));
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
                }
            }
        });

        mBinding.locationFragmentMapView.onCreate(savedInstanceState);
        mBinding.locationFragmentMapView.onResume();
        mBinding.locationFragmentMapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (mLatitude != 0 && mLongitude != 0) {
            LatLng ny = new LatLng(mLatitude, mLongitude);
            googleMap.addMarker(new MarkerOptions().position(ny)
                    .title("I'm here!"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
        }
    }

    private void init() {
        DividerDecorator decorator = new DividerDecorator(3);
        final LocationUserAdapter adapter = new LocationUserAdapter();
        mBinding.fragmentLocationRecyclerView.setAdapter(adapter);
        mBinding.fragmentLocationRecyclerView.addItemDecoration(decorator);
        mBinding.fragmentLocationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));

        adapter.setListener(new OnItemClickedListener() {
            @Override
            public void onItemClicked(int position, Object binding) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.member_detail_container, MemberDetailFragment.newInstance(adapter.getItemModel(position)))
                        .addToBackStack(null)
                        .commit();
            }
        });


        BottomDialogBinding dialogBinding = BottomDialogBinding.inflate(getActivity().getLayoutInflater());
        dialogBinding.setHandler(mViewModel);

        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(dialogBinding.getRoot());

        mBinding.fragmentLocationSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBinding.locationFragmentMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.locationFragmentMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBinding.locationFragmentMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinding.locationFragmentMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mBinding.locationFragmentMapView.onLowMemory();
    }

    //    @RequiresApi(api = Build.VERSION_CODES.M)
//    public void getLocationPermission() {
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext()
//                , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            int permission = getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
//            if (permission == PackageManager.PERMISSION_DENIED) {
//                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
//                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
//                    dialog.setTitle("위치권한이 필요합니다.")
//                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
//                                @RequiresApi(api = Build.VERSION_CODES.M)
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_CODE);
//                                }
//                            })
//                            .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    dialogInterface.cancel();
//                                }
//                            })
//                            .create().show();
//                }
//            }
//            return;
//        }
//        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if (location != null) {
//                    double latitude = location.getLatitude();
//                    double longitude = location.getLongitude();
////                    mMyLocationTextView.setText(latitude + "\n" + longitude);
//                    sendUserLocation(latitude, longitude);
//                }
//            }
//        });
//    }
}
