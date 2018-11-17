package com.example.leeyh.abroadapp.dataconverter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.util.Base64;
import android.widget.ImageView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataConverter {

    public static byte[] getByteArrayToStringFromBitmap(Bitmap bitmap) {
        BitmapFactory.Options bitmapOption = new BitmapFactory.Options();
        bitmapOption.inSampleSize = 5;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return outputStream.toByteArray();
    }

    public static String convertAddress(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context);
//        List<String> resultList = new ArrayList<>();
        List<Address> addressList = null;
        try {
            addressList = geocoder.getFromLocation(latitude, longitude, 1);
            if(addressList != null) {
                if(addressList.size() == 0) {
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        resultList.add(addressList.get(0).getAdminArea());
//        resultList.add(addressList.get(0).getCountryName());
        return addressList.get(0).getAdminArea();
    }

}
