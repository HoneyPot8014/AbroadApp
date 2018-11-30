package com.example.leeyh.abroadapp.background;

import org.json.JSONArray;
import org.json.JSONObject;

public interface SocketRequestListener {

    void socketRouting(String nameSpace);
    void socketEmitEvent(String event, JSONObject data);
    void socketEmitEvent(String event, JSONArray data);
    void socketOnEvent(String event);
    void closeSocket();
}
