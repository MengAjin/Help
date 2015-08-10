package com.example.firstprogram.myAdapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.evaluate.PersonalEvaluationActivity;
import com.example.firstprogram.R;

import java.util.List;
import java.util.Map;


public class mymyAdapter extends BaseAdapter{
    private Context context;
    private List<Map<String, Object>> listitem;
    private LayoutInflater listContainer;
    private Button evaluate;
    private long[] acc = new long[20];
    private int event_id;
    private long[] account;

    public mymyAdapter(Context context, List<Map<String, Object>> listitem, long[] accounts, int id){
        this.context = context;
        this.listitem = listitem;
        acc = accounts;
        event_id = id;
        listContainer = LayoutInflater.from(context);
    }
    public final class ListItemView{
        public ImageView imageView;
        public TextView textView;
        public Button assess;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new ListItemView();

            convertView = listContainer.inflate(R.layout.sos_me_list_item,null);
            listItemView.imageView = (ImageView) convertView.findViewById(R.id.sos_me_list_item_imageview);
            listItemView.textView = (TextView) convertView.findViewById(R.id.sos_me_list_item_textview);
            listItemView.assess = (Button) convertView.findViewById(R.id.sos_me_list_item_button);
            convertView.setTag(listItemView);
        }else {
            listItemView = (ListItemView) convertView.getTag();
        }

        listItemView.imageView.setImageResource(R.mipmap.ic_launcher);
        listItemView.textView.setText(String.valueOf(acc[position]));
        Log.d("account-mymyadapter",String.valueOf(acc[position]));

        //添加监听事件,将点击的position传给需要处理的页面
        listItemView.assess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "Person receive Account List -> " + acc[0]);
                Intent intent = new Intent(context, PersonalEvaluationActivity.class);
                account = new  long[20];
                account[0] = acc[position];
                //带参数跳转
                intent.putExtra("user_account", account);
                intent.putExtra("event_id", event_id);
                Log.d("TAG", "Person pass data -> " + account[0] + event_id);

                
                //intent.putExtra("index", getItemId(position));
                context.startActivity(intent);
            }
        });

        return convertView;
    }



    @Override
    public int getCount() {
        if(listitem != null) {
            return listitem.size();
        }
        else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return listitem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
