package com.example.leeyh.abroadapp.constants;

public class SocketEvent {

    public static final String ROUTING = "routing";
    public static final String SQL_ERROR = "sqlError";

    //DEFAULT HOST
    public static final String DEFAULT_HOST = "http://49.236.137.55";
    public static final String CHECK_SIGNED = "checkSignedUser";
    public static final String SIGNED_USER = "signedUser";
    public static final String UN_SIGNED_USER = "unSignedUser";
    public static final String DISCONNECT = "disconnect";

    //ROUTE_SIGN_IN
    public static final String ROUTE_SIGN_IN = "/signIn";
    public static final String SIGN_IN = "signInRequest";
    public static final String SIGN_IN_SUCCESS = "signInSuccess";
    public static final String SIGN_IN_FAILED = "signInFailed";

    //ROUTE_SIGN_UP
    public static final String ROUTE_SIGN_UP = "/signUp";
    public static final String SIGN_UP = "signUpRequest";
    public static final String SIGN_UP_SUCCESS = "signUpSuccess";
    public static final String SIGN_UP_FAILED = "signUpFailed";

    //ROUTE_CHAT
    public static final String ROUTE_CHAT = "/chat";
    public static final String CHAT_CONNECT = "chatConnected";
    public static final String CHAT_CONNECT_SUCCESS = "chatConnectedSuccess";
    public static final String CHAT_CONNECT_FAILED = "chatConnectedFailed";
    public static final String MAKE_CHAT_ROOM = "makeChatRoom";
    public static final String MAKE_CHAT_ROOM_SUCCESS = "makeRoomSuccess";
    public static final String MAKE_CHAT_ROOM_FAILED = "makeRoomFailed";
    public static final String NEW_ROOM_CHAT = "newRoomChat";
    public static final String CHAT_LIST = "chatList";
    public static final String CHAT_LIST_SUCCESS = "chatListSuccess";
    public static final String CHAT_LIST_FAILED = "chatListFailed";
    public static final String CHAT_MESSAGE = "chatMessage";
    public static final String CHAT_MESSAGE_SUCCESS = "chatMessageSuccess";
    public static final String SEND_MESSAGE = "sendMessage";
    public static final String SEND_MESSAGE_SUCCESS = "sendMessageSuccess";
    public static final String SEND_MESSAGE_FAILED = "sendMessageFailed";
    public static final String RECEIVE_MESSAGE = "receiveMessage";

    //ROUTE_MAP
    public static final String ROUTE_MAP = "/chat/map";
    public static final String SAVE_LOCATION = "saveLocation";
    public static final String SAVE_LOCATION_SUCCESS = "saveLocationSuccess";
    public static final String NO_NEAR_USER = "noNearUser";
}
