package com.example.leeyh.abroadapp.prototype;

public class Constants {

    //URL
    public static final String REQUEST_URL = "http://49.236.137.55";
    public static final String LOGIN_ROUTE = "/login";
    public static final String CHAT_ROUTE = LOGIN_ROUTE + "/chat";

    //SIGN UP
    public static final String SIGN_UP = "signUpRequest";
    public static final String SIGN_UP_SUCCESS = "signUpSuccess";
    public static final String SIGN_UP_FAILED = "signUpFailed";
    public static final String CHECK_USER = "checkUser";
    public static final String RESPONSE_CHECK_USER = "responseCheckUser";

    //SIGN IN
    public static final String SIGN_IN = "loginRequest";
    public static final String SIGN_IN_SUCCESS = "loginSuccess";
    public static final String SIGN_IN_FAILED = "loginFailed";
    public static final String INCORRECT_ID = "idNotFoundException";

    //CHAT_LIST
    public static final String REQUEST_JOIN = "requestJoin";
    public static final String GET_CHAT_LIST = "getChatList";
    public static final String SET_CHAT_LIST = "setChatList";
    public static final String INVITED_JOIN = "invitedJoin";
    public static final String JOIN_SUCCESS = "joinSuccess";

    //ROOM_CHAT
    public static final String SEND_CHAT = "sendMessage";
    public static final String REQUEST_STORED_CHAT = "storedMessage";
    public static final String GET_STORED_CHAT = "responseStoredMessage";
    public static final String RECEIVE_CHAT = "receiveMessage";
}
