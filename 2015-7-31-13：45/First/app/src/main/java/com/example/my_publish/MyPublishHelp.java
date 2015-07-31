package com.example.my_publish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Class.GetAllParams;
import com.example.Class.Person;
import com.example.firstprogram.R;
import com.example.firstprogram.TopBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by 漫豆画芽 on 2015/7/27.
 */
public class MyPublishHelp extends Activity{
    private TextView title, content, time, address, state, follow_number, support_number;
    private TopBar topbar;
    private int event_id , _state, _follow_number, _support_number, _type;
    private String _time,_title,_content,_address;
    private Person person;
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypublish_help_detail);
        init();
        //topbar监听
        topbar.setOnTopbarClickListener(new onTopBarListener());
     }
    private void init() {
        person = (Person) getApplication();
        topbar = (TopBar) findViewById(R.id.topBar);
        //findviewbyid
        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);
        time = (TextView) findViewById(R.id.time);
        address = (TextView) findViewById(R.id.address);
        state = (TextView) findViewById(R.id.state);
        follow_number = (TextView) findViewById(R.id.follow_number);
        support_number = (TextView) findViewById(R.id.support_number);


        Intent intent = getIntent();
        event_id = intent.getIntExtra("event_id",0);
        _title = intent.getStringExtra("title");
        _content = intent.getStringExtra("content");
        _time = intent.getStringExtra("time");
        _address = intent.getStringExtra("address");
        _state = intent.getIntExtra("state", 0);
        _follow_number = intent.getIntExtra("follow_number",0);
        _support_number = intent.getIntExtra("support_number",0);
        position = intent.getIntExtra("position",0);
        _type = intent.getIntExtra("type", 0);

        if (_type == 0)
            topbar.setTitleText(intent.getStringExtra("提问事件"));
        if (_type == 0)
            topbar.setTitleText(intent.getStringExtra("求助事件"));
        if (_type == 0)
            topbar.setTitleText(intent.getStringExtra("求救事件"));
        title.setText("title");
        content.setText(_content);
        time.setText(_time);

        if (_state == 0) {
            //Log.d("TAG-state_before", intent.getStringExtra("state"));
            state.setText("求助中");
        } else if (_state == 1) {
            //Log.d("TAG-state_before", intent.getStringExtra("state"));
            state.setText("求助结束");
        }
        follow_number.setText(intent.getStringExtra("follow_number"));
        support_number.setText(intent.getStringExtra("support_number"));


    }

    private class onTopBarListener implements TopBar.topbarClickListener {
        @Override
        public void leftClick() {
            finish();
        }

        @Override
        public void rightClick() throws UnsupportedEncodingException{
            if(_state == 1){
                Toast.makeText(MyPublishHelp.this,"该事件已经结束",Toast.LENGTH_SHORT).show();
            }
            //判断帮助者人数是否为0,为0的话就给出直接结束回到主界面，否则需要进入到评价页面
            else if(_follow_number!=0){
                Toast.makeText(MyPublishHelp.this,"去到评价页面",Toast.LENGTH_SHORT).show();
            }else {
                GetAllParams getAllParams = new GetAllParams(MyPublishHelp.this);
                JSONObject param = new JSONObject();
                try {
                    param.put("id", person.getId());
                    param.put("event_id", (event_id));
                    param.put("state", 1);
                    Log.d("TAG-result-200-0", String.valueOf(event_id));
                    getAllParams.getList("http://120.24.208.130:1503/event/modify", param, new GetAllParams.VolleyJsonCallback() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            Log.d("TAG-result", result.toString());
                            try {
                                if (result.getInt("status")==200) {
                                    Log.d("TAG-result-200-1", result.toString());
                                    Toast.makeText(MyPublishHelp.this, "状态已修改", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    intent.putExtra("position", position);
                                    intent.putExtra("state", 1);
                                    MyPublishHelp.this.setResult(1,intent);
                                    finish();
                                } else {
                                    Toast.makeText(MyPublishHelp.this, "500 error", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
