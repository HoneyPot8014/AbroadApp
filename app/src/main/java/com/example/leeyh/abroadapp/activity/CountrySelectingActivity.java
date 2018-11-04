package com.example.leeyh.abroadapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.controller.CountrySelectingAdapter;


public class CountrySelectingActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_selecting);
        int[] data = {R.drawable.eiffel_tower,R.drawable.badshahi_mosque, R.drawable.christ_the_redeemer, R.drawable.statue_of_liberty, R.drawable.taj_mahal, R.drawable.torii_gate};
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager

        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new CountrySelectingAdapter(data);
        mRecyclerView.setAdapter(mAdapter);

    }



}
