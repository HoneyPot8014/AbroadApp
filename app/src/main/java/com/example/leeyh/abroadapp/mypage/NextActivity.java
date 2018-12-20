package com.example.leeyh.abroadapp.mypage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leeyh.abroadapp.R;
import com.example.leeyh.abroadapp.view.fragment.TravelPlanFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;


public class NextActivity extends AppCompatActivity {

    private static String TAG = "recyclerview_example";

    private static String start[], end[];
    String start_date, end_date;

    private ArrayList<Dictionary> mArrayList;
    private CustomAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    listitemAdapter itemAdapter[];
    private ArrayList<list_item> list_itemArrayList[];

    private RelativeLayout add_layout;
    private LinearLayout addmoneylayout;

    list_item entry;
    int entryPosition;
    Gson gson;

    int cnt;
    int i;
    int searchTag;

    AlertDialog.Builder ad;

    private String value;
    private String theme;
    private String country;
    private String index = null;

    private Button btn;

    private EditText et;
    private EditText money;
    private EditText dialogBudget;

    //    private TextView dialogDate;
    private TextView spentMoney;
    private TextView sentMoney;
    private TextView leftMoney;

    ListView listView[];
    Sub n_layout[];
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        spentMoney = (TextView)findViewById(R.id.moneySpent);
        leftMoney = (TextView)findViewById(R.id.moneyLeft);

        if(getIntent().getStringExtra("recyclerCountry") != null){

            sp = getSharedPreferences("shared"+getIntent().getStringExtra("recyclerCountry"), MODE_PRIVATE);
            index = getIntent().getStringExtra("position");
//            System.out.println("getintent.getstringextra check : " + index);
//            System.out.println("getintent.getstringextra chaeck : " + getIntent().getStringExtra("recyclerCountry"));

            String strcontact = sp.getString("contacts" ,"");
            String strlistitem = sp.getString("listitem", "");

            Type listType = new TypeToken<ArrayList<Dictionary>>(){}.getType();

            ArrayList<Dictionary> datas = new Gson().fromJson(strcontact, listType);

            start = new String[3];
            end = new String[3];

            int cnt = 0;

            for(Dictionary data : datas){
                if(cnt == 0) {
                    end[2] = data.getId();
                    end[1] = data.getMonth();
                }
                if(cnt == datas.size()-2) {
                    start[2] = data.getId();
                    start[1] = data.getMonth();
                }
                cnt++;
            }
            leftMoney.setText(sp.getString("left", ""));
            spentMoney.setText(sp.getString("spent", ""));
            country = getIntent().getStringExtra("recyclerCountry");
        }
        else {
            start = ((TempActivity)TempActivity.mContext).getmTxt_StartDate().toString().split("/");
            end =   ((TempActivity)TempActivity.mContext).getmTxt_EndDate().toString().split("/");

            start_date = Arrays.toString(start);
            end_date = Arrays.toString(end);

            leftMoney.setText(getIntent().getStringExtra("budget"));
            country = getIntent().getStringExtra("countryname");
        }

        addmoneylayout = (LinearLayout)findViewById(R.id.addmoneylayout);
//        dialogDate = (TextView) findViewById(R.id.insert_dateinAddBudget);

        Intent intent = getIntent();

        theme = intent.getStringExtra("theme");

//        System.out.println("end[2] : " + end[2] + " start[2] : " +start[2]);
        listView = new ListView[Integer.parseInt(end[2]) - Integer.parseInt(start[2]) + 2];
        list_itemArrayList = new ArrayList[Integer.parseInt(end[2]) - Integer.parseInt(start[2]) + 2];
        itemAdapter = new listitemAdapter[Integer.parseInt(end[2]) - Integer.parseInt(start[2]) + 2];

        final Button add_button[] = new Button[Integer.parseInt(end[2]) - Integer.parseInt(start[2]) + 2];

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main_list);
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mLinearLayoutManager.setStackFromEnd(true);

        add_layout = (RelativeLayout) findViewById(R.id.add_layout);

        // MainActivity에서 RecyclerView의 데이터에 접근시 사용됩니다.
        mArrayList = new ArrayList<>();
        // RecyclerView를 위해 CustomAdapter를 사용합니다.
        mAdapter = new CustomAdapter( mArrayList);
        mRecyclerView.setAdapter(mAdapter);


        // RecyclerView의 줄(row) 사이에 수평선을 넣기위해 사용됩니다.
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);



        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        System.out.println("second - end[2] : " + end[2] + " start[2] : " + start[2]);
                        for(int i=0; i<Integer.parseInt(end[2])-Integer.parseInt(start[2]) + 2; i++){
                            System.out.println("position : " + position + "compare with : " + (Integer.parseInt(end[2])-Integer.parseInt(start[2]) + 1  - i));
                            if(position == Integer.parseInt(end[2])-Integer.parseInt(start[2]) + 1  - i){
                                add_button[i].setVisibility(View.VISIBLE);
                                listView[i].setVisibility(View.VISIBLE);
                            }
                            else{
                                add_button[i].setVisibility(View.INVISIBLE);
                                listView[i].setVisibility(View.INVISIBLE);
                            }
                        }
//                        Toast.makeText(getApplicationContext(),position+"번 째 아이템 클릭",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
//                        Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
                    }
                }));

        cnt = 0;

//        System.out.println(Integer.parseInt(end[2]) - Integer.parseInt(start[2]));
//        for(i=Integer.parseInt(end[2]); i>= Integer.parseInt(start[2]) -2; i--){
        for(i=Integer.parseInt(end[2]); i>= Integer.parseInt(start[2]) -1; i--){
            Dictionary dictionary;
            RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            buttonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            buttonParams.addRule(RelativeLayout.ABOVE, R.id.tableFormoney);
//            buttonParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;

            add_button[cnt] = new Button(this);
            add_button[cnt].setText("+");
            add_button[cnt].setLayoutParams(buttonParams);
            add_button[cnt].setOnClickListener(mAddScheduleListener);
            add_button[cnt].setTag(cnt);
            add_layout.addView(add_button[cnt]);
            if(i == Integer.parseInt(start[2]) - 1)
                dictionary = new Dictionary("R", "Ready");
            else {
                if(start[1].equals("1")){
                    dictionary = new Dictionary(Integer.toString(i), "Jan");
                }
                else if(start[1].equals("2")){
                    dictionary = new Dictionary(Integer.toString(i), "Feb");
                }
                else if(start[1].equals("3")){
                    dictionary = new Dictionary(Integer.toString(i), "Mar");
                }
                else if(start[1].equals("4")){
                    dictionary = new Dictionary(Integer.toString(i), "Apr");
                }
                else if(start[1].equals("5")){
                    dictionary = new Dictionary(Integer.toString(i), "May");
                }
                else if(start[1].equals("6")){
                    dictionary = new Dictionary(Integer.toString(i), "Jun");
                }
                else if(start[1].equals("7")){
                    dictionary = new Dictionary(Integer.toString(i), "Jul");
                }
                else if(start[1].equals("8")){
                    dictionary = new Dictionary(Integer.toString(i), "Aug");
                }
                else if(start[1].equals("9")){
                    dictionary = new Dictionary(Integer.toString(i), "Sep");
                }
                else if(start[1].equals("10")){
                    dictionary = new Dictionary(Integer.toString(i), "Oct");
                }
                else if(start[1].equals("11")){
                    dictionary = new Dictionary(Integer.toString(i), "Nov");
                }
                else{
                    dictionary = new Dictionary(Integer.toString(i), "Dec");
                }
            }

            list_itemArrayList[cnt] = new ArrayList<list_item>();
            itemAdapter[cnt] = new listitemAdapter(NextActivity.this, list_itemArrayList[cnt]);
            listView[cnt] = new ListView(this);
            listView[cnt].setAdapter(itemAdapter[cnt]);

            itemAdapter[cnt].notifyDataSetChanged();
            mArrayList.add(dictionary);
            mAdapter.notifyDataSetChanged();
            add_layout.addView(listView[cnt]);


            listView[cnt].setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    String item =((TextView)view)
                    //new ArrayList<list_item>();
                    entry= (list_item) parent.getAdapter().getItem(position);
                    System.out.println("entry.getType : " + entry.getType());
//                    ListView listView = (ListView)view;
                    final String[] items = new String[]{"Delete", "Change"};
                    final int[] selectedItem = {0};
                    entryPosition = position;

                    AlertDialog.Builder dialog = new AlertDialog.Builder(NextActivity.this);
                    dialog.setTitle("Delete Or Change")
                            .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    selectedItem[0] = which;
                                }
                            })
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if(selectedItem[0] == 0) {
                                        if(entry.getlistTitle().equals("Add Budget")){
                                            leftMoney.setText(String.valueOf(Double.parseDouble(leftMoney.getText().toString())-Double.parseDouble(entry.getMoney())));
//                                            spentMoney.setText(String.valueOf(Double.parseDouble(spentMoney.getText().toString())+Double.parseDouble(entry.getMoney())));
                                        }
                                        else{
                                            leftMoney.setText(String.valueOf(Double.parseDouble(leftMoney.getText().toString())+Double.parseDouble(entry.getMoney())));
                                            spentMoney.setText(String.valueOf(Double.parseDouble(spentMoney.getText().toString())-Double.parseDouble(entry.getMoney())));
                                        }
                                        list_itemArrayList[Integer.parseInt(entry.getIndex())].remove(entryPosition);
                                        itemAdapter[Integer.parseInt(entry.getIndex())].notifyDataSetChanged();
                                    }
                                    else if(selectedItem[0] == 1){
                                        Intent intent = new Intent(NextActivity.this, AddActivity.class);

                                        intent.putExtra("type", entry.getType());
                                        intent.putExtra("title", entry.getlistTitle());
                                        intent.putExtra("money", entry.getMoney());
                                        intent.putExtra("change", "change");

                                        BitmapFactory.Options options = new BitmapFactory.Options();
                                        options.inSampleSize = 10;
                                        System.out.println("entry.getURI : " + entry.getUri());
                                        String path = geturiPath(entry.getUri());
                                        Bitmap src = BitmapFactory.decodeFile(path, options);
                                        System.out.println("path : " + path);

                                        intent.putExtra("img", src);
                                        intent.putExtra("uri", Uri.parse(entry.getUri()));

                                        startActivityForResult(intent, 2);

                                    }
//                            finish();
                                }
                            })
                            .setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    dialog.create();
                    dialog.show();
                }
            });


            if(cnt != 0){
                add_button[cnt].setVisibility(View.INVISIBLE);
                listView[cnt].setVisibility(View.INVISIBLE);
            }
            cnt++;
        }


        if(getIntent().getStringExtra("travelIndex") != null) {
            if (!sp.getString("listitem0", "").equals("")) {
                Type listitemType[] = new Type[list_itemArrayList.length];
                for (int ii = 0; ii < list_itemArrayList.length; ii++) {
                    listitemType[ii] = new TypeToken<ArrayList<list_item>>() {
                    }.getType();
                    ArrayList<list_item> datas = new Gson().fromJson(sp.getString("listitem" + ii, ""), listitemType[ii]);

                    for (list_item item : datas) {
//                    System.out.println("list : " + item.getTitle());
                        list_itemArrayList[ii].add(item);
                    }

                }

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String geturiPath(String uri) {
        String wholeID = DocumentsContract.getDocumentId(Uri.parse(uri));

// Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

// where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{ id }, null);

        String filePath = "";

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }

        cursor.close();
       /* String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(Uri.parse(uri), projection, null, null, null);
        startManagingCursor(cursor);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);*/
        return filePath;
    }

    Button.OnClickListener mAddScheduleListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final String[] items = new String[]{"Spent", "Income"};
            final int[] selectedItem = {0};
            AlertDialog.Builder dialog = new AlertDialog.Builder(NextActivity.this);
            dialog.setTitle("Spent | Income Choice")
                    .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            selectedItem[0] = which;
                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            btn = (Button)v;

                            if(selectedItem[0] == 0) {
                                Intent intent = new Intent(NextActivity.this, AddActivity.class);
                                intent.putExtra("date", btn.getTag().toString());
                                startActivityForResult(intent, 1);
                            }
                            else if(selectedItem[0] == 1){
                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                final View formElementsView = inflater.inflate(R.layout.addmoneylayout, null, false);

                                String titleText = "Add Budget";
                                dialogBudget = (EditText)formElementsView.findViewById(R.id.dialogBudget);

                                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.BLACK);
                                SpannableStringBuilder ssBuilder = new SpannableStringBuilder("Add Budget");

                                ssBuilder.setSpan(
                                        foregroundColorSpan,
                                        0,
                                        titleText.length(),
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                                );

                                new AlertDialog.Builder(NextActivity.this).setView(formElementsView)
                                        .setTitle(ssBuilder)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                list_itemArrayList[Integer.parseInt(btn.getTag().toString())].add(new list_item("plus","Add Budget", " ",dialogBudget.getText().toString(), btn.getTag().toString(), "Add", null, null));
                                                double spent = Double.parseDouble(dialogBudget.getText().toString());

                                                leftMoney.setText(String.valueOf(Double.parseDouble(leftMoney.getText().toString()) + spent));
//                                                spentMoney.setText(String.valueOf(Double.parseDouble(spentMoney.getText().toString()) - spent));
                                            }
                                        }).show();

                            }
//                            finish();
                        }
                    })
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            dialog.create();
            dialog.show();
        }
    };

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(resultCode == 1) {
            if (data.getStringExtra("Date") != null && data.getStringExtra("Type") != null && data.getStringExtra("Money") != null && data.getStringExtra("DetailedTitle") != null) {

                if(data.getParcelableExtra("img") != null) {
                    Uri uri = data.getParcelableExtra("img");
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ExifInterface exif = null;
                    try {
                        exif = new ExifInterface(geturiPath(uri));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                    bitmap = rotateBitmap(bitmap, exifOrientation);
                    list_itemArrayList[Integer.parseInt(data.getStringExtra("index"))].add(new list_item(data.getStringExtra("Type"), data.getStringExtra("DetailedTitle"), " ", data.getStringExtra("Money"), data.getStringExtra("index"), "spent", bitmap, uri.toString()));
                }
                else
                    list_itemArrayList[Integer.parseInt(data.getStringExtra("index"))].add(new list_item(data.getStringExtra("Type"), data.getStringExtra("DetailedTitle"), " ", data.getStringExtra("Money"), data.getStringExtra("index"), "spent", null, null));
                itemAdapter[Integer.parseInt(data.getStringExtra("index"))].notifyDataSetChanged();

                double spent = Double.parseDouble(data.getStringExtra("Money"));

                leftMoney.setText(String.valueOf(Double.parseDouble(leftMoney.getText().toString()) - spent));
                spentMoney.setText(String.valueOf(Double.parseDouble(spentMoney.getText().toString()) + spent));
            }
        }
        else if(resultCode == 2){

            int temp = Integer.parseInt(entry.getIndex());

            list_item item;
//            list_item item = new list_item(data.getStringExtra("Type"), data.getStringExtra("DetailedTitle"), " ", data.getStringExtra("Money"), data.getStringExtra("index"), entry.getMoneyType(), data.getStringExtra(""), null);
            if(data.getParcelableExtra("img") != null) {
                Uri uri = data.getParcelableExtra("img");
                System.out.println("uri : " + uri);
                Bitmap bitmap = null;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 10;
                String path = geturiPath(uri);
                bitmap = BitmapFactory.decodeFile(path, options);
                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(geturiPath(uri));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                bitmap = rotateBitmap(bitmap, exifOrientation);
                item = new list_item(data.getStringExtra("Type"), data.getStringExtra("DetailedTitle"), " ", data.getStringExtra("Money"), data.getStringExtra("index"), "spent", bitmap, uri.toString());
            }
            else
                item = new list_item(data.getStringExtra("Type"), data.getStringExtra("DetailedTitle"), " ", data.getStringExtra("Money"), data.getStringExtra("index"), "spent", null, null);

            list_itemArrayList[temp].set(entryPosition, item);
            itemAdapter[temp].notifyDataSetChanged();
        }
    }
    @Override
    public void onBackPressed()
    {
        onSaveData();
        super.onBackPressed();
    }

    protected void onSaveData()
    {
        SharedPreferences sp = getSharedPreferences("shared" + country, MODE_PRIVATE);
        System.out.println("shared : " + country);
        SharedPreferences.Editor editor = sp.edit();

        gson = new GsonBuilder().create();
        Type listType = new TypeToken<ArrayList<Dictionary>>(){}.getType();
        Type listitemType[] = new Type[list_itemArrayList.length];

        String[] listitemjson = new String[list_itemArrayList.length];

        for(int ii=0; ii < list_itemArrayList.length; ii++){
            listitemType[ii] = new TypeToken<ArrayList<list_item>>(){}.getType();
            listitemjson[ii] = gson.toJson(list_itemArrayList[ii], listitemType[ii]);
            editor.putString("listitem"+ii, listitemjson[ii]);
        }
//                new TypeToken<ArrayList<list_item>>(){}.getType();
//        private ArrayList<list_item> list_itemArrayList[];
        String json = gson.toJson(mArrayList, listType);//        listitemjson = gson.toJson(list_itemArrayList, listitemType);
        editor.putString("contacts", json);
        System.out.println("spent put String : " + spentMoney.getText().toString());
        System.out.println("left put String : " + leftMoney.getText().toString());

        editor.putString("left", leftMoney.getText().toString());
        editor.putString("spent", spentMoney.getText().toString());
//        editor.putString("position", index);

//        Intent intent = new Intent();
        Intent intent = new Intent(NextActivity.this, TravelPlanFragment.class);
        intent.putExtra("leftMoney", leftMoney.getText().toString());
        System.out.println("index : " + index);
        intent.putExtra("position", index);
        setResult(RESULT_OK, intent);

        editor.commit();
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

