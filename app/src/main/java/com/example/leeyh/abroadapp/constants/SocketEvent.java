package com.example.leeyh.abroadapp.constants;

public class SocketEvent {

    public static final String ROUTING = "routing";
    public static final String SQL_ERROR = "sqlError";

    //DEFAULT HOST
    public static final String DEFAULT_HOST = "http://49.236.137.55";
    public static final String CHECK_SIGNED = "checkSignedUser";
    public static final String USER_DATA = "userData";
    public static final String SIGNED_USER = "signedUser";

    //ROUTE_SIGN_UP
    public static final String ROUTE_SIGN_UP = "/signUp";
    public static final String SIGN_UP = "signUpRequest";
    public static final String SIGN_UP_SUCCESS = "signUpSuccess";
    public static final String SIGN_UP_FAILED = "signUpFailed";

    //ROUTE_SIGN_IN ROUTE
    public static final String ROUTE_SIGN_IN = "/singIn";
    public static final String SIGN_IN = "signInRequest";
    public static final String SIGN_IN_SUCCESS = "signInSuccess";
    public static final String SIGN_IN_FAILED = "signInFailed";

    //CHAT
    public static final String CHAT = "/chat";
    public static final String CHAT_CONNECT = "chatConnected";
    public static final String CHAT_CONNECT_FAILED = "chatConnectedFailed";

    //MAP
    public static final String ROUTE_MAP = "/chat/map";
    public static final String USER_LOCATION = "userLocation";
    public static final String SAVE_LOCATION = "saveLocation";
    public static final String SAVE_LOCATION_SUCCESS = "saveLocationSuccess";
}
