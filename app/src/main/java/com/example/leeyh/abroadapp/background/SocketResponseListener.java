package com.example.leeyh.abroadapp.background;

public interface SocketResponseListener {
    void onResponseFromServer(String onEvent, Object... args);
}
