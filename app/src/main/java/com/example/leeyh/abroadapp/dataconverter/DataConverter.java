package com.example.leeyh.abroadapp.dataconverter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
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

//    public static Bitmap rotateBitmap() {
//        ExifInterface ei = new ExifInterface();
//        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                ExifInterface.ORIENTATION_UNDEFINED);
//
//        Bitmap rotatedBitmap = null;
//        switch(orientation) {
//
//            case ExifInterface.ORIENTATION_ROTATE_90:
//                rotatedBitmap = rotateImage(bitmap, 90);
//                break;
//
//            case ExifInterface.ORIENTATION_ROTATE_180:
//                rotatedBitmap = rotateImage(bitmap, 180);
//                break;
//
//            case ExifInterface.ORIENTATION_ROTATE_270:
//                rotatedBitmap = rotateImage(bitmap, 270);
//                break;
//
//            case ExifInterface.ORIENTATION_NORMAL:
//            default:
//                rotatedBitmap = bitmap;
//        }
//    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

}
