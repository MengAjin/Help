package com.example.contactList;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.Class.GetAllParams;
import com.example.Class.Person;
import com.example.firstprogram.R;
import com.example.firstprogram.TopBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by lenovo on 2015/8/4.
 */
public class CheckEmergencyContactActivity extends Activity {
    private String string,string1;
    private TopBar topBar;
    private Person person;
    private GetAllParams c1,c2;
    private Context mcontext1;
    private JSONObject obj1,obj2;
    private JSONArray list1;
    double height1 ;
    private int type;
    double weight1 ;
    private RequestQueue requestQueue;
    private TextView height,weight,gender,age,occupation,location,nick;
    private Button delete_;
    Long account1;
    private static String httpurl = "http://120.24.208.130:1503/user/get_information";
    private static String httpur2 = "http://120.24.208.130:1503/user/relation_manage";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_contact_activity);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        string = bundle.getString("account");
        person = (Person) getApplication();
        account1=Long.parseLong(string);
        Log.d("TAG","have you get the right account?"+account1);
        TextView textView = (TextView) (findViewById(R.id.my_infor_phone_textview));
        textView.setText(string);

        height =(TextView)findViewById(R.id.my_infor_height_textview);
        weight = (TextView) findViewById(R.id.my_infor_weight_textview);

        location = (TextView) findViewById(R.id.my_infor_location_textview);

        age = (TextView) findViewById(R.id.my_infor_age_textview);

        nick = (TextView) findViewById(R.id.my_infor_nick2_textview);

        occupation = (TextView)findViewById(R.id.my_infor_occupation_textview);

        delete_ = (Button) findViewById(R.id.delete);

        gender = (TextView) findViewById(R.id.my_infor_gender_textview);
        // Log.d("TAG","person.gender      "+person.getGender());
        //设置topbar点击事件
        topBar = (TopBar) findViewById(R.id.topbar_in_check_contact_activity);
        topBar.setOnTopbarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() throws UnsupportedEncodingException {

            }
        });
        //设置删除好友点击事件
        delete_.setOnClickListener(new delete_listener());




        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject map = new JSONObject();
        try {
            Log.d("TAG","account1111111111111111111111"+account1);
            map.put("account", account1);

            JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST,
                    httpurl, map, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.d("TAG","JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ"+jsonObject.toString());
                    try {
                        //获得住址
                        if(jsonObject.getString("status") != "500") {
                            if (jsonObject.getString("location") != null) {
                                Log.d("TAG", "location exsit?" + jsonObject.getString("location"));
                                location.setText(jsonObject.getString("location"));
                            }
                            //获得昵称
                            Log.d("TAG","nickkkkkkkkkkkkkkkkkkkkkkkkkk"+jsonObject.getString("nickname"));
                            nick.setText(jsonObject.getString("nickname"));
                            //获得年纪
                            if(String.valueOf(jsonObject.getInt("age"))!=null) {
                                Log.d("TAG", "ageeeeeeeeeeeeeeeeeeeeeeeeeeeeee" + jsonObject.getInt("age"));
                                age.setText(String.valueOf(jsonObject.getInt("age")));
                            }
                            else
                                age.setText("0");
                            //获得职业
                            if(String.valueOf(jsonObject.getInt("occupation"))!=null) {
                                Log.d("TAG", "occupationnnnnnnnnnnnnnnnnnnnnnnnnnnn" + jsonObject.getInt("occupation"));
                                if (jsonObject.getInt("occupation") == 0) {
                                    occupation.setText("学生");
                                } else if (jsonObject.getInt("occupation") == 1) {
                                    occupation.setText("教师");
                                } else if (jsonObject.getInt("occupation") == 2) {
                                    occupation.setText("医生");
                                } else if (jsonObject.getInt("occupation") == 3) {
                                    occupation.setText("程序员");
                                } else {
                                    occupation.setText("其他");
                                }
                                Log.d("TAG", "have occupation set successly?");
                            }

                            //获得性别
                            Log.d("TAG","genderrrrrrrrrrrrrrrrrrrrrrrrrrrr"+jsonObject.getInt("gender"));
                            if (String.valueOf(jsonObject.getInt("gender")) != null) {

                                if (jsonObject.getInt("gender") == 0) {
                                    gender.setText("女");
                                } else {
                                    gender.setText("男");
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("TAG", volleyError.toString());
                }
            });

            requestQueue.add(jsonRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }


//        JSONObject temp1 = new JSONObject();
//        anwser1 = new JSONObject();
//        c1 = new GetAllParams(mcontext1);
//        obj1 = new JSONObject();
//        try {
//            obj1.put("account",account1);
//            Log.d("TAG", "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTT" + account1);
//            temp1 = c1.getList(httpurl, obj1, new GetAllParams.VolleyJsonCallback() {
//               // Log.d("TAGG","HAVE YOU ENTERED?");
//                @Override
//
//                public void onSuccess(JSONObject result) {
//
//                    Log.d("TAG", "result ----------------------------------------------------> " + result.toString());
//                    anwser1 = result;
//                    try {
//                        //获得住址
//                        location.setText(anwser1.getString("location"));
//                        //获得职业
//                        if (anwser1.getInt("occupation") == 0){
//                            occupation.setText("学生");
//                        }else if (anwser1.getInt("occupation") == 1){
//                            occupation.setText("教师");
//                        }
//                        else if (anwser1.getInt("occupation") == 2){
//                            occupation.setText("医生");
//                        }
//                        else if (anwser1.getInt("occupation") == 3){
//                            occupation.setText("程序员");
//                        }
//                        else if (anwser1.getInt("occupation") == 4){
//                            occupation.setText("其他");
//                        }
//                        //获得昵称
//                        nick.setText(anwser1.getString("nick"));
//                        //获得年纪
//                        age.setText(anwser1.getInt("age"));
//                        //获得性别
//                        if (anwser1.getInt("gender") == 0) {
//                            gender.setText("女");
//                        } else if (anwser1.getInt("gender") == 1) {
//                            gender.setText("男");
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }



      /*  // 查询用户身体数据中的身高和体重
        JSONObject temp2 = new JSONObject();
        anwser2 = new JSONObject();
        c2 = new GetAllParams(mcontext2);
        obj2 = new JSONObject();
        try {
            obj1.put("id", ); //  查询用户身体数据中的身高和体重
            temp1 = c2.getList(httpurl, obj2, new GetAllParams.VolleyJsonCallback() {
                @Override
                public void onSuccess(JSONObject result) {
                    Log.d("TAG", "result -> " + result.toString());
                    anwser1 = result;
                    //Log.d("TAG", "list -> " + list.toString());
                    if(anwser1 != null ) {
                        try {
                            list1 = new JSONArray();
                            list1 = anwser1.getJSONArray("health_list");
                            //获取health_list中身高最近的一条记录
                            for (int i = 0; i < list1.length() - 1 ;  i++) {
                                JSONObject jo1 = (JSONObject) list1.get(i);
                                if(jo1.getInt("type") == 3) {  //type 3 表示身高
                                    height1 = jo1.getDouble("value");//获取身高

                                    Log.d("TAG","height1    "+jo1.getDouble("value"));
                                    Log.d("TAG", "height1    " + height1);
                                    height.setText(String.valueOf(height1));
                                    Log.d("TAG","HHHHHHHHHHHHHHHH");
                                    break;
                                }
                            }
                            //获取health_list中体重最近的一条记录
                            for (int i = 0; i < list1.length() - 1 ;  i++) {
                                JSONObject jo2 = (JSONObject) list1.get(i);
                                if(jo2.getInt("type") == 4) {  //type 4 表示体重
                                    weight1 = jo2.getDouble("value");//获取体重
                                    Log.d("TAG","WWWWWWWWWWWWWWWW");
                                    Log.d("TAG", "weight1    " + weight1);
                                    weight.setText(String.valueOf(weight1));
                                    break;
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    else {  //没有修改记录
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

*/


    }
    //删除按钮的点击事件函数
    private class delete_listener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //弹出对话框，询问是否确定添加为好友
            AlertDialog.Builder dialog = new AlertDialog.Builder(CheckEmergencyContactActivity.this);
            dialog.setMessage("是否删除好友？");
            dialog.setTitle("提示");
            dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    //发出添加请求
                    JSONObject map1 = new JSONObject();
                    Log.d("TAG","account!!!!!!!!!!!!!!!!!"+account1);
                    Log.d("TAG","person.getid success?"+person.getId());
                    try {

                        map1.put("id", person.getId());
                        map1.put("user_account", account1);
                        map1.put("operation",0);
                        map1.put("type",0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST,httpur2 , map1, new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject jsonObject1) {
                            Log.d("TAG", jsonObject1.toString());
                            try {
                                Log.d("TAG","delete success????????????????"+jsonObject1.getInt("status"));
                                if (jsonObject1.getInt("status") == 200) {
                                    Log.d("TAG", "delete success");
                                    Toast.makeText(CheckEmergencyContactActivity.this, "delete success", Toast.LENGTH_SHORT).show();
                                    //写入本地数据库
                                    // db.insert(add_account, add_nickname, add_image);
//                                            Intent mIntent = new Intent();
//                                            mIntent.putExtra("account", String.valueOf(add_account));
//                                            mIntent.putExtra("nickname", add_nickname);
                                    // 设置结果，并进行传送
                                    //  CheckContactActivity.this.setResult(1, mIntent);
                                    finish();
                                } else {

                                    Toast.makeText(CheckEmergencyContactActivity.this, "delete failed", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //添加请求失败
                            Toast.makeText(CheckEmergencyContactActivity.this, "service connection failed", Toast.LENGTH_SHORT).show();

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
