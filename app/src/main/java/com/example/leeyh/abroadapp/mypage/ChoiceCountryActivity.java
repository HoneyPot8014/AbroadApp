package com.example.leeyh.abroadapp.mypage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.leeyh.abroadapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ChoiceCountryActivity extends AppCompatActivity {

    private List<String> list;          // 데이터를 넣은 리스트변수
    private ListView listView;          // 검색을 보여줄 리스트변수
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private SearchAdapter adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<String> arraylist;
    List<String> country;
    String[] countryArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_country);

        editSearch = (EditText) findViewById(R.id.editSearch);

        listView = (ListView) findViewById(R.id.listView);

        // 리스트를 생성한다.
        list = new ArrayList<String>();

        // 검색에 사용할 데이터을 미리 저장한다.
        settingList();

        // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
        arraylist = new ArrayList<String>();
        arraylist.addAll(list);

        // 리스트에 연동될 아답터를 생성한다.
        adapter = new SearchAdapter(list, this);

        // 리스트뷰에 아답터를 연결한다.
        listView.setAdapter(adapter);

        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                String text = editSearch.getText().toString();
                search(text);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                System.out.println(arraylist.get(position));
                String model = (String)list.get(position);
                System.out.println("model : " + model);
                Intent intent = new Intent(ChoiceCountryActivity.this, TempActivity.class);
                intent.putExtra("country", model);
                startActivityForResult(intent, 1);
//                finish();
            }
        });
    }

    // 검색을 수행하는 메소드
    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            list.addAll(arraylist);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < arraylist.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist.get(i).toLowerCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    list.add(arraylist.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }

    // 검색에 사용될 데이터를 리스트에 추가한다.
    private void settingList(){

        country = new ArrayList<String>();
        countryArray = Locale.getISOCountries();
        for( int i=0; i < countryArray.length; i++ ){
            Locale locale = new Locale( "en", countryArray[i] );
            country.add(locale.getDisplayCountry(Locale.ENGLISH));
        }

        country.toArray(countryArray);
        Arrays.sort(countryArray);

        for(int i=0; i< countryArray.length; i++)
            list.add(countryArray[i]);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        System.out.println("resultcode : " + resultCode);
        if(resultCode == RESULT_OK){
            System.out.println("stardate : " + data.getStringExtra("startdate"));
            System.out.println("countryname : " +  data.getStringExtra("countryname"));
//            System.out.println("budget : " + data.getStringExtra("budget"));
//            System.out.println("budget : " + getIntent().getStringExtra("leftMoney"));

            Intent intent = new Intent(ChoiceCountryActivity.this, MainActivity.class);

            intent.putExtra("countryname",data.getStringExtra("countryname"));
            intent.putExtra("startdate",data.getStringExtra("startdate"));
            intent.putExtra("enddate",data.getStringExtra("enddate"));
            intent.putExtra("budget", data.getStringExtra("budget"));
            intent.putExtra("img", data.getParcelableExtra("img"));

            setResult(RESULT_OK, intent);
            finish();
        }
    }


}

