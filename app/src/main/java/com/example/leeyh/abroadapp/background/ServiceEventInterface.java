package com.example.leeyh.abroadapp.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTING;
import static com.example.leeyh.abroadapp.constants.StaticString.BROADCAST;
import static com.example.leeyh.abroadapp.constants.StaticString.EMIT_EVENT;
import static com.example.leeyh.abroadapp.constants.StaticString.JSON_DATA;
import static com.example.leeyh.abroadapp.constants.StaticString.ON_EVENT;

public class ServiceEventInterface implements SocketListener {

    private Context mContext;
    private OnResponseReceivedListener mResponseListener;

    public ServiceEventInterface(Context context) {
        this.mContext = context;
        onReceivedResponse();
//        onSocketRouted();
    }

    @Override
    public void socketRouting(String nameSpace) {
        Intent service = new Intent(mContext, BackgroundService.class);
        service.putExtra(ROUTING, nameSpace);
        mContext.startService(service);
    }

    @Override
    public void socketEmitEvent(String event, String data) {
        Intent service = new Intent(mContext, BackgroundService.class);
        service.putExtra(EMIT_EVENT, event);
        service.putExtra(JSON_DATA, data);
        mContext.startService(service);
    }

    public void onReceivedResponse() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST);
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mResponseListener.onResponseReceived(intent);
            }
        };
        LocalBroadcastManager.getInstance(mContext).registerReceiver(broadcastReceiver, intentFilter);
    }

    public void setResponseListener(OnResponseReceivedListener listener) {
        this.mResponseListener = listener;
    }

    @Override
    public void socketOnEvent(String onEvent) {
        Intent service = new Intent(mContext, BackgroundService.class);
        service.putExtra(ON_EVENT, onEvent);
        mContext.startService(service);
    }
}
