package com.example.leeyh.abroadapp.controller;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.leeyh.abroadapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.leeyh.abroadapp.constants.StaticString.USER_EMAIL;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_NAME;


public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.MemberListViewHolder> {
    private JSONArray mItems = new JSONArray();
    private Context mContext;
    private RecyclerItemClickListener listener;

    static class MemberListViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private ImageView mImageView;
        private TextView nameTextView;
        private TextView descriptionTextView;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private MemberListViewHolder(View v) {
            super(v);
            mImageView = v.findViewById(R.id.memberImage);
            mImageView.setClipToOutline(true);
            nameTextView = v.findViewById(R.id.member_name_text_view);
            descriptionTextView = v.findViewById(R.id.member_discription_text_view);
        }
    }

    public MemberListAdapter(RecyclerItemClickListener listener) {
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public MemberListViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.member_list_view, parent, false);
        mContext = parent.getContext();
        return new MemberListViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MemberListViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        try {
            final JSONObject item = mItems.getJSONObject(position);
            holder.nameTextView.setText(item.getString(USER_EMAIL));
            Glide.with(mContext).load("http://49.236.137.55/profile?id=" + item.getString(USER_NAME) + ".jpeg")
                    .into(holder.mImageView);
//            holder.descriptionTextView.setText(DataConverter.convertAddress(mContext
//                    , Double.parseDouble(item.getString(LATITUDE)), Double.parseDouble(item.getString(LONGITUDE))));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClicked(view, position, item);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mItems.length();
}

    public void addList(JSONArray list) {
        this.mItems = list;
    }

    public void deleteAllItem() {
        this.mItems = null;
    }
}

