package com.example.leeyh.abroadapp.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.background.OnResponseReceivedListener;
import com.example.leeyh.abroadapp.background.ServiceEventInterface;
import com.example.leeyh.abroadapp.controller.ChatListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.leeyh.abroadapp.constants.SocketEvent.CHAT_LIST;
import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTE_CHAT;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_ID;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_INFO;

public class ChatListFragment extends Fragment implements OnResponseReceivedListener {

    private ListView mChattingListListView;
    private ChatListAdapter mChatListAdapter;
    private ServiceEventInterface mSocketListener;
    private String mUserId;
    private OnFragmentInteractionListener mListener;

    public ChatListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSocketListener = new ServiceEventInterface(getContext());
        mSocketListener.socketRouting(ROUTE_CHAT);
        mSocketListener.setResponseListener(this);
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
        mSocketListener.socketEmitEvent(CHAT_LIST, chatListData.toString());
        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResponseReceived(Intent intent) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
