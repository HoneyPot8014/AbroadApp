package com.example.leeyh.abroadapp.dataconverter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
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
            if (addressList != null) {
                if (addressList.size() == 0) {
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

    public static void modifyOrientation(Uri imgUri, ImageView imageView, Context context) throws IOException {
        String imagePath = getRealPathFromURI(imgUri, context); // path 경로
        Log.d("절대경로", "modifyOrientation: " + imagePath);
        ExifInterface exif = new ExifInterface(imagePath);
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
        imageView.setImageBitmap(rotate(bitmap, exifDegree));//이미지 뷰에 비트맵 넣기
    }

    private static Bitmap rotate(Bitmap src, float degree) {

        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }

    private static int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private static String getRealPathFromURI(Uri contentUri, Context context) {
        int column_index = 0;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor.moveToFirst()) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }
        return cursor.getString(column_index);
    }

}
