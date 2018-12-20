package com.example.leeyh.abroadapp.view.fragment;

        import android.Manifest;
        import android.app.AlertDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.content.pm.PackageManager;
        import android.location.Location;
        import android.os.Build;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Looper;
        import android.os.Message;
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
        import android.view.WindowManager;
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
        import com.example.leeyh.abroadapp.view.activity.MemberDetailViewActivity;
        import com.google.android.gms.location.FusedLocationProviderClient;
        import com.google.android.gms.location.LocationServices;
        import com.google.android.gms.tasks.OnSuccessListener;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

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

public class MyPageFragment extends Fragment {

    private ApplicationManagement mAppManager;
    private Button makePlanButton;


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
       // mAppManager.setOnResponseReceivedListener(this);
        //mAppManager.routeSocket(ROUTE_MAP);
      //  mAppManager.onResponseFromServer(SQL_ERROR);
     //   mAppManager.onResponseFromServer(SAVE_LOCATION_SUCCESS);
     //   mAppManager.onResponseFromServer(NEW_ROOM_CHAT);
       // mSharedPreferences = getActivity().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);
       // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if(data != null) {

            }
        }
    }
}
