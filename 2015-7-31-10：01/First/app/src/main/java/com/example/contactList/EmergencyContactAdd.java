package com.example.contactList;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.example.firstprogram.TopBar;
import com.example.Class.Person;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class EmergencyContactAdd extends Activity {
    private TopBar topBar;
    private EditText editText;
    private ImageView search,add;
    private LayoutInflater this_inflater;
    private LinearLayout dongtai_linearlayout;
    private EmergencyContactsDB db;
    private RequestQueue requestQueue;
    private Person person;
    private Long add_account;
    private String add_nickname;
    private String add_image;
    private boolean toAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_add);
        toAdd = false;
        init();
    }
    public void init() {
        person = (Person) getApplication();
        //用于发送json request
        requestQueue = new Volley().newRequestQueue(getApplicationContext());
        //获得数据库内容
        db = new EmergencyContactsDB(this);
        //获得当前布局
        this_inflater = LayoutInflater.from(EmergencyContactAdd.this);
        //获得将用来呈现动态布局的linear layout
        dongtai_linearlayout = (LinearLayout) findViewById(R.id.emergency_search_result);

        topBar = (TopBar) findViewById(R.id.topbar_in_emergency_add);
        //设置topbar的监听事件
        topBar.setOnTopbarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() throws UnsupportedEncodingException {
                //右边需不需要隐藏掉呢？
            }
        });
        //edit text
        editText = (EditText) findViewById(R.id.emergency_add_eidt_text);
        //search 按钮的监听
        search = (ImageView) findViewById(R.id.emergency_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAdd = false;
                if (editText.getText().toString().length() != 11) {
                    Toast.makeText(EmergencyContactAdd.this, "format wrong", Toast.LENGTH_SHORT).show();
                    dongtai_linearlayout.removeAllViews();
                } else {
                    String url_add_emergency = "http://120.24.208.130:1503/user/relation_manage";

                    JSONObject map = new JSONObject();
                    try {
                        Log.d("TAG", String.valueOf(Long.MAX_VALUE));
                        map.put("account", Long.parseLong(editText.getText().toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST,
                            "http://120.24.208.130:1503/user/get_information", map,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {

                                    try {
                                        //查看请求成功
                                        if (jsonObject.getInt("status") == 200) {
                                            Log.d("TAG1", jsonObject.toString());
                                            //缓存
                                            add_account = Long.parseLong(editText.getText().toString());
                                            add_nickname = jsonObject.getString("nickname");
                                            //add_image = "";
                                            //动态布局请求结果结果
                                            RelativeLayout layout = (RelativeLayout) this_inflater.inflate(
                                                    R.layout.emergency_add_dongtai, null).findViewById(R.id.emergency_add_dongtai_layout);
                                            //获得Emergency_add_dongtai.xml中第2个控件
                                            TextView lv = (TextView) layout.getChildAt(2);
                                            lv.setText(jsonObject.getString("nickname"));
                                            //获得Emergency_add_dongtai.xml中第0个控件
                                            ImageView im = (ImageView) layout.getChildAt(0);
                                            im.setImageResource(R.mipmap.ic_launcher);//这里需要修改，为服务器传过来的bitmap的图像！！！！
                                            //获得Emergency_add_dongtai.xml中第3个控件，并为他设置监听Log.d("TAG3", jsonObject.toString());
                                            ImageView add = (ImageView) layout.getChildAt(3);
                                            dongtai_linearlayout.removeAllViews();
                                            dongtai_linearlayout.addView(layout);
                                            toAdd = true;
                                            if(toAdd)
                                                add.setOnClickListener(new add_listener());
                                        } else {
                                            //失败，账号不存在
                                            Toast.makeText(EmergencyContactAdd.this, "account doesn't exist", Toast.LENGTH_SHORT).show();
                                            //如果之前动态布局里有上一次的查询结果，需要先remove掉
                                            dongtai_linearlayout.removeAllViews();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //查看请求失败
                            Toast.makeText(EmergencyContactAdd.this, "service connection failed", Toast.LENGTH_SHORT).show();
                            //如果之前动态布局里有上一次的查询结果，需要先remove掉
                            dongtai_linearlayout.removeAllViews();
                        }
                    });
                    requestQueue.add(jsonRequest);
                }

            }
        });

    }
    private class add_listener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //弹出对话框，询问是否确定添加为紧急联系人
            AlertDialog.Builder dialog = new AlertDialog.Builder(EmergencyContactAdd.this);
            dialog.setMessage("是否添加到紧急联系人？");
            dialog.setTitle("提示");
            dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //发出添加请求
                    JSONObject map = new JSONObject();
                    try {
                        map.put("id", person.getId());
                        map.put("user_account", Long.parseLong(editText.getText().toString()));
                        map.put("operation",1);
                        map.put("type", 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST,
                            "http://120.24.208.130:1503/user/relation_manage", map,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    Log.d("TAG", jsonObject.toString());
                                    try {
                                        //添加请求成功
                                        if (jsonObject.getInt("status") == 200) {
                                            Log.d("TAG", "add success");
                                            Toast.makeText(EmergencyContactAdd.this,"add success",Toast.LENGTH_SHORT).show();
                                            //写入本地数据库
                                            db.insert(add_account, add_nickname, add_image);
                                            Intent mIntent = new Intent();
                                            mIntent.putExtra("account", String.valueOf(add_account));
                                            mIntent.putExtra("nickname", add_nickname);
                                            // 设置结果，并进行传送
                                            EmergencyContactAdd.this.setResult(1, mIntent);
                                            finish();
                                        } else {
                                            //失败，账号不存在
                                            Toast.makeText(EmergencyContactAdd.this, "add failed", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //添加请求失败
                            Toast.makeText(EmergencyContactAdd.this, "service connection failed", Toast.LENGTH_SHORT).show();

                        }
                    });
                    requestQueue.add(jsonRequest);
                }
            });
            dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.create().show();
        }
    }
}
