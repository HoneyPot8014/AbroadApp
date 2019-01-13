package com.example.leeyh.abroadapp.adapters;

import android.support.v7.util.DiffUtil;

import com.example.leeyh.abroadapp.model.UserModel;

import java.util.ArrayList;

public class ListDiffUtil extends DiffUtil.Callback {

    private ArrayList<UserModel> mOldList;
    private ArrayList<UserModel> mNewList;

    public ListDiffUtil(ArrayList<UserModel> oldMovieList, ArrayList<UserModel> newMovieList) {
        this.mOldList = oldMovieList;
        this.mNewList = newMovieList;
    }

    @Override
    public int getOldListSize() {
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int i, int i1) {
        return mOldList.get(i).getUid().equals(mNewList.get(i1).getUid());
    }

    @Override
    public boolean areContentsTheSame(int i, int i1) {
        return mOldList.get(i).equals(mOldList.get(i1));
    }
}
