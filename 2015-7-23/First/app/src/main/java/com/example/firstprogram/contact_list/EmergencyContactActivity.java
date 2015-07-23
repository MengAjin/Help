package com.example.firstprogram.contact_list;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.firstprogram.R;
import com.example.firstprogram.myAdapter.contactlist_Adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EmergencyContactActivity extends Activity {
    private ListView list;
    private contactlist_Adapter adapter;
    private List<Map<String, Object>> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_contact_activity);
        list = (ListView) findViewById(R.id.listView_in_emergencycontact);
        data = getListItem();

        adapter = new contactlist_Adapter(this, data);
        list.setAdapter(adapter);

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
