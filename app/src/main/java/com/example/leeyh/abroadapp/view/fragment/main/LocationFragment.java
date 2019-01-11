package com.example.leeyh.abroadapp.view.fragment.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.adapters.DividerDecorator;
import com.example.leeyh.abroadapp.adapters.LocationUserAdapter;
import com.example.leeyh.abroadapp.databinding.FragmentLocationBinding;
import com.example.leeyh.abroadapp.model.UserModel;
import com.example.leeyh.abroadapp.viewmodel.UserViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationFragment extends Fragment implements OnMapReadyCallback {

    FragmentLocationBinding mBinding;

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
        UserViewModel viewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        mBinding.setHandler(viewModel);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap gmap = googleMap;
        gmap.setMinZoomPreference(12);
//        LatLng ny = new LatLng(Double.valueOf(uLat), Double.valueOf(uLong));
//        googleMap.addMarker(new MarkerOptions().position(ny)
//                .title("Marker in Korea"));
//        gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }

    private void init() {
        DividerDecorator decorator = new DividerDecorator(3);
        LocationUserAdapter adapter = new LocationUserAdapter();
        mBinding.fragmentLocationRecyclerView.setAdapter(adapter);
        mBinding.fragmentLocationRecyclerView.addItemDecoration(decorator);
        mBinding.fragmentLocationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));
        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea"));
        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea"));
        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea"));
        adapter.addItem(new UserModel("111", "111", "111", "Bum", "26", "man", "Korea"));
        adapter.notifyDataSetChanged();

        mBinding.locationFragmentMapView.getMapAsync(this);
    }

//    @Override
//    public void onResponseReceived(String onEvent, Object[] object) {
//        switch (onEvent) {
//            case SQL_ERROR:
//                break;
//            case SAVE_LOCATION_SUCCESS:
//                mAdapter.deleteAllItem();
//                JSONArray receivedData = (JSONArray) object[0];
//                mAdapter.addList(receivedData);
//                new Handler(Looper.getMainLooper()) {
//                    @Override
//                    public void handleMessage(Message msg) {
//                        super.handleMessage(msg);
//                        mAdapter.notifyDataSetChanged();
//                    }
//                }.sendEmptyMessage(0);
//                routeSocketToChatting();
//                break;
//            case NEW_ROOM_CHAT:
//                new Handler(Looper.getMainLooper()) {
//                    @Override
//                    public void handleMessage(Message msg) {
//                        super.handleMessage(msg);
//                        Toast.makeText(mAppManager, "새로운 채팅이 도착했어요", Toast.LENGTH_SHORT).show();
//                    }
//                }.sendEmptyMessage(0);
//                break;
////            case MAKE_CHAT_ROOM_SUCCESS:
////                JSONObject makeRoomSuccessObject = (JSONObject) object[0];
////                Log.d("룸생성", "onResponseReceived: " + makeRoomSuccessObject.optString("roomName"));
////                new Handler(Looper.getMainLooper()) {
////                    @Override
////                    public void handleMessage(Message msg) {
////                        super.handleMessage(msg);
////                        Toast.makeText(mAppManager, "성공", Toast.LENGTH_SHORT).show();
////                    }
////                }.sendEmptyMessage(0);
////                break;
//            case MAKE_CHAT_ROOM_FAILED:
//                break;
//        }
//    }

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
//
//    public void sendUserLocation(double latitude, double longitude) {
//        JSONObject locationData = new JSONObject();
//        try {
//            locationData.put(USER_UUID, mSharedPreferences.getString(USER_UUID, null));
//            locationData.put(USER_NAME, mSharedPreferences.getString(USER_NAME, null));
//            locationData.put(DISTANCE, mDistance);
//            locationData.put(LATITUDE, latitude);
//            locationData.put(LONGITUDE, longitude);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        mAppManager.emitRequestToServer(SAVE_LOCATION, locationData);
//    }
//
//    public void routeSocketToChatting() {
//        mAppManager.routeSocket(ROUTE_CHAT);
//        mAppManager.onResponseFromServer(NEW_ROOM_CHAT);
//        mAppManager.onResponseFromServer(MAKE_CHAT_ROOM_SUCCESS);
//        mAppManager.onResponseFromServer(MAKE_CHAT_ROOM_FAILED);
//    }
//
//    /*public void onSearchClicked(View view) {
//        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
//        View diag = getActivity().getLayoutInflater().inflate(R.layout.bottom_dialog, null);
//        dialog.setContentView(diag);
//        // dialog.setContentView();
//        Log.d("button", "button func" );
//        Toast.makeText(mAppManager, "새로운 채팅이 도착했어요", Toast.LENGTH_SHORT).show();
//        dialog.show();
//    }*/
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 101) {
//            if(data != null) {
//                if(data.getStringExtra(ROOM_NAME) != null) {
//                    String roomName = data.getStringExtra(ROOM_NAME);
//                    mOnNewChatRoomMaked.onNewChatRoomCreated(roomName);
//                }
//            }
//        }
//    }
}
