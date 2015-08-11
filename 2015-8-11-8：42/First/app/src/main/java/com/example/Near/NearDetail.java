package com.example.Near;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Class.GetAllParams;
import com.example.Class.Person;
import com.example.Navigation.RoutePlanDemo;
import com.example.firstprogram.R;
import com.example.firstprogram.TopBar;
import com.example.my_publish.event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 漫豆画芽 on 2015/7/31.
 */
public class NearDetail extends Activity {
    private Person person;
    private TopBar topbar;
    private int event_id , _state, _follow_number, _support_number, _type;
    private String _time,_title,_content,_address;
    private event e;
    private GetAllParams getAllParams;
    private TextView title, content, time, address, state, follow_number, support_number;
    private ListView pingjia;
    private List<Map<String,Object>> listData = new ArrayList<>();
    private SimpleAdapter mSimpleAdapter;
    //发表评论有关
    private EditText edittext_pingjia;
    private Button btn_pingjia;

    //定位相关
    private double longitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.near_detail);
        person = (Person) getApplication();
        getAllParams = new GetAllParams(this);
        //findviewbyid
        init();

        //关于listview
        mSimpleAdapter = new SimpleAdapter(this,
                listData,
                R.layout.near_detail_listitem,
                new String[] { "author_id", "content" },
                new int[] {R.id.textView1, R.id.textView2});
        pingjia.setAdapter(mSimpleAdapter);
        mSimpleAdapter.notifyDataSetChanged();

    }
    public void init() {
        edittext_pingjia = (EditText) findViewById(R.id.edittext_pingjia);
        btn_pingjia = (Button) findViewById(R.id.send_pingjia);
        btn_pingjia.setOnClickListener(new pingjia_listener());

        topbar = (TopBar) findViewById(R.id.topbar);
        topbar.setOnTopbarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                Toast.makeText(NearDetail.this, "左btn", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void rightClick() throws UnsupportedEncodingException {
                final Intent intent = new Intent(NearDetail.this, RoutePlanDemo.class);
                intent.putExtra("event_id",event_id);
                intent.putExtra("longitude",longitude);
                intent.putExtra("latitude",latitude);
                intent.putExtra("type", _type);
                JSONObject params = new JSONObject();
                try {
                    params.put("id", person.getId());
                    params.put("event_id",event_id);
                    params.put("operation",2);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                getAllParams.getList("http://120.24.208.130:1503/user/event_manage", params, new GetAllParams.VolleyJsonCallback() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Log.d("user/event_manage", jsonObject.toString());
                        try {
                            if (jsonObject.getInt("status") == 200) {
                                Toast.makeText(NearDetail.this,"参与成功",Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);
        time = (TextView) findViewById(R.id.time);
        address = (TextView) findViewById(R.id.address);
        state = (TextView) findViewById(R.id.state);
        follow_number = (TextView) findViewById(R.id.follow_number);
        support_number = (TextView) findViewById(R.id.support_number);
        pingjia = (ListView) findViewById(R.id.pingjia);

        //获取前一个页面传过来的参数
        Intent intent =  getIntent();
        event_id = intent.getIntExtra("event_id", 0);
        _title = intent.getStringExtra("title");
        _content = intent.getStringExtra("content");
        _time = intent.getStringExtra("time");
        _address = intent.getStringExtra("address");
        _state = intent.getIntExtra("state", 0);
        _follow_number = intent.getIntExtra("follow_number",0);
        _support_number = intent.getIntExtra("support_number",0);
        _type = intent.getIntExtra("type", 0);
        longitude = intent.getDoubleExtra("longitude", 23.072552);
        latitude = intent.getDoubleExtra("latitude", 113.395988);


        if (_type == 0)
            topbar.setTitleText(intent.getStringExtra("提问事件"));
        if (_type == 0)
            topbar.setTitleText(intent.getStringExtra("求助事件"));
        if (_type == 0)
            topbar.setTitleText(intent.getStringExtra("求救事件"));
        title.setText(_title);
        content.setText(_content);
        time.setText(_time);

        if (_state == 0) {
            //Log.d("TAG-state_before", intent.getStringExtra("state"));
            state.setText("求助中");
        } else if (_state == 1) {
            //Log.d("TAG-state_before", intent.getStringExtra("state"));
            state.setText("求助结束");
        }
        follow_number.setText(String.valueOf(intent.getIntExtra("follow_number", 0)));
        support_number.setText(String.valueOf(intent.getIntExtra("support_number", 0)));

        //获取评论数据
        try {
            JSONObject jsonObject1 = new JSONObject(intent.getStringExtra("pingjia"));
            JSONArray jsonArray = jsonObject1.getJSONArray("comment_list");
            Map<String,Object> temp ;
            for(int i=0; i<jsonArray.length();i++) {
                temp = new HashMap<>();
                JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
                temp.put("author_id",jsonObject2.getInt("author_id"));
                temp.put("content",jsonObject2.getString("content"));
                listData.add(temp);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    private class pingjia_listener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //发出评论操作
            JSONObject params = new JSONObject();
            try {
                params.put("id", person.getId());
                params.put("event_id",event_id);
                params.put("content", edittext_pingjia.getText().toString());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            getAllParams.getList("http://120.24.208.130:1503/comment/add", params, new GetAllParams.VolleyJsonCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    Log.d("comment-query", jsonObject.toString());
                    try {
                        if (jsonObject.getInt("status") == 200) {
                            Map<String,Object> temp = new HashMap<>();
                            temp.put("author_id",person.getId());
                            temp.put("content",edittext_pingjia.getText().toString());
                            listData.add(temp);
                            mSimpleAdapter.notifyDataSetChanged();
                            edittext_pingjia.setText("");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
