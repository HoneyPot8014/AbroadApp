package com.example.leeyh.abroadapp.background;

public interface SocketListener {

    void socketRouting(String nameSpace);
    void socketEmitEvent(String event, String data);
    void socketOnEvent(String event);
}
