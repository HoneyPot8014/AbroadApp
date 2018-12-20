package com.example.leeyh.abroadapp.mypage;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leeyh.abroadapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class TempActivity extends AppCompatActivity {

    public static Context mContext;
    private static int PICK_IMAGE_REQUEST = 2;

    List<String> country;
    String[] countryArray;
    String leftMoney;

    int mYear, mMonth, mDay;
    Button btn_intent, btn_startDate, btn_endDate;
    ImageView travelImage;
    TextView mTxt_startDate, mTxt_endDate, mChangeImage, mTxt_country;

    EditText mCountry;
    EditText mTotalmoney, mtravelTheme;

    Calendar calendar;

    ArrayAdapter<String> adapter;
    Uri uri;
    Bitmap scaled;
    int[] selectedItem = {0};
    Intent intent2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        Intent intent=new Intent(this.getIntent());
        String s=intent.getStringExtra("country");

//        mCountry = (EditText)findViewById(R.id.et_country);
        mTotalmoney = (EditText)findViewById(R.id.et_totalMoney);
        mtravelTheme = (EditText)findViewById(R.id.travelTheme);
//        mCountry.setOnClickListener(mChoiceCountryListener);

        btn_intent = (Button)findViewById(R.id.btn_intent);

        btn_intent.setOnClickListener(moveCalendar);



        mTxt_startDate = (TextView)findViewById(R.id.startDate);
        mTxt_endDate = (TextView)findViewById(R.id.endDate);
        mChangeImage = (TextView)findViewById(R.id.changeImage);
        mTxt_country = (TextView)findViewById(R.id.txt_country);

        mTxt_country.setText(s);

        mChangeImage.setOnClickListener(loadImagefromGallery);

        mTxt_startDate.setOnClickListener(mOnStartDateClick);
        mTxt_endDate.setOnClickListener(mOnEndDateClick);

        travelImage = (ImageView)findViewById(R.id.travelimage);

        Calendar cal = new GregorianCalendar();

        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        mContext = this;

        country = new ArrayList<String>();
        countryArray = Locale.getISOCountries();
        for( int i=0; i < countryArray.length; i++ ){
            Locale locale = new Locale( "en", countryArray[i] );
            country.add(locale.getDisplayCountry());
        }

        country.toArray(countryArray);
        Arrays.sort(countryArray);
    }

    Button.OnClickListener loadImagefromGallery = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //ACTION_PIC과 차이점?
            intent.setType("image/*"); //이미지만 보이게
            //Intent 시작 - 갤러리앱을 열어서 원하는 이미지를 선택할 수 있다.
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        try {
            //이미지를 하나 골랐을때
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data) {
                //data에서 절대경로로 이미지를 가져옴
                uri = data.getData();
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 30;
                String path = geturiPath(uri);
                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                ExifInterface exif = new ExifInterface(geturiPath(uri));
                int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                bitmap = rotateBitmap(bitmap, exifOrientation);
                //이미지가 한계이상(?) 크면 불러 오지 못하므로 사이즈를 줄여 준다.
                int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);
                travelImage.setImageBitmap(scaled);
                return ;

            }
            else {
                Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Oops! 로딩에 오류가 있습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String geturiPath(Uri contentUri) {

        if(contentUri.getPath().startsWith("/storage")){
            return contentUri.getPath();
        }

        String id = DocumentsContract.getDocumentId(contentUri).split(":")[1];
        String[] columns = {MediaStore.Files.FileColumns.DATA};
        String selection = MediaStore.Files.FileColumns._ID+" = " + id;
        Cursor cursor = getContentResolver().query(MediaStore.Files.getContentUri("external"), columns, selection, null, null);
        try{
            int columnIndex = cursor.getColumnIndex(columns[0]);
            if(cursor.moveToFirst()){
                return cursor.getString(columnIndex);
            }
        }finally {
            cursor.close();
        }
        return null;
    }

    Button.OnClickListener mOnStartDateClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            calendar = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(TempActivity.this, mStartDateSetlistener, mYear, mMonth, mDay);
            dialog.show();
        }
    };

    Button.OnClickListener mOnEndDateClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog dialog = new DatePickerDialog(TempActivity.this, mEndDateSetlistener, mYear, mMonth, mDay);
            dialog.show();
        }
    };

    private DatePickerDialog.OnDateSetListener mStartDateSetlistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            //Toast.makeText(getApplicationContext(), year + "년" + month + "월" + day + "일", Toast.LENGTH_SHORT).show();
            mYear = year;
            mMonth = month;
            mDay = day;
            mTxt_startDate.setText(String.format("%d/%d/%d",mYear,mMonth+1, mDay));
        }
    };

    private DatePickerDialog.OnDateSetListener mEndDateSetlistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            //Toast.makeText(getApplicationContext(), year + "년" + month + "월" + day + "일", Toast.LENGTH_SHORT).show();
            mYear = year;
            mMonth = month;
            mDay = day;
            mTxt_endDate.setText(String.format("%d/%d/%d",mYear,mMonth+1, mDay));
        }
    };

    Button.OnClickListener moveCalendar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(TempActivity.this, NextActivity.class);
            intent2 = new Intent(TempActivity.this, ChoiceCountryActivity.class);

            intent.putExtra("budget", mTotalmoney.getText().toString());
            intent.putExtra("countryname", mTxt_country.getText().toString());
            intent.putExtra("theme", mtravelTheme.getText().toString());

            intent2.putExtra("countryname",mTxt_country.getText().toString());
            intent2.putExtra("startdate", mTxt_startDate.getText().toString());
            intent2.putExtra("enddate", mTxt_endDate.getText().toString());
            System.out.println("tempactivity uri : " + uri);
            intent2.putExtra("img", uri);
            setResult(RESULT_OK, intent2);

            startActivityForResult(intent, 1);
            finish();
        }
    };

    public CharSequence getmTxt_StartDate(){
        return mTxt_startDate.getText();
    }

    public CharSequence getmTxt_EndDate(){
        return mTxt_endDate.getText();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public Bitmap rotateBitmap(Bitmap bitmap, int orientation){
        Matrix matrix = new Matrix();
        switch (orientation){
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try{
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch(OutOfMemoryError e){
            e.printStackTrace();
            return null;
        }
    }
}
