package com.example.leeyh.abroadapp.controller;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.leeyh.abroadapp.R;


public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.MemberListViewHolder> {
    private int[] mDataset;
    public static class MemberListViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView mImageView;
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public MemberListViewHolder(View v) {
            super(v);

            mImageView = (ImageView) v.findViewById(R.id.memberImage);
            mImageView.setClipToOutline(true);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MemberListAdapter(int[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public MemberListViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.member_list_view, parent, false);
        return new MemberListViewHolder(view);


    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MemberListViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mImageView.setImageResource(mDataset[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}

