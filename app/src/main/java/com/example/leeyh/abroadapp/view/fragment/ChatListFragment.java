package com.example.leeyh.abroadapp.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.background.OnResponseReceivedListener;
import com.example.leeyh.abroadapp.controller.ChatListAdapter;
import com.example.leeyh.abroadapp.helper.ApplicationManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.leeyh.abroadapp.constants.SocketEvent.CHAT_LIST;
import static com.example.leeyh.abroadapp.constants.SocketEvent.CHAT_LIST_SUCCESS;
import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTE_CHAT;
import static com.example.leeyh.abroadapp.constants.StaticString.ROOM_NAME;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_INFO;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_UUID;

public class ChatListFragment extends Fragment implements OnResponseReceivedListener {

    private ChatListAdapter mChatListAdapter;
    private ApplicationManagement mAppManager;
    private SharedPreferences mSharedPreferences;
    private OnChatListItemClicked mChatListListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            String roomName = getArguments().getString(ROOM_NAME);
            mChatListListener.onChatNewChatMessage(roomName);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAppManager = (ApplicationManagement) getContext().getApplicationContext();
        mAppManager.setOnResponseReceivedListener(this);
        mAppManager.routeSocket(ROUTE_CHAT);
        mSharedPreferences = getContext().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        View rootView = inflater.inflate(R.layout.fragment_chat_list, container, false);
        getChattingList();
        mAppManager.onResponseFromServer(CHAT_LIST_SUCCESS);
        mChatListAdapter = new ChatListAdapter();
        ListView chattingListListView = rootView.findViewById(R.id.chatting_list_view);
        chattingListListView.setAdapter(mChatListAdapter);
        chattingListListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                JSONObject chatListModel = (JSONObject) mChatListAdapter.getItem(i);
                mChatListListener.onChatItemClicked(chatListModel);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("test", "onStart: ");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChatListItemClicked) {
            mChatListListener = (OnChatListItemClicked) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChatListItemClicked");
        }
    }

    @Override
    public void onResponseReceived(String onEvent, Object[] args) {
        switch (onEvent) {
            case CHAT_LIST_SUCCESS:
                JSONArray chatListArray = (JSONArray) args[0];
                mChatListAdapter.addList(chatListArray);
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        mChatListAdapter.notifyDataSetChanged();
                    }
                }.sendEmptyMessage(0);
                break;
        }
    }

    public static ChatListFragment newChattingFragmentInstance(String roomName) {
        ChatListFragment fragment = new ChatListFragment();
        Bundle args = new Bundle();
        args.putString(ROOM_NAME, roomName);
        fragment.setArguments(args);
        return fragment;
    }

    public void getChattingList() {
        JSONObject chatListData = new JSONObject();
        try {
            chatListData.put(USER_UUID, mSharedPreferences.getString(USER_UUID, null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAppManager.emitRequestToServer(CHAT_LIST, chatListData);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
