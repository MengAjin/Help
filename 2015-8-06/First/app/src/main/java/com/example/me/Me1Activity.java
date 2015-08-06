package com.example.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.Class.Person;
import com.example.firstprogram.R;
import com.example.my_publish.MyPublishActivity;

/**
 * Created by 漫豆画芽 on 2015/7/21.
 */
public class Me1Activity extends Activity{
    private LinearLayout my_infor,my_send_out,setting;
    private TextView nick,gender;
    private Person person;

    private void init() {
        my_infor = (LinearLayout) findViewById(R.id.my_infor);
        nick = (TextView) findViewById(R.id.my_infor_nick1);
        person = (Person) getApplication();
        if(person.getNick()!=null)
            nick.setText(person.getNick());

        //我发出的监听
        my_send_out = (LinearLayout) findViewById(R.id.my_send_out);
        my_send_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Me1Activity.this, MyPublishActivity.class);
                startActivity(intent);
            }
        });
        //设置相关
        setting = (LinearLayout) findViewById(R.id.my_setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Me1Activity.this, SetUp.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_1_activity);
        init();
        my_infor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Me1Activity.this, "hahah", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Me1Activity.this, MeInfoActivity1.class);
                startActivityForResult(intent,1);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (resultCode) {
            case 1:
                nick.setText(data.getStringExtra("change"));
                break;
            default:
                break;
        }
    }


}
