package com.example.contactList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.firstprogram.R;
import com.example.firstprogram.TopBar;
import com.example.firstprogram.myAdapter.contactlist_Adapter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EmergencyContactActivity extends Activity {
    private ListView list;
    private contactlist_Adapter adapter;
    private List<Map<String, Object>> data;
    private TopBar topbar;
    private Cursor mCursor;
    private EmergencyContactsDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_contact_activity);
        //初始化数据库
        db = new EmergencyContactsDB(this);
        mCursor = db.select();

        init();
        data = getListItem();

        adapter = new contactlist_Adapter(this, data);
        list.setAdapter(adapter);
    }
    private void init(){
        list = (ListView) findViewById(R.id.listView_in_emergencycontact);
        topbar = (TopBar) findViewById(R.id.topbarInmergencyContactList);
        //设计topbar左右（返回和添加）两个按钮的监听事件
        topbar.setOnTopbarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() throws UnsupportedEncodingException {
                //去到添加紧急联系人页面
                Intent intent = new Intent(EmergencyContactActivity.this, EmergencyContactAdd.class);
                startActivityForResult(intent, 1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data_){
        switch (resultCode) {
            //若返回码为1则显示在紧急联系人列表
            case 1:
                List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
                Map<String, Object> temp = new HashMap<String, Object>();
                temp.put("image", R.drawable.face1);
                temp.put("name", "账号：" + data_.getStringExtra("account") + "      昵称："
                        + data_.getStringExtra("nickname") );
                data.add(temp);
                adapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private List<Map<String, Object>> getListItem() {
        List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
        Map<String, Object> temp = new HashMap<String, Object>();

        if (mCursor != null) {
        // 循环遍历cursor
            while (mCursor.moveToNext()) {
                temp = new HashMap<String, Object>();
                // 拿到每一行name 与hp的数值
                temp.put("image", R.drawable.face1);
                temp.put("name","账号：" + mCursor.getString(mCursor.getColumnIndex("EmergencyContacts_account")) +"    昵称："
                        + mCursor.getString(mCursor.getColumnIndex("EmergencyContacts_name")));
                lists.add(temp);
                Log.v("info", "账号是 " +mCursor.getLong(mCursor.getColumnIndex("EmergencyContacts_account"))  + "昵称 "
                        + mCursor.getString(mCursor.getColumnIndex("EmergencyContacts_name")));
            }
            // 关闭
            mCursor.close();
        }
        return lists;
    }
}
