package com.example.firstprogram.myAdapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firstprogram.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class myExpandableListAdapter extends BaseExpandableListAdapter {
    private List<String> group_head;
    private List<List<Map<String, Object>>> child;
    private Context context;
    private LayoutInflater childlistContainer;


    public final class ChildListItem{
        public ImageView imageView;
        public TextView textView;
        public Button add;
        public Button delete;
    }

    public myExpandableListAdapter(Context context){
        //初始化组、子列表项
        group_head = new ArrayList<String>();
        child = new ArrayList<List<Map<String, Object>>>();
        this.context = context;
        this.childlistContainer = LayoutInflater.from(context);
    }

    public myExpandableListAdapter(Context context,List<String> group_head,
                           List<List<Map<String, Object>>> child){
        this.context = context;
        //初始化组、子列表项
        this.group_head = group_head;
        this.child = child;
        this.childlistContainer = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return group_head.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(groupPosition<0 || groupPosition >= this.child.size()){
            return 0;
        }
        return this.child.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group_head.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return child.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String text = group_head.get(groupPosition);
        if (convertView == null) {
            convertView = new TextView(context);
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 60);
            ((TextView) convertView).setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            convertView.setPadding(45, 0, 0, 0);
            convertView.setLayoutParams(lp);
        }
        ((TextView) convertView).setText(text);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Map<String, Object> data = child.get(groupPosition).get(childPosition);
        ChildListItem childlistitem = null;
        if (convertView == null) {
            childlistitem = new ChildListItem();
            convertView = childlistContainer.inflate(R.layout.contact_child_listitem,null);
            childlistitem.imageView = (ImageView) convertView.findViewById(R.id.contact_child_listitem_imageview);
            childlistitem.textView = (TextView) convertView.findViewById(R.id.contact_child_listitem_name);
            childlistitem.add = (Button) convertView.findViewById(R.id.contact_child_listitem_add);
            childlistitem.delete = (Button) convertView.findViewById(R.id.contact_child_listitem_delete);
            convertView.setTag(childlistitem);
        } else {
            childlistitem = (ChildListItem) convertView.getTag();
        }
        childlistitem.imageView.setBackgroundResource((Integer) data.get("image"));
        childlistitem.textView.setText(data.get("name").toString());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        this.childlistContainer = LayoutInflater.from(context);
        return true;
    }
}
