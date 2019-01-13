package com.example.leeyh.abroadapp.view.fragment.main;

import android.annotation.SuppressLint;
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
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationFragment extends Fragment implements OnMapReadyCallback {

    private FragmentLocationBinding mBinding;
    private UserViewModel mViewModel;
    private static final String MAP_VIEW_BUNDLE_KEY = "AIzaSyD3q4iAopnm9lr3CHbGXpfBFfnhBAY4w2c";

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

        mBinding.locationFragmentMapView.onCreate(savedInstanceState);
        mBinding.locationToolBar.inflateMenu(R.menu.location_menu);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap gmap = googleMap;
        gmap.setMinZoomPreference(12);
        LatLng ny = new LatLng(37.56, 126.97);
        googleMap.addMarker(new MarkerOptions().position(ny)
                .title("Marker in Korea"));
        gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }

    private void init(Bundle savedInstanceState) {
        DividerDecorator decorator = new DividerDecorator(3);
        LocationUserAdapter adapter = new LocationUserAdapter();
        mBinding.fragmentLocationRecyclerView.setAdapter(adapter);
        mBinding.fragmentLocationRecyclerView.addItemDecoration(decorator);
        mBinding.fragmentLocationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));
        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is...my plan is..."));
        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
//        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
//        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
//        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
//        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
//        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
//        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
//        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
//        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
//        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
//        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
//        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
//        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
//        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
//        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
//        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
//        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
//        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
//        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
//        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
//        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
//        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
//        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
//        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
//        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea", "my plan is..."));
        adapter.notifyDataSetChanged();

        adapter.setListener(new OnItemClickedListener() {
            @Override
            public void onItemClicked(int position, Object binding) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_container, new MemberDetailFragment()).addToBackStack(null).commit();
            }
        });

        mBinding.locationFragmentMapView.onCreate(savedInstanceState);
        mBinding.locationFragmentMapView.getMapAsync(this);

        BottomDialogBinding dialogBinding = BottomDialogBinding.inflate(getActivity().getLayoutInflater());
        dialogBinding.setHandler(mViewModel);

        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(dialogBinding.getRoot());
        mBinding.locationToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                dialog.show();
                return true;
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
        mBinding.locationFragmentMapView.onSaveInstanceState(mapViewBundle);
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
