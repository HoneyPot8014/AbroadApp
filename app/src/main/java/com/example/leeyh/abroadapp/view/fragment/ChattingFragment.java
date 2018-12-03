package com.example.leeyh.abroadapp.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.background.OnResponseReceivedListener;
import com.example.leeyh.abroadapp.controller.ChatMessageAdapter;
import com.example.leeyh.abroadapp.helper.ApplicationManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;
import static com.example.leeyh.abroadapp.constants.SocketEvent.CHAT_MESSAGE;
import static com.example.leeyh.abroadapp.constants.SocketEvent.CHAT_MESSAGE_SUCCESS;
import static com.example.leeyh.abroadapp.constants.SocketEvent.RECEIVE_MESSAGE;
import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTE_CHAT;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SEND_MESSAGE;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SEND_MESSAGE_FAILED;
import static com.example.leeyh.abroadapp.constants.SocketEvent.SEND_MESSAGE_SUCCESS;
import static com.example.leeyh.abroadapp.constants.StaticString.MESSAGE;
import static com.example.leeyh.abroadapp.constants.StaticString.ROOM_NAME;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_NAME;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_INFO;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_UUID;

public class ChattingFragment extends Fragment implements OnResponseReceivedListener {

    private String mRoomName;
    private OnFragmentInteractionListener mListener;
    private ApplicationManagement mAppManager;
    private ListView mChatMessageListView;
    private ChatMessageAdapter mChatMessageAdaptor;
    private EditText mChatMessageEditText;
    private SharedPreferences mSharedPreferences;

    public static ChattingFragment newChattingFragmentInstance(String param1) {
        ChattingFragment fragment = new ChattingFragment();
        Bundle args = new Bundle();
        args.putString(ROOM_NAME, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRoomName = getArguments().getString(ROOM_NAME);
        }
        mAppManager = (ApplicationManagement) getContext().getApplicationContext();
        mAppManager.setOnResponseReceivedListener(this);
        mAppManager.routeSocket(ROUTE_CHAT);
        mSharedPreferences = getContext().getSharedPreferences(USER_INFO, MODE_PRIVATE);
        mChatMessageAdaptor = new ChatMessageAdapter();
        getChattingMessage();
        mAppManager.onResponseFromServer(CHAT_MESSAGE_SUCCESS);
        mAppManager.onResponseFromServer(SEND_MESSAGE_SUCCESS);
        mAppManager.onResponseFromServer(SEND_MESSAGE_FAILED);
        mAppManager.onResponseFromServer(RECEIVE_MESSAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatting, container, false);
        mChatMessageListView = view.findViewById(R.id.room_chat_message_list_view);
        mChatMessageListView.setAdapter(mChatMessageAdaptor);
        mChatMessageEditText = view.findViewById(R.id.room_chat_message_edit_text);
        Button sendMessageButton = view.findViewById(R.id.room_chat_send_button);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResponseReceived(String onEvent, Object[] object) {
        switch (onEvent) {
            case CHAT_MESSAGE_SUCCESS:
                JSONArray chatMessageList = (JSONArray) object[0];
                mChatMessageAdaptor.addList(chatMessageList);
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        mChatMessageAdaptor.notifyDataSetChanged();
                        mChatMessageListView.setSelection(mChatMessageAdaptor.getCount() - 1);
                    }
                }.sendEmptyMessage(0);
                break;

            case RECEIVE_MESSAGE:
                JSONObject receivedMessage = (JSONObject) object[0];
                mChatMessageAdaptor.addItem(receivedMessage);
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        mChatMessageAdaptor.notifyDataSetChanged();
                        mChatMessageListView.setSelection(mChatMessageAdaptor.getCount() - 1);
                    }
                }.sendEmptyMessage(0);
                break;

            case SEND_MESSAGE_SUCCESS:
                break;

            case SEND_MESSAGE_FAILED:
                break;
        }

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void getChattingMessage() {
        JSONObject chatMessageData = new JSONObject();
        try {
            chatMessageData.put(ROOM_NAME, mRoomName);
            mAppManager.emitRequestToServer(CHAT_MESSAGE, chatMessageData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage() {
        JSONObject sendMessageData = new JSONObject();
        try {
            sendMessageData.put(USER_NAME, mSharedPreferences.getString(USER_NAME, null));
            sendMessageData.put(USER_UUID, mSharedPreferences.getString(USER_UUID, null));
            sendMessageData.put(ROOM_NAME, mRoomName);
            sendMessageData.put(MESSAGE, mChatMessageEditText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAppManager.emitRequestToServer(SEND_MESSAGE, sendMessageData);
        mChatMessageEditText.setText("");
    }
}
