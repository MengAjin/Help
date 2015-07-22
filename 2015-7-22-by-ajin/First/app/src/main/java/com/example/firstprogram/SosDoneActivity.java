package com.example.firstprogram;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.firstprogram.myAdapter.mymyAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SosDoneActivity extends Activity {
    private ListView listview;
    private mymyAdapter adapter;
    private List<Map<String, Object>> listItemContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sos_end_assess);

        listview = (ListView) findViewById(R.id.sos_done_listview);
        listItemContainer = getListItem();

        adapter = new mymyAdapter(this, listItemContainer);
        listview.setAdapter(adapter);
    }
    private List<Map<String, Object>> getListItem() {
        List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();

        Map<String, Object> temp = new HashMap<String, Object>();
        temp.put("image", R.drawable.face1);
        temp.put("name", "大叔");
        lists.add(temp);

        temp = new HashMap<String, Object>();
        temp.put("image", R.drawable.face2);
        temp.put("name", "大婶");
        lists.add(temp);

        temp = new HashMap<String, Object>();
        temp.put("image", R.drawable.face3);
        temp.put("name", "大妈");
        lists.add(temp);

        return lists;
    }
}
