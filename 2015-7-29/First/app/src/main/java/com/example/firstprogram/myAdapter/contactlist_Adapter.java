package com.example.firstprogram.myAdapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firstprogram.R;

import java.util.List;
import java.util.Map;


public class contactlist_Adapter extends BaseAdapter{
    private Context context;
    private List<Map<String, Object>> listitem;
    private LayoutInflater listContainer;

    public contactlist_Adapter(Context context, List<Map<String, Object>> listitem){
        this.context = context;
        this.listitem = listitem;
        this.listContainer = LayoutInflater.from(context);
    }
    public final class ListItemView{
        public ImageView imageView;
        public TextView textView;
        public ImageView delete;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new ListItemView();

            convertView = listContainer.inflate(R.layout.emergency_contact_listitem,null);
            listItemView.imageView = (ImageView) convertView.findViewById(R.id.emergency_contact_imageview);
            listItemView.textView = (TextView) convertView.findViewById(R.id.emergency_contact_textview);
            listItemView.delete = (ImageView) convertView.findViewById(R.id.emergency_contact_button);
            convertView.setTag(listItemView);
        }else {
            listItemView = (ListItemView) convertView.getTag();
        }

        listItemView.imageView.setBackgroundResource((Integer) listitem.get(position).get("image"));
        listItemView.textView.setText(listitem.get(position).get("name").toString());
        return convertView;
    }


    @Override
    public int getCount() {
        return listitem.size();
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
