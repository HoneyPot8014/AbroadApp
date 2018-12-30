package com.example.leeyh.abroadapp.view.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.background.OnResponseReceivedListener;
import com.example.leeyh.abroadapp.controller.MemberListAdapter;
import com.example.leeyh.abroadapp.controller.RecyclerItemClickListener;
import com.example.leeyh.abroadapp.controller.TravelPlanAdapter;
import com.example.leeyh.abroadapp.helper.ApplicationManagement;
import com.example.leeyh.abroadapp.mypage.ChoiceCountryActivity;
import com.example.leeyh.abroadapp.mypage.Dictionary;
import com.example.leeyh.abroadapp.mypage.MyCountryAdapter;
import com.example.leeyh.abroadapp.mypage.MyData;
import com.example.leeyh.abroadapp.mypage.NextActivity;
import com.example.leeyh.abroadapp.view.activity.MemberDetailViewActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.example.leeyh.abroadapp.constants.SocketEvent.MAKE_CHAT_ROOM_FAILED;
import static com.example.leeyh.abroadapp.constants.SocketEvent.MAKE_CHAT_ROOM_SUCCESS;
import static com.example.leeyh.abroadapp.constants.SocketEvent.NEW_ROOM_CHAT;
import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTE_CHAT;
import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTE_MAP;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SAVE_LOCATION;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SAVE_LOCATION_SUCCESS;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SQL_ERROR;
import static com.example.leeyh.abroadapp.constants.StaticString.DISTANCE;
import static com.example.leeyh.abroadapp.constants.StaticString.DOB;
import static com.example.leeyh.abroadapp.constants.StaticString.GENDER;
import static com.example.leeyh.abroadapp.constants.StaticString.LATITUDE;
import static com.example.leeyh.abroadapp.constants.StaticString.LOCATION_CODE;
import static com.example.leeyh.abroadapp.constants.StaticString.LONGITUDE;
import static com.example.leeyh.abroadapp.constants.StaticString.ROOM_NAME;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_INFO;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_NAME;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_UUID;

public class TravelPlanFragment extends Fragment implements OnResponseReceivedListener {

    private ApplicationManagement mAppManager;
    private TravelPlanAdapter mAdapter;
    private SharedPreferences mSharedPreferences;
    private TextView planTextView;
    private Button makePlanButton;
    public ArrayList<MyData> myDataset;
    private Context mContext;
//    public int travelCount = 0;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChatListItemClicked) {
            //   mOnNewChatRoomMaked = (OnNewChatRoomMaked) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChatListItemClicked");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAppManager = (ApplicationManagement) getContext().getApplicationContext();
        mAppManager.setOnResponseReceivedListener(this);
        mAppManager.routeSocket(ROUTE_MAP);
        mAppManager.onResponseFromServer(SQL_ERROR);
        mAppManager.onResponseFromServer(SAVE_LOCATION_SUCCESS);
        mAppManager.onResponseFromServer(NEW_ROOM_CHAT);
//        mContext = getActivity();
        mContext = getContext();
        mSharedPreferences = getActivity().getSharedPreferences(USER_INFO, MODE_PRIVATE);
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        SharedPreferences sp = getActivity().getSharedPreferences("travelplan", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    public TravelPlanFragment(){

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_travel_plan, container, false);

        SharedPreferences hSharedPreferences = getActivity().getSharedPreferences("travelplan", MODE_PRIVATE);
        if(!hSharedPreferences.getString("contacts", "").equals("")){
            String strcontact = hSharedPreferences.getString("contacts" ,"");
            Type listType = new TypeToken<ArrayList<MyData>>(){}.getType();
            myDataset = new Gson().fromJson(strcontact, listType);
        }
        else{
            myDataset = new ArrayList<MyData>();
        }
        makePlanButton = view.findViewById(R.id.makePlanButton);
        planTextView = view.findViewById(R.id.planText);
        RecyclerView recyclerView = view.findViewById(R.id.travelPlanRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager recyclerViewManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(recyclerViewManager);

//        editor.putString("recycler", recyclerView);

        makePlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChoiceCountryActivity.class);
//                intent.putExtra("travelIndex", "0");
                startActivityForResult(intent, 1);
            }
        });

        mAdapter = new TravelPlanAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);
        DividerItemDecoration myDivider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(myDivider);
        recyclerView.addOnItemTouchListener(
                new com.example.leeyh.abroadapp.mypage.RecyclerItemClickListener(getActivity().getApplicationContext(), recyclerView, new com.example.leeyh.abroadapp.mypage.RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), NextActivity.class);
                        intent.putExtra("recyclerCountry", myDataset.get(position).country);
                        intent.putExtra("position", String.valueOf(position));
                        intent.putExtra("travelIndex",String.valueOf(position));

                        mAdapter.notifyDataSetChanged();
                        startActivityForResult(intent, 2);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );
        getLocationPermission();

        return view;
    }

    @Override
    public void onResponseReceived(String onEvent, Object[] object) {
        switch (onEvent) {
            case SQL_ERROR:
                break;
            case SAVE_LOCATION_SUCCESS:
                mAdapter.deleteAllItem();
                JSONArray receivedData = (JSONArray) object[0];
                mAdapter.addList(receivedData);
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        mAdapter.notifyDataSetChanged();
                    }
                }.sendEmptyMessage(0);
                routeSocketToChatting();
                break;
            case NEW_ROOM_CHAT:
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        Toast.makeText(mAppManager, "새로운 채팅이 도착했어요", Toast.LENGTH_SHORT).show();
                    }
                }.sendEmptyMessage(0);
                break;

            case MAKE_CHAT_ROOM_FAILED:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext()
                , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            int permission = getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (permission == PackageManager.PERMISSION_DENIED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("위치권한이 필요합니다.")
                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_CODE);
                                }
                            })
                            .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .create().show();
                }
            }
            return;
        }

    }

    public void routeSocketToChatting() {
        mAppManager.routeSocket(ROUTE_CHAT);
        mAppManager.onResponseFromServer(NEW_ROOM_CHAT);
        mAppManager.onResponseFromServer(MAKE_CHAT_ROOM_SUCCESS);
        mAppManager.onResponseFromServer(MAKE_CHAT_ROOM_FAILED);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();
        // Activity 가 종료되기 전에 저장한다
        // SharedPreferences 에 설정값(특별히 기억해야할 사용자 값)을 저장하기
        /*SharedPreferences sf = getSharedPreferences(sfName, 0);
        SharedPreferences.Editor editor = sf.edit();//저장하려면 editor가 필요
        String str = et.getText().toString(); // 사용자가 입력한 값
        editor.putString("name", str); // 입력
        editor.putString("xx", "xx"); // 입력
        editor.commit(); // 파일에 최종 반영함*/
        SharedPreferences hSharedPreferences = getActivity().getSharedPreferences("travelplan", MODE_PRIVATE);
        SharedPreferences.Editor editor = hSharedPreferences.edit();
        Gson gson = new GsonBuilder().create();
        Type listType = new TypeToken<ArrayList<MyData>>(){}.getType();
        String json = gson.toJson(myDataset, listType);//        listitemjson = gson.toJson(list_itemArrayList, listitemType);
        editor.putString("contacts", json);
        editor.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String geturiPath(Uri contentUri) {

        if(contentUri.getPath().startsWith("/storage")){
            return contentUri.getPath();
        }

        String id = DocumentsContract.getDocumentId(contentUri).split(":")[1];
        String[] columns = {MediaStore.Files.FileColumns.DATA};
        String selection = MediaStore.Files.FileColumns._ID+" = " + id;
        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Files.getContentUri("external"), columns, selection, null, null);
        try{
            int columnIndex = cursor.getColumnIndex(columns[0]);
            if(cursor.moveToFirst()){
                return cursor.getString(columnIndex);
            }
        }finally {
            cursor.close();
        }
        return null;
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        System.out.println("travelplanfragment : " + resultCode);
        System.out.println("travelplanfragment : " + resultCode + ", " + requestCode);
        if (requestCode == 101) {
            if (data != null) {
                if (data.getStringExtra(ROOM_NAME) != null) {
                    String roomName = data.getStringExtra(ROOM_NAME);

                }
            }
        }

        if(resultCode == RESULT_OK && requestCode == 1){
//            System.out.println("uri : " + data.getParcelableExtra("img"));
            if( data.getParcelableExtra("img") == null) {
                MyData myData = new MyData(data.getStringExtra("startdate") + " - " + data.getStringExtra("enddate"), data.getStringExtra("countryname"), data.getStringExtra("budget"), null);
                SharedPreferences sp = getActivity().getSharedPreferences("shared"+data.getStringExtra("countryname"), MODE_PRIVATE);
                String leftMoney = sp.getString("left", "");
                myData.budget = leftMoney;
                System.out.println("mydata.budget : " + myData.budget);
                myDataset.add(myData);
            }
            else {
                Uri uri = data.getParcelableExtra("img");
                Bitmap bitmap = null;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;

                try {
                    bitmap = BitmapFactory.decodeFile(geturiPath(uri), options);
                    ExifInterface exif = new ExifInterface(geturiPath(uri));
                    int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                    bitmap = rotateBitmap(bitmap, exifOrientation);

                } catch (NullPointerException  e) {
                    e.printStackTrace();
                } catch (Exception e){
                    Toast.makeText(getActivity(), "오류발생: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

                MyData myData = new MyData(data.getStringExtra("startdate") + " - " + data.getStringExtra("enddate"), data.getStringExtra("countryname"), data.getStringExtra("budget"), bitmap);
                SharedPreferences sp = getActivity().getSharedPreferences("shared"+data.getStringExtra("countryname"), MODE_PRIVATE);
                String leftMoney = sp.getString("left", "");
                myData.budget = leftMoney;
                System.out.println("mydata.budget : " + myData.budget);
//                SharedPreferences.Editor editor = sp.edit();
                /*if(getActivity().getIntent().getStringExtra("leftMoney") != null){
                    myData.budget = getActivity().getIntent().getStringExtra("leftMoney");
                    System.out.println("budget : " + myData.budget);
                }*/
                myDataset.add(myData);
            }
            mAdapter.notifyDataSetChanged();
        }
        if(requestCode == 2 && resultCode == RESULT_OK)
        {
            myDataset.get(Integer.parseInt(data.getStringExtra("position"))).budget = data.getStringExtra("leftMoney");
            System.out.println("mydata.budget : " + myDataset.get(Integer.parseInt(data.getStringExtra("position"))).budget);
            mAdapter.notifyDataSetChanged();
        }

    }

    public Bitmap rotateBitmap(Bitmap bitmap, int orientation){
        Matrix matrix = new Matrix();
        switch (orientation){
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try{
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch(OutOfMemoryError e){
            e.printStackTrace();
            return null;
        }
    }
}
