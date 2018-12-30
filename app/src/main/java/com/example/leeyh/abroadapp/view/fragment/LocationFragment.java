package com.example.leeyh.abroadapp.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.databinding.FragmentLocationBinding;
import com.example.leeyh.abroadapp.viewmodel.UserViewModel;

public class LocationFragment extends Fragment {

    FragmentLocationBinding mBinding;

//    private ApplicationManagement mAppManager;
//    private FusedLocationProviderClient mFusedLocationClient;
//    private MemberListAdapter mAdapter;
//    private SharedPreferences mSharedPreferences;
//    private ImageButton mSettingButton;
//    private boolean mIsShown = false;
//    private double mDistance = 5.0;
//    private ImageButton fragSearchButton;
//    private View diag;
//    private SeekBar seekBar;
//    private TextView infoText;
//    private BottomSheetDialog dialog;
//    private int locData;
//    private Button searchButton;
//    private OnNewChatRoomMaked mOnNewChatRoomMaked;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnChatListItemClicked) {
//            mOnNewChatRoomMaked = (OnNewChatRoomMaked) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnChatListItemClicked");
//        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mAppManager = (ApplicationManagement) getContext().getApplicationContext();
//        mAppManager.setOnResponseReceivedListener(this);
//        mAppManager.routeSocket(ROUTE_MAP);
//        mAppManager.onResponseFromServer(SQL_ERROR);
//        mAppManager.onResponseFromServer(SAVE_LOCATION_SUCCESS);
//        mAppManager.onResponseFromServer(NEW_ROOM_CHAT);
//        mSharedPreferences = getActivity().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_location, container, false);
        View view = mBinding.getRoot();
        UserViewModel viewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        mBinding.setHandler(viewModel);
//        diag = getActivity().getLayoutInflater().inflate(R.layout.bottom_dialog, null);
//        dialog = new BottomSheetDialog(getActivity());
//        dialog.setContentView(diag);
//        fragSearchButton = view.findViewById(R.id.fragSearchButton);
//        searchButton = diag.findViewById(R.id.searchButton);
//        seekBar = (SeekBar) diag.findViewById(R.id.locSeekBar);
//
//        infoText = diag.findViewById(R.id.infoText);
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
//        RecyclerView recyclerView = view.findViewById(R.id.memberRecyclerView);
//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager recyclerViewManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(recyclerViewManager);
//        mAdapter = new MemberListAdapter(new RecyclerItemClickListener() {
//            //when recycler view clicked handle here onItemClicked.
//            @Override
//            public void onItemClicked(View view, int position, final JSONObject item) {
//                try {
//                    Intent intent = new Intent(getActivity(), MemberDetailViewActivity.class);
//                    intent.putExtra("name", item.getString(USER_UUID));
//                    intent.putExtra("realName", item.getString(USER_NAME));
//                    intent.putExtra("dob", item.getString(DOB));
//                    intent.putExtra("distance", item.getString(DISTANCE));
//                    intent.putExtra("lat", item.getString(LATITUDE));
//                    intent.putExtra("long", item.getString(LONGITUDE));
//                    intent.putExtra("gen", item.getString(GENDER));
//                    //intent.putExtra("onoff",item.getString());
//
//                    startActivityForResult(intent, 101);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//
//                /*AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setMessage("채팅방 개설 ㄱㄱ?").setPositiveButton("네", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        JSONArray makeRoomIdsData = new JSONArray();
//                        try {
//                            makeRoomIdsData.put(mSharedPreferences.getString(USER_UUID, null));
//                            makeRoomIdsData.put(item.getString(USER_UUID));
//                            Log.d("여기2", "onClick: " + item.getString(USER_UUID));
//                            mAppManager.emitRequestToServer(MAKE_CHAT_ROOM, makeRoomIdsData);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.cancel();
//                    }
//                }).create().show();*/
//            }
//        });
//
//        searchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mDistance = locData;
//                mAppManager.routeSocket(ROUTE_MAP);
//                mAppManager.onResponseFromServer(SQL_ERROR);
//                mAppManager.onResponseFromServer(SAVE_LOCATION_SUCCESS);
//                mAppManager.onResponseFromServer(NEW_ROOM_CHAT);
//                getLocationPermission();
//                dialog.dismiss();
//            }
//        });
//
//        fragSearchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                dialog.show();
//            }
//        });
//
//
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                                               @Override
//                                               public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                                                   infoText.setText("location in " + i * 5 + " km");
//                                                   locData = i * 5;
//                                               }
//
//                                               @Override
//                                               public void onStartTrackingTouch(SeekBar seekBar) {
//
//                                               }
//
//                                               @Override
//                                               public void onStopTrackingTouch(SeekBar seekBar) {
//
//                                               }
//                                           }
//        );
//
//        recyclerView.setAdapter(mAdapter);
//        DividerItemDecoration myDivider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
//        recyclerView.addItemDecoration(myDivider);
//        getLocationPermission();
//
//        View toolView = inflater.inflate(R.layout.fragment_location, container, false);
//        Toolbar toolbar = (Toolbar) toolView.findViewById(R.id.toolbarLoc);
//        toolbar.setTitle("hello");
//
//        //for crate home button
//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//        activity.setSupportActionBar(toolbar);
//        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return view;
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
