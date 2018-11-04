package com.example.leeyh.abroadapp.model;

public class ChatModel {

    private String userNickName;
    private String message;
    private boolean isMyMessage;

    public ChatModel(String userNickName, String message, boolean isMyMessage) {
        this.userNickName = userNickName;
        this.message = message;
        this.isMyMessage = isMyMessage;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isMyMessage() {
        return isMyMessage;
    }

    public void setMyMessage(boolean myMessage) {
        isMyMessage = myMessage;
    }
}
