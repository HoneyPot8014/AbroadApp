package com.example.leeyh.abroadapp.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.background.OnResponseReceivedListener;
import com.example.leeyh.abroadapp.controller.ChatListAdapter;
import com.example.leeyh.abroadapp.helper.ApplicationManagement;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.leeyh.abroadapp.constants.SocketEvent.CHAT_LIST;
import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTE_CHAT;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_ID;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_INFO;

public class ChatListFragment extends Fragment implements OnResponseReceivedListener {

    private ListView mChattingListListView;
    private ChatListAdapter mChatListAdapter;
    private ApplicationManagement mAppManager;
//    private ServiceEventInterface mSocketListener;
    private String mUserId;
//    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppManager = (ApplicationManagement) getContext().getApplicationContext();
        mAppManager.setOnResponseReceivedListener(this);
        mAppManager.routeSocket(ROUTE_CHAT);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        mUserId = sharedPreferences.getString(USER_ID, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_list, container, false);
        mChattingListListView = rootView.findViewById(R.id.chatting_list_view);
        mChatListAdapter = new ChatListAdapter();
        JSONObject chatListData = new JSONObject();
        try {
            chatListData.put(USER_ID, mUserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAppManager.emitRequestToServer(CHAT_LIST, chatListData);
        return rootView;
    }

//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onResponseReceived(String onEvent, Object[] object) {
        switch (onEvent) {
            case CHAT_LIST:
                break;
        }
    }
}
