package com.example.my_publish;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.firstprogram.R;

import org.json.JSONException;

import java.util.List;

public class CardsAdapter extends BaseAdapter {

    public List<event> items;
    private final Context context;

    public CardsAdapter(Context context, List<event> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public event getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_card, null);

            holder = new ViewHolder();
            holder.itemText = (TextView) convertView.findViewById(R.id.list_item_card_text);
            holder.itemText2 = (TextView) convertView.findViewById(R.id.list_item_card_text2);
            holder.itemText3 = (TextView) convertView.findViewById(R.id.list_item_card_time);
            holder.itemText4 = (TextView) convertView.findViewById(R.id.list_item_card_state);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            holder.itemText.setText(items.get(position).getLauncher());
            //holder.itemText.setText("title");
            holder.itemText2.setText(items.get(position).getContent());
            holder.itemText3.setText(items.get(position).getTime());
            if (items.get(position).getState() == 0) {
                holder.itemText4.setText("进行中");
                holder.itemText4.setTextColor(0xffd43d3d);
            } else {
                holder.itemText4.setText("已结束");
                holder.itemText4.setTextColor(0xff707070);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private static class ViewHolder {
        private TextView itemText;//标题
        private TextView itemText2;//内容
        private TextView itemText3;//时间
        private TextView itemText4;//状态
    }

}
