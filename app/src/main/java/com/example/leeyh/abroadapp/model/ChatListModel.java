package com.example.leeyh.abroadapp.model;

public class ChatListModel {

    private String roomName;
    private String roomNickName;
    private String joinMember;
    private String lastMessage;
    private String mainUserId;

    public ChatListModel(String roomName, String mainUserId, String joinMember, String lastMessage) {
        this.roomName = roomName;
        this.mainUserId = mainUserId;
        this.joinMember = joinMember;
        this.lastMessage = lastMessage;
    }

//    public ChatListModel(String roomName, String roomNickName, String joinMember, String lastMessage) {
//        this.roomName = roomName;
//        this.roomNickName = roomNickName;
//        this.joinMember = joinMember;
//        this.lastMessage = lastMessage;
//    }

    public String getMainUserId() {
        return mainUserId;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getRoomNickName() {
        return roomNickName;
    }

    public String getJoinMember() {
        return joinMember;
    }

    public String getLastMessage() {
        return lastMessage;
    }
}
