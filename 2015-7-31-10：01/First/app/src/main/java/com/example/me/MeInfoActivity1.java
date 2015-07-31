package com.example.me;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.firstprogram.R;
import com.example.Class.Person;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 漫豆画芽 on 2015/7/21.
 */
public class MeInfoActivity1 extends Activity {
    private String httpurl_person_info;
    private TextView nick, gender, age;
    private RequestQueue requestQueue;
    private Person person;
    private LinearLayout editNickname, editGender, editAge;

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
        //gender
        editGender = (LinearLayout) findViewById(R.id.my_infor_gender);
        //age
        age = (TextView) findViewById(R.id.my_infor_age_textview);
        age.setText(String.valueOf(person.getAge()));
        editAge = (LinearLayout) findViewById(R.id.my_infor_age);
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
        editGender.setOnClickListener(new modify_gender_listener());
        editAge.setOnClickListener(new modify_age_listener());
    }
    public class modify_age_listener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            final EditText et = new EditText(MeInfoActivity1.this);
            AlertDialog.Builder builder = new AlertDialog.Builder(MeInfoActivity1.this);
            builder.setTitle("请输入年龄：");
            builder.setView(et);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    RequestQueue requestQueue = new Volley().newRequestQueue(MeInfoActivity1.this);
                    JSONObject map = new JSONObject();
                    try {
                        map.put("id",person.getId());
                        map.put("age", Integer.parseInt(et.getText().toString()));
                        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST,
                                "http://120.24.208.130:1503/user/modify_information", map,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject jsonObject) {
                                        if(jsonObject.toString().contains("id")){
                                            person.setAge(Integer.parseInt(et.getText().toString()));
                                            age.setText(et.getText().toString());
                                        }else{
                                            Toast.makeText(MeInfoActivity1.this,"modify failed",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(MeInfoActivity1.this,"modify failed",Toast.LENGTH_SHORT).show();
                            }
                        });
                        requestQueue.add(jsonRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
            builder.show();
        }
    }
    public class modify_gender_listener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            editGender = (LinearLayout) findViewById(R.id.my_infor_gender);
            editGender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MeInfoActivity1.this);
                    builder.setTitle("选择性别");
                    //    指定下拉列表的显示数据
                    final String[] cities = {"女", "男"};
                    //    设置一个下拉的列表选择项
                    builder.setItems(cities, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, final int which)
                        {
                            Toast.makeText(MeInfoActivity1.this, "选择的性别为：" + cities[which], Toast.LENGTH_SHORT).show();
                            if(which == person.getGender()){

                            } else{
                                RequestQueue requestQueue = new Volley().newRequestQueue(MeInfoActivity1.this);
                                JSONObject map = new JSONObject();
                                try {
                                    map.put("id", person.getId());
                                    map.put("gender", which);
                                    JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST,
                                            "http://120.24.208.130:1503/user/modify_information", map,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject jsonObject) {
                                                    Log.d("Tag", jsonObject.toString());
                                                    if(jsonObject.toString().contains("id")){
                                                            person.setGender(which);

                                                        if (person.getGender() == 0) {
                                                            gender.setText("女");
                                                        } else if (person.getGender() == 1) {
                                                            gender.setText("男");
                                                        }
                                                    }

                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError volleyError) {
                                            Log.d("Tag", "service error");
                                            Toast.makeText(MeInfoActivity1.this, "modify failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    requestQueue.add(jsonRequest);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    builder.show();
                }
            });
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                nick.setText(data.getStringExtra("change"));
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent mIntent = new Intent();
            mIntent.putExtra("change", person.getNick());
            // 设置结果，并进行传送
            MeInfoActivity1.this.setResult(1, mIntent);
            finish();
        }
        return false;
    }
}

