package com.example.leeyh.abroadapp.mypage;

import android.app.LauncherActivity;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leeyh.abroadapp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class listitemAdapter extends BaseAdapter {
    Context context;
    ArrayList<list_item> list_itemArrayList;
    TextView title_textView;
    TextView money_textView;
    ImageView type_imageView;
    ImageView choice_imageView;


    private RecyclerView imageRecyclerView;
    private RecyclerView.Adapter imageAdapter;
    private RecyclerView.LayoutManager imageLayoutManager;

    public listitemAdapter(Context context, ArrayList<list_item> list_itemArrayList) {
        this.context = context;
        this.list_itemArrayList = list_itemArrayList;
    }

    @Override
    public int getCount() {
        return this.list_itemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list_itemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        if(convertView == null){
        convertView = LayoutInflater.from(context).inflate(R.layout.schedule_list, null);
        money_textView = (TextView)convertView.findViewById(R.id.money);
        title_textView = (TextView)convertView.findViewById(R.id.title);
        type_imageView = (ImageView) convertView.findViewById(R.id.type);
        choice_imageView = (ImageView)convertView.findViewById(R.id.choiceImage);
//        }

        money_textView.setText(list_itemArrayList.get(position).getMoney());
        title_textView.setText(list_itemArrayList.get(position).getlistTitle());
        choice_imageView.setImageBitmap(list_itemArrayList.get(position).getImg());
        title_textView.refreshDrawableState();

        System.out.println("list_itemArrayList : " + list_itemArrayList.get(position).getlistTitle());
        System.out.println("title_textView : " + title_textView.getText().toString());

        if(list_itemArrayList.get(position).getType().equals("Transport")){
            type_imageView.setImageResource(R.drawable.transport);
        }
        else if(list_itemArrayList.get(position).getType().equals("Eat")){
            type_imageView.setImageResource(R.drawable.eat);
        }
        else if(list_itemArrayList.get(position).getType().equals("Shopping")){
            type_imageView.setImageResource(R.drawable.shopping);
        }
        else if(list_itemArrayList.get(position).getType().equals("Sightseeing")){
            type_imageView.setImageResource(R.drawable.tour);
        }
        else if(list_itemArrayList.get(position).getType().equals("Sleep")){
            type_imageView.setImageResource(R.drawable.hotel);
        }
        else if(list_itemArrayList.get(position).getType().equals("Etc")){
            type_imageView.setImageResource(R.drawable.etcetera);
        }
        else if(list_itemArrayList.get(position).getType().equals("plus")){
            type_imageView.setImageResource(R.drawable.plus);
        }

        if(list_itemArrayList.get(position).getMoneyType().equals("spent"))
            money_textView.setTextColor(Color.RED);
        else
            money_textView.setTextColor(Color.GREEN);


        return convertView;
    }
}

