package com.example.firstprogram.contact_list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.example.firstprogram.R;
import com.example.firstprogram.TopBar;
import com.example.firstprogram.myAdapter.myExpandableListAdapter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ContactListActivity extends Activity {
    private ExpandableListView list;
    private TopBar topbar;
    private List<String> group_head;
    private List<List<Map<String, Object>>> childdatas;
    private myExpandableListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list);

        list = (ExpandableListView) findViewById(R.id.expandablelistview_in_contactlist);
        group_head = new ArrayList<String>();
        childdatas = new ArrayList<List<Map<String, Object>>>();

        addGroup("家人");
        addGroup("朋友");
        addGroup("邻居");

        Map<String, Object> temp = new HashMap<String, Object>();
        temp.put("image", R.drawable.face1);
        temp.put("name", "大叔");
        addChild(0, temp);

        temp = new HashMap<String, Object>();
        temp.put("image", R.drawable.face2);
        temp.put("name", "大婶");
        addChild(0, temp);


        temp = new HashMap<String, Object>();
        temp.put("image", R.drawable.face3);
        temp.put("name", "大妈");
        addChild(1, temp);

        temp = new HashMap<String, Object>();
        temp.put("image", R.drawable.face4);
        temp.put("name", "大姐");
        addChild(2, temp);

        adapter = new myExpandableListAdapter(this, group_head, childdatas);
        list.setAdapter(adapter);
        list.setCacheColorHint(0);

        //topbar
        topbar = (TopBar) findViewById(R.id.topbarInContactList);
        topbar.setOnTopbarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {

            }

            @Override
            public void rightClick() throws UnsupportedEncodingException {
                Intent intent = new Intent(ContactListActivity.this, EmergencyContactActivity.class);
                startActivity(intent);
            }
        });
    }
    public void addGroup(String group){
        group_head.add(group);
        childdatas.add(new ArrayList<Map<String, Object>>()); //child中添加新数组
    }
    public void addChild(int position,Map<String, Object> child){
        List<Map<String, Object>> it = this.childdatas.get(position);
        if(it != null){
            it.add(child);
        }else{
            it = new ArrayList<Map<String, Object>>();
            it.add(child);
        }
    }
}
