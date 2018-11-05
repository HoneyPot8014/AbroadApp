package com.example.leeyh.abroadapp.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.dataconverter.DataConverter;
import com.example.leeyh.abroadapp.service.GpsService;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.example.leeyh.abroadapp.constants.StaticString.MIN_DISTANCE_FOR_UPDATE;
import static com.example.leeyh.abroadapp.constants.StaticString.MIN_TIME_FOR_UPDATE;

public class LocationFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private GpsService mGpsService;
    private OnFragmentInteractionListener mListener;
    private TextView mMyLocationTextView;

    public LocationFragment() {
        // Required empty public constructor
    }

    public static LocationFragment newInstance(String param1, String param2) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_location, container, false);
        mMyLocationTextView = view.findViewById(R.id.my_location_text_view);
        getLocation(mMyLocationTextView);

//        mGpsService = new GpsService(getContext());
//        if (mGpsService.gpsIsEnabled()) {
//            double latitude = mGpsService.getLatitude();
//            double longitude = mGpsService.getLongitude();
//
//            Geocoder gCoder = new Geocoder(getContext(), Locale.getDefault());
//            List<Address> addresses;
//            try {
//                addresses = gCoder.getFromLocation(latitude, longitude, 1);
//                Address address = addresses.get(0);
//                for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
//                    mMyLocationTextView.setText(address.getAddressLine(i));
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }

        return view;
    }

    public void getLocation(final TextView textView) {

        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                MIN_TIME_FOR_UPDATE,
                MIN_DISTANCE_FOR_UPDATE,
                new LocationListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onLocationChanged(Location location) {
                        double longitude = location.getLongitude();
                        double latitude = location.getLatitude();
                        List<String> addressData = DataConverter.convertAddress(getContext(), latitude, longitude);
                        textView.setText(addressData.get(0) + " " + addressData.get(1));
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {

                    }
                });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
