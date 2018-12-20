package com.example.leeyh.abroadapp.controller;

/**
 * Created by bum on 2018. 12. 6..
 */

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.mypage.MyData;

import org.json.JSONArray;


import java.util.ArrayList;


public class TravelPlanAdapter extends RecyclerView.Adapter<TravelPlanAdapter.PlanListViewHolder> {
    private ArrayList<MyData> mDataset;
    private JSONArray mItems = new JSONArray();
    private Context mContext;
    private RecyclerItemClickListener listener;

    static class PlanListViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
       /* private ImageView mImageView;
        private TextView nameTextView;
        private TextView descriptionTextView;
        private ImageView onButton;*/
        public ImageView mImageView;
        public TextView mtxtdateTextView;
        public TextView mtxtcountryName;
        public TextView mtxtbudgetTextView;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private PlanListViewHolder(View view) {
            /*super(v);
            mImageView = v.findViewById(R.id.countryImage);
            mImageView.setClipToOutline(true);
            nameTextView = v.findViewById(R.id.countryName);
            descriptionTextView = v.findViewById(R.id.planText);
            //onButton = v.findViewById(R.id.onButtonImg);*/
            super(view);
            mImageView = (ImageView)view.findViewById(R.id.countryImage);
            mtxtdateTextView = (TextView)view.findViewById(R.id.dateTextView);
            mtxtcountryName = (TextView)view.findViewById(R.id.countryName);
            mtxtbudgetTextView = (TextView)view.findViewById(R.id.budgetTextView);
        }
    }
    public TravelPlanAdapter(ArrayList<MyData> myDataset){
        mDataset = myDataset;
    }
    /*public TravelPlanAdapter(RecyclerItemClickListener listener) {
        this.listener = listener;
    }*/

    // Create new views (invoked by the layout manager)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public TravelPlanAdapter.PlanListViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        // create a new view
       /* LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.travel_list, parent, false);
        mContext = parent.getContext();
        return new PlanListViewHolder(view);*/
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_view, parent, false);

        TravelPlanAdapter.PlanListViewHolder vh = new TravelPlanAdapter.PlanListViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(PlanListViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.nameTextView.setText("japan");
        holder.mtxtdateTextView.setText(mDataset.get(position).date);
        holder.mtxtcountryName.setText(mDataset.get(position).country);
        holder.mtxtbudgetTextView.setText(mDataset.get(position).budget);
        holder.mImageView.setImageBitmap(mDataset.get(position).img);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return  mDataset.size();
    }

    public void addList(JSONArray list) {
        this.mItems = list;
    }

    public void deleteAllItem() {
        this.mItems = null;
    }
}

