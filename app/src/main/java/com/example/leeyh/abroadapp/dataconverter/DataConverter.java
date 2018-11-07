package com.example.leeyh.abroadapp.dataconverter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataConverter {

    public static byte[] getByteArrayFromImageView(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] imageByteArray = outputStream.toByteArray();
        return imageByteArray;
    }

    public static byte[] getByteArrayToStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    public static byte[] getByteArrayFromString(String byteArrayToString) {
        return Base64.decode(byteArrayToString, Base64.DEFAULT);
    }

    public static List<String> convertAddress(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context);
        List<String> resultList = new ArrayList<>();
        List<Address> addressList = null;
        try {
            addressList = geocoder.getFromLocation(latitude, longitude, 10);
            if(addressList != null) {
                if(addressList.size() == 0) {
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultList.add(addressList.get(0).getAddressLine(0));
        resultList.add(addressList.get(0).getAdminArea());
        resultList.add(addressList.get(0).getCountryName());
        return resultList;
    }

}
