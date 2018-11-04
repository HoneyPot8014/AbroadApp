package com.example.leeyh.abroadapp.controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.leeyh.abroadapp.R;

/**
 * Created by bum on 2018. 10. 2..
 */

public class CountrySelectingAdapter extends RecyclerView.Adapter<CountrySelectingAdapter.CountrySelectingViewHolder>{
    private int[] mDataset;
    public static class CountrySelectingViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView mImageView;
        public CountrySelectingViewHolder(View v) {
            super(v);

            mImageView = (ImageView) v.findViewById(R.id.my_image_view);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CountrySelectingAdapter(int[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CountrySelectingAdapter.CountrySelectingViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.country_selecting_cardview, parent, false);
        return new CountrySelectingAdapter.CountrySelectingViewHolder(view);


    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CountrySelectingViewHolder holder, int position) {
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

