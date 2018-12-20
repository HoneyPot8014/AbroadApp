package com.example.leeyh.abroadapp.mypage;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leeyh.abroadapp.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    Bitmap img;

    int mYear, mMonth, mDay;
    String moneyType;
    String chk;
    String change;

    TextView mTxt_Date;

    Button mBtn_imageChoice;
    Button mBtn_Forsleep, mBtn_Foreat, mBtn_Fortransport, mBtn_Foretc, mBtn_Forshopping, mBtn_Forsight;
    Button mBtn_insertTitle;

    ImageView imageView;
    ImageView img_Forsleep, img_Foreat, img_Fortransport, img_Foretc, img_Forshopping, img_Forsight;

    EditText etMoney, etDetailedTitle;
    private static int PICK_IMAGE_REQUEST = 1;
    Uri uri;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);




        Calendar cal = new GregorianCalendar();
//        mTxt_Date = (TextView)findViewById(R.id.insert_date);

//        mBtn_choiceDate = (Button)findViewById(R.id.choiceDate);
        mBtn_imageChoice = (Button)findViewById(R.id.btnImagechoice);

        mBtn_Foreat = (Button)findViewById(R.id.btnForeat);
        mBtn_Foretc = (Button)findViewById(R.id.btnForetc);
        mBtn_Forshopping = (Button)findViewById(R.id.btnForshopping);
        mBtn_Forsight = (Button)findViewById(R.id.btnForsight);
        mBtn_Forsleep = (Button)findViewById(R.id.btnForsleep);
        mBtn_Fortransport = (Button)findViewById(R.id.btnFortransport);

        mBtn_insertTitle = (Button)findViewById(R.id.btn_insertTitle);

        imageView = (ImageView)findViewById(R.id.image);
        img_Foreat = (ImageView)findViewById(R.id.imgForeat);
        img_Foretc = (ImageView)findViewById(R.id.imgForetcetera);
        img_Forshopping = (ImageView)findViewById(R.id.imgForshopping);
        img_Forsight = (ImageView)findViewById(R.id.imgFortour) ;
        img_Forsleep = (ImageView)findViewById(R.id.imgForsleep);
        img_Fortransport = (ImageView)findViewById(R.id.imgFortransport);


        etMoney = (EditText)findViewById(R.id.et_insertmoney);
        etDetailedTitle = (EditText)findViewById(R.id.detailedTitle);

        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

//        mBtn_choiceDate.setOnClickListener(mInsertDate);
        mBtn_imageChoice.setOnClickListener(mInsertPictureListener);

        mBtn_Fortransport.setOnClickListener(mSelectMoneyTypeListener);
        mBtn_Forsleep.setOnClickListener(mSelectMoneyTypeListener);
        mBtn_Forsight.setOnClickListener(mSelectMoneyTypeListener);
        mBtn_Forshopping.setOnClickListener(mSelectMoneyTypeListener);
        mBtn_Foretc.setOnClickListener(mSelectMoneyTypeListener);
        mBtn_Foreat.setOnClickListener(mSelectMoneyTypeListener);

        img_Fortransport.setOnClickListener(mSelectMoneyTypeImageListener);
        img_Forsleep.setOnClickListener(mSelectMoneyTypeImageListener);
        img_Forsight.setOnClickListener(mSelectMoneyTypeImageListener);
        img_Forshopping.setOnClickListener(mSelectMoneyTypeImageListener);
        img_Foretc.setOnClickListener(mSelectMoneyTypeImageListener);
        img_Foreat.setOnClickListener(mSelectMoneyTypeImageListener);

        mBtn_insertTitle.setOnClickListener(mInsertCompleteListener);


        Intent intent = getIntent();
        chk = intent.getStringExtra("date");
        uri = intent.getParcelableExtra("uri");

        if(intent.getStringExtra("money") != null)
            etMoney.setText(intent.getStringExtra("money"));
        if(intent.getStringExtra("title") != null)
            etDetailedTitle.setText(intent.getStringExtra("title"));
        if(intent.getParcelableExtra("img") != null) {
            Bitmap bm = (Bitmap)intent.getParcelableExtra("img");
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(geturiPath(uri));
            } catch (IOException e) {
                e.printStackTrace();
            }
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            bm = rotateBitmap(bm, exifOrientation);
            imageView.setImageBitmap(bm);
        }
        if(intent.getStringExtra("type") != null)
        {
            System.out.println("intent.getStringExtra(type) : " + intent.getStringExtra("type"));
            if(intent.getStringExtra("type").equals("Sleep")) {
                System.out.println("sleep");
                mBtn_Forsleep.setTextColor(Color.RED);
            }
            else
                mBtn_Forsleep.setTextColor(Color.BLACK);

            if(intent.getStringExtra("type").equals("Eat")) {
                System.out.println("eat");
                mBtn_Foreat.setTextColor(Color.RED);
            }
            else
                mBtn_Foreat.setTextColor(Color.BLACK);

            if(intent.getStringExtra("type").equals("Transport")) {
                System.out.println("transport");
                mBtn_Fortransport.setTextColor(Color.RED);
            }
            else
                mBtn_Fortransport.setTextColor(Color.BLACK);

            if(intent.getStringExtra("type").equals("Etc")) {
                System.out.println("etc");
                mBtn_Foretc.setTextColor(Color.RED);
            }
            else
                mBtn_Foretc.setTextColor(Color.BLACK);

            if(intent.getStringExtra("type").equals("Shopping")) {
                System.out.println("shopping");
                mBtn_Forshopping.setTextColor(Color.RED);
            }
            else
                mBtn_Forshopping.setTextColor(Color.BLACK);

            if(intent.getStringExtra("type").equals("Sightseeing")) {
                System.out.println("sightseeing");
                mBtn_Forsight.setTextColor(Color.RED);
            }
            else
                mBtn_Forsight.setTextColor(Color.BLACK);

            moneyType = intent.getStringExtra("type");
        }
        if(intent.getStringExtra("change") != null)
            change = "change";
        else
            change = "notchange";
    }

    public String doYearMonthDay(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String currentDate = formatter.format(date);

        return currentDate;
    }


    Button.OnClickListener mInsertCompleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(AddActivity.this, NextActivity.class);

            if(moneyType == null){
                Toast.makeText(getApplicationContext(), "Choice Your Spending Type", Toast.LENGTH_LONG).show();
                return;
            }
            else if(etMoney.getText().toString().matches("")){
                Toast.makeText(getApplicationContext(), "Insert Your Spent Money", Toast.LENGTH_LONG).show();
                return;
            }
            else if(etDetailedTitle.getText().toString().matches("")){
                Toast.makeText(getApplicationContext(), "Insert Title", Toast.LENGTH_LONG).show();
                return;
            }


            intent.putExtra("Date", String.format("%d/%d/%d",mYear,mMonth+1, mDay));
            intent.putExtra("Type", moneyType);
            intent.putExtra("Money", etMoney.getText().toString());
            intent.putExtra("DetailedTitle", etDetailedTitle.getText().toString());
            intent.putExtra("index", chk);
            intent.putExtra("img", uri);

            if(change.equals("notchange")) {
                setResult(1, intent);
            }
            else if(change.equals("change")) {
                setResult(2, intent);
            }

            finish();
        }
    };

    Button.OnClickListener mSelectMoneyTypeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Button button = (Button)v;
            moneyType = button.getText().toString();
            if(button == mBtn_Forsleep)
                mBtn_Forsleep.setTextColor(Color.RED);
            else
                mBtn_Forsleep.setTextColor(Color.BLACK);

            if(button == mBtn_Foreat)
                mBtn_Foreat.setTextColor(Color.RED);
            else
                mBtn_Foreat.setTextColor(Color.BLACK);

            if(button == mBtn_Fortransport)
                mBtn_Fortransport.setTextColor(Color.RED);
            else
                mBtn_Fortransport.setTextColor(Color.BLACK);

            if(button == mBtn_Foretc)
                mBtn_Foretc.setTextColor(Color.RED);
            else
                mBtn_Foretc.setTextColor(Color.BLACK);

            if(button == mBtn_Forshopping)
                mBtn_Forshopping.setTextColor(Color.RED);
            else
                mBtn_Forshopping.setTextColor(Color.BLACK);

            if(button == mBtn_Forsight)
                mBtn_Forsight.setTextColor(Color.RED);
            else
                mBtn_Forsight.setTextColor(Color.BLACK);

        }
    };

    Button.OnClickListener mSelectMoneyTypeImageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            ImageView imageView = (ImageView)v;
//            moneyType = button.getText().toString();
            if(imageView == img_Forsleep) {
                mBtn_Forsleep.setTextColor(Color.RED);
                moneyType = mBtn_Forsleep.getText().toString();
            }
            else
                mBtn_Forsleep.setTextColor(Color.BLACK);

            if(imageView == img_Foreat) {
                mBtn_Foreat.setTextColor(Color.RED);
                moneyType = mBtn_Foreat.getText().toString();
            }
            else
                mBtn_Foreat.setTextColor(Color.BLACK);

            if(imageView == img_Fortransport) {
                mBtn_Fortransport.setTextColor(Color.RED);
                moneyType = mBtn_Fortransport.getText().toString();
            }
            else
                mBtn_Fortransport.setTextColor(Color.BLACK);

            if(imageView == img_Foretc) {
                mBtn_Foretc.setTextColor(Color.RED);
                moneyType = mBtn_Foretc.getText().toString();
            }
            else
                mBtn_Foretc.setTextColor(Color.BLACK);

            if(imageView == img_Forshopping) {
                mBtn_Forshopping.setTextColor(Color.RED);
                moneyType = mBtn_Forshopping.getText().toString();
            }
            else
                mBtn_Forshopping.setTextColor(Color.BLACK);

            if(imageView == img_Forsight) {
                mBtn_Forsight.setTextColor(Color.RED);
                moneyType = mBtn_Forsight.getText().toString();
            }
            else
                mBtn_Forsight.setTextColor(Color.BLACK);

        }
    };

    public void setType(String type)
    {

    }

    Button.OnClickListener mInsertPictureListener = new View.OnClickListener() {
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
        System.out.println("requestCode add activity : " + requestCode);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data) {
            //data에서 절대경로로 이미지를 가져옴
            uri = data.getData();

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //이미지가 한계이상(?) 크면 불러 오지 못하므로 사이즈를 줄여 준다.
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(geturiPath(uri));
            } catch (IOException e) {
                e.printStackTrace();
            }
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            bitmap = rotateBitmap(bitmap, exifOrientation);
            int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);
            Toast.makeText(this, "이미지가 첨부되었습니다.", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
        }

        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    img = BitmapFactory.decodeStream(in);
                    ExifInterface exif = new ExifInterface(geturiPath(data.getData()));
                    int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                    img = rotateBitmap(img, exifOrientation);
                    in.close();

                    // 이미지 표시
                    imageView.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if(resultCode == 2){
            if(data.getStringExtra("type") == "Sleep")
                mBtn_Forsleep.setTextColor(Color.RED);
            else
                mBtn_Forsleep.setTextColor(Color.BLACK);

            if(data.getStringExtra("type") == "Eat")
                mBtn_Foreat.setTextColor(Color.RED);
            else
                mBtn_Foreat.setTextColor(Color.BLACK);

            if(data.getStringExtra("type") == "Transport")
                mBtn_Fortransport.setTextColor(Color.RED);
            else
                mBtn_Fortransport.setTextColor(Color.BLACK);

            if(data.getStringExtra("type") == "Etc")
                mBtn_Foretc.setTextColor(Color.RED);
            else
                mBtn_Foretc.setTextColor(Color.BLACK);

            if(data.getStringExtra("type") == "Shopping")
                mBtn_Forshopping.setTextColor(Color.RED);
            else
                mBtn_Forshopping.setTextColor(Color.BLACK);

            if(data.getStringExtra("type") == "Sightseeing")
                mBtn_Forsight.setTextColor(Color.RED);
            else
                mBtn_Forsight.setTextColor(Color.BLACK);

            etMoney.setText(data.getStringExtra("money"));
            etDetailedTitle.setText(data.getStringExtra("title"));
        }
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
}

