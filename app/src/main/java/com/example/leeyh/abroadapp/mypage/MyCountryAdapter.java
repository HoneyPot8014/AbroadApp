package com.example.leeyh.abroadapp.mypage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leeyh.abroadapp.R;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MyCountryAdapter extends RecyclerView.Adapter<MyCountryAdapter.ViewHolder> {

    private ArrayList<MyData> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mtxtdateTextView;
        public TextView mtxtcountryName;
        public TextView mtxtbudgetTextView;

        public ViewHolder(View view){
            super(view);
            mImageView = (ImageView)view.findViewById(R.id.countryImage);
            mtxtdateTextView = (TextView)view.findViewById(R.id.dateTextView);
            mtxtcountryName = (TextView)view.findViewById(R.id.countryName);
            mtxtbudgetTextView = (TextView)view.findViewById(R.id.budgetTextView);
        }
    }

    public MyCountryAdapter(ArrayList<MyData> myDataset){
        mDataset = myDataset;
    }

    @Override
    public MyCountryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_view, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mtxtdateTextView.setText(mDataset.get(position).date);
        holder.mtxtcountryName.setText(mDataset.get(position).country);
        holder.mtxtbudgetTextView.setText(mDataset.get(position).budget);
        holder.mImageView.setImageBitmap(mDataset.get(position).img);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}