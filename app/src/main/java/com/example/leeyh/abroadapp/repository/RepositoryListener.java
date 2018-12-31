package com.example.leeyh.abroadapp.repository;

public interface RepositoryListener {

    void onTaskStarted();
    void onTaskFinished(String status);
}
