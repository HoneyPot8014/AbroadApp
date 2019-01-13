package com.example.leeyh.abroadapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leeyh.abroadapp.databinding.NearLocationItemViewBinding;
import com.example.leeyh.abroadapp.model.UserModel;

import java.util.ArrayList;

public class LocationUserAdapter extends RecyclerView.Adapter<LocationUserAdapter.LocationUserViewHolder> {

    private ArrayList<UserModel> items = new ArrayList<>();
    private OnItemClickedListener<NearLocationItemViewBinding> mListener;

    static class LocationUserViewHolder extends RecyclerView.ViewHolder {

        private NearLocationItemViewBinding mBinding;
        private OnItemClickedListener<NearLocationItemViewBinding> mListener;

        LocationUserViewHolder(@NonNull NearLocationItemViewBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClicked(getAdapterPosition(), mBinding);
                }
            });
        }

        public void setListener(OnItemClickedListener listener) {
            this.mListener = listener;
        }
    }

    @NonNull
    @Override
    public LocationUserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        NearLocationItemViewBinding binding = NearLocationItemViewBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new LocationUserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationUserViewHolder locationUserViewHolder, int i) {
        locationUserViewHolder.setListener(mListener);
        final NearLocationItemViewBinding binding = locationUserViewHolder.mBinding;
        final UserModel user = items.get(i);
        binding.locationUserNameTextView.setText(user.getUserName());
        binding.locationUserAgeTextView.setText(user.getAge());
        binding.locationUserCountryTextView.setText(user.getCountry());
        binding.locationFragmentPlanTextView.setText(user.getPlan());
        binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(UserModel model) {
        items.add(model);
    }

    public void setListener(OnItemClickedListener listener) {
        mListener = listener;
    }

}
