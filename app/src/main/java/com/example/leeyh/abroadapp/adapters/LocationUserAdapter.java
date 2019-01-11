package com.example.leeyh.abroadapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.leeyh.abroadapp.databinding.NearLocationItemViewBinding;
import com.example.leeyh.abroadapp.model.UserModel;

import java.util.ArrayList;

public class LocationUserAdapter extends RecyclerView.Adapter<LocationUserAdapter.LocationUserViewHolder> {

    private ArrayList<UserModel> items = new ArrayList<>();

    static class LocationUserViewHolder extends RecyclerView.ViewHolder {

        private NearLocationItemViewBinding mBinding;

        LocationUserViewHolder(@NonNull NearLocationItemViewBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
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
        NearLocationItemViewBinding binding = locationUserViewHolder.mBinding;
        UserModel user = items.get(i);
        binding.locationUserNameTextView.setText(user.getUserName());
        binding.locationUserAgeTextView.setText(user.getAge());
        binding.locationUserCountryTextView.setText(user.getCountry());
        binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(UserModel model) {
        items.add(model);
    }

}
