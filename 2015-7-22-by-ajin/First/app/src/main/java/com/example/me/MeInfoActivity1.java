package com.example.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.firstprogram.R;
import com.example.myAppData.Person;

/**
 * Created by 漫豆画芽 on 2015/7/21.
 */
public class MeInfoActivity1 extends Activity {
    private String httpurl_person_info;
    private TextView nick, gender,age;
    private RequestQueue requestQueue;
    private Person person;
    private LinearLayout editNickname;
    private void init() {
        editNickname = (LinearLayout) findViewById(R.id.my_infor_nick2);
        httpurl_person_info = "http://120.24.208.130:1503/user/get_information";
        person = (Person) getApplication();
        //nick
        nick = (TextView) findViewById(R.id.my_infor_nick2_textview);
        nick.setText(person.getNick());

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        //gender
        gender = (TextView) findViewById(R.id.my_infor_gender_textview);
        if (person.getGender() == 0) {
            gender.setText("女");
        } else if (person.getGender() == 1) {
            gender.setText("男");
        }
        //age
        age = (TextView) findViewById(R.id.my_infor_age_textview);
        age.setText(String.valueOf(person.getAge()));


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_1_myinfo_activity);
        init();
        editNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeInfoActivity1.this, MeInfoEditNicknameActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode) {
            case 1:
                nick.setText(data.getStringExtra("change"));
                break;
            default:
                break;
        }
    }
}
