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
import com.example.leeyh.abroadapp.model.ChatListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.leeyh.abroadapp.constants.SocketEvent.CHAT_LIST;
import static com.example.leeyh.abroadapp.constants.SocketEvent.CHAT_LIST_SUCCESS;
import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTE_CHAT;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_INFO;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_UUID;

public class ChatListFragment extends Fragment implements OnResponseReceivedListener {

    private ListView mChattingListListView;
    private ChatListAdapter mChatListAdapter;
    private ApplicationManagement mAppManager;
    private SharedPreferences mSharedPreferences;
    private OnChatListItemClicked mChatListItemListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppManager = (ApplicationManagement) getContext().getApplicationContext();
        mAppManager.setOnResponseReceivedListener(this);
        mAppManager.routeSocket(ROUTE_CHAT);
        mSharedPreferences = getContext().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_list, container, false);
        getChattingList();
        mAppManager.onResponseFromServer(CHAT_LIST_SUCCESS);
        mChatListAdapter = new ChatListAdapter();
        mChattingListListView = rootView.findViewById(R.id.chatting_list_view);
        mChattingListListView.setAdapter(mChatListAdapter);
        mChattingListListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                JSONObject chatListModel = (JSONObject) mChatListAdapter.getItem(i);
                mChatListItemListener.onChatItemClicked(chatListModel);
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChatListItemClicked) {
            mChatListItemListener = (OnChatListItemClicked) context;
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
                Log.d("채팅", "onResponseReceived: " + chatListArray);
                mChatListAdapter.addList(chatListArray);
//                for(int i = 0; i < chatListArray.length(); i++) {
//                    try {
//                        JSONObject chatList = (JSONObject) chatListArray.get(i);
//                        mChatListAdapter.addItem(chatList);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
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
