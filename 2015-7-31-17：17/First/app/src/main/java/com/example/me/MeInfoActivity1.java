package com.example.me;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import com.example.Class.GetAllParams;
import com.example.Class.Person;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by 漫豆画芽 on 2015/7/21.
 */
public class MeInfoActivity1 extends Activity {

    private GetAllParams c1;
    private GetAllParams c2;
    private Context mcontext1;
    private Context mcontext2;
    private JSONObject anwser1;
    private JSONObject anwser2;
    private JSONArray list1;
    private JSONArray list2;
    private static String httpurl = "http://120.24.208.130:1503/health/query";
    private static String httpur2 = "http://120.24.208.130:1503/illness/query";
    private JSONObject obj1;
    private JSONObject obj2;
     double height1 ;
     double weight1 ;
     String medical_record1;
    private int userId;
    private String httpurl_person_info;
    private TextView nick, gender, age,occupation,location,height,weight,medical_record;
    private RequestQueue requestQueue;
    private Person person;
    private LinearLayout editNickname, editGender, editAge,editoccupation,editlocation,editheight,editweight,editmedical_record;
    private void init() {
        editNickname = (LinearLayout) findViewById(R.id.my_infor_nick2);
        httpurl_person_info = "http://120.24.208.130:1503/user/get_information";
        person = (Person) getApplication();
        //nick
        nick = (TextView) findViewById(R.id.my_infor_nick2_textview);
        nick.setText(person.getNick());

       // requestQueue = Volley.newRequestQueue(getApplicationContext());
        //gender
        gender = (TextView) findViewById(R.id.my_infor_gender_textview);
        Log.d("TAG","person.gender      "+person.getGender());
        if (person.getGender() == 0) {
            gender.setText("女");
        } else if (person.getGender() == 1) {
            gender.setText("男");
        }


        //gender
        editGender = (LinearLayout) findViewById(R.id.my_infor_gender);
        //age
        Log.d("TAG", "person.age     " + person.getAge());
        age = (TextView) findViewById(R.id.my_infor_age_textview);
        age.setText(String.valueOf(person.getAge()));

        occupation = (TextView)findViewById(R.id.my_infor_occupation_textview);
        if (person.getOccupation() == 0){
            occupation.setText("学生");
        }else if (person.getOccupation() == 1){
            occupation.setText("教师");
        }
        else if (person.getOccupation() == 2){
            occupation.setText("医生");
        }
        else if (person.getOccupation() == 3){
            occupation.setText("程序员");
        }
        else if (person.getOccupation() == 4){
            occupation.setText("其他");
        }
       // occupation.setText(String.valueOf(person.getOccupation()));
        location = (TextView) findViewById(R.id.my_infor_location_textview);
        location.setText(person.getLocation());
        height =(TextView)findViewById(R.id.my_infor_height_textview);

        weight = (TextView) findViewById(R.id.my_infor_weight_textview);

        medical_record = (TextView) findViewById(R.id.my_infor_medical_record_textview);

        editAge = (LinearLayout) findViewById(R.id.my_infor_age);
        editoccupation = (LinearLayout)findViewById(R.id.my_infor_occupation);
        editlocation = (LinearLayout) findViewById(R.id.my_infor_location);
        editheight = (LinearLayout) findViewById(R.id.my_infor_height);
        editweight = (LinearLayout) findViewById(R.id.my_infor_weight);
        editmedical_record = (LinearLayout) findViewById(R.id.my_infor_medical_record);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_1_myinfo_activity);
        init();
        mcontext1 = MeInfoActivity1.this;
        mcontext2 = MeInfoActivity1.this;
        userId = person.getId();
        // 查询用户身体数据中的身高和体重
        JSONObject temp1 = new JSONObject();
        anwser1 = new JSONObject();
        c1 = new GetAllParams(mcontext1);
        obj1 = new JSONObject();
        try {
            obj1.put("id", userId); //  查询用户身体数据中的身高和体重
            temp1 = c1.getList(httpurl, obj1, new GetAllParams.VolleyJsonCallback() {
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

//查询用户病历
        JSONObject temp2 = new JSONObject();
        anwser2 = new JSONObject();
        c2 = new GetAllParams(mcontext2);
        obj2 = new JSONObject();
        try {
            obj2.put("id", userId); //  查询用户病历
            temp2 = c2.getList(httpur2, obj1, new GetAllParams.VolleyJsonCallback() {
                @Override
                public void onSuccess(JSONObject result) {
                    Log.d("TAG", "result -> " + result.toString());
                    anwser2 = result;
                    //Log.d("TAG", "list -> " + list.toString());
                    if(anwser2 != null ) {
                        try {
                            list2 = new JSONArray();
                            list2 = anwser2.getJSONArray("illness_list");
                            JSONObject jo3 = (JSONObject) list2.get(0);
                            medical_record1 = jo3.getString("content");//获取病历值
                            Log.d("TAG", "AAAAAAAAAAAAAAAAAAAA");
                            medical_record.setText(medical_record1);
                            Log.d("TAG","medical_record1     "+medical_record1);
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
//        height.setText(String.valueOf(height1));
//        Log.d("TAG", "height set error" + height1);
//        weight.setText(String.valueOf(weight1));
//        Log.d("TAG", "weight set error" + weight1);
//        medical_record.setText(medical_record1);
//        Log.d("TAG", "weight set error" + medical_record1);
        editNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeInfoActivity1.this, MeInfoEditNicknameActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        editGender.setOnClickListener(new modify_gender_listener());
        editAge.setOnClickListener(new modify_age_listener());
        editoccupation.setOnClickListener(new modify_occupation_listener());
        editlocation.setOnClickListener(new modify_location_listener());
        editheight.setOnClickListener(new modify_height_listener());
        editweight.setOnClickListener(new modify_weight_listener());
        editmedical_record.setOnClickListener(new modify_medical_record_listener());

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
                                            Log.d("TAGAGE",et.getText().toString());
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
                    builder.setItems(cities, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, final int which) {
                            Toast.makeText(MeInfoActivity1.this, "选择的性别为：" + cities[which], Toast.LENGTH_SHORT).show();
                            if (which == person.getGender()) {

                            } else {
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
                                                    if (jsonObject.toString().contains("id")) {
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






    public class modify_location_listener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            final EditText et = new EditText(MeInfoActivity1.this);
            AlertDialog.Builder builder = new AlertDialog.Builder(MeInfoActivity1.this);
            builder.setTitle("请输入住址：");
            builder.setView(et);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    RequestQueue requestQueue = new Volley().newRequestQueue(MeInfoActivity1.this);
                    JSONObject map = new JSONObject();
                    try {
                        map.put("id",person.getId());
                        map.put("location", et.getText().toString());
                        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST,
                                "http://120.24.208.130:1503/user/modify_information", map,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject jsonObject) {
                                        if(jsonObject.toString().contains("id")){
                                            person.setLocation(et.getText().toString());
                                            location.setText(et.getText().toString());
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



    public class modify_height_listener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            final EditText et = new EditText(MeInfoActivity1.this);
            AlertDialog.Builder builder = new AlertDialog.Builder(MeInfoActivity1.this);
            builder.setTitle("请输入身高：");
            builder.setView(et);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    RequestQueue requestQueue = new Volley().newRequestQueue(MeInfoActivity1.this);
                    JSONObject map = new JSONObject();
                    try {
                        map.put("id",person.getId());
                        map.put("value", Double.parseDouble(et.getText().toString()));
                        map.put("type",3);
                        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST,
                                "http://120.24.208.130:1503/health/upload", map,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject jsonObject) {
                                        if(jsonObject.toString().contains("id")){
                                           // person.setAge(Integer.parseInt(et.getText().toString()));
                                            height.setText(et.getText().toString());
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




    public class modify_weight_listener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            final EditText et = new EditText(MeInfoActivity1.this);
            AlertDialog.Builder builder = new AlertDialog.Builder(MeInfoActivity1.this);
            builder.setTitle("请输入体重：");
            builder.setView(et);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    RequestQueue requestQueue = new Volley().newRequestQueue(MeInfoActivity1.this);
                    JSONObject map = new JSONObject();
                    try {
                        map.put("id",person.getId());
                        map.put("value", Double.parseDouble(et.getText().toString()));
                        map.put("type",4);
                        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST,
                                "http://120.24.208.130:1503/health/upload", map,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject jsonObject) {
                                        if(jsonObject.toString().contains("id")){
                                            // person.setAge(Integer.parseInt(et.getText().toString()));
                                            weight.setText(et.getText().toString());
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




    public class modify_medical_record_listener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            final EditText et = new EditText(MeInfoActivity1.this);
            AlertDialog.Builder builder = new AlertDialog.Builder(MeInfoActivity1.this);
            builder.setTitle("请输入病历：");
            builder.setView(et);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    RequestQueue requestQueue = new Volley().newRequestQueue(MeInfoActivity1.this);
                    JSONObject map = new JSONObject();
                    try {
                        map.put("id",person.getId());
                        map.put("content",et.getText().toString());
                        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST,
                                "http://120.24.208.130:1503/illness/upload", map,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject jsonObject) {

                                        if(jsonObject.toString().contains("id")){
                                            Log.d("TAG","AAAAAAAAAAAAAAAA");
                                            // person.setAge(Integer.parseInt(et.getText().toString()));
                                            medical_record.setText(et.getText().toString());
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



    public class modify_occupation_listener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            editoccupation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MeInfoActivity1.this);
                    builder.setTitle("选择职业");
                    //    指定下拉列表的显示数据
                    final String[] sort = {"学生", "教师","医生","程序员","其他"};
                    //    设置一个下拉的列表选择项
                    builder.setItems(sort, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, final int which) {
                            Toast.makeText(MeInfoActivity1.this, "选择的职业为：" + sort[which], Toast.LENGTH_SHORT).show();
                            if (which == person.getOccupation()) {

                            } else {
                                RequestQueue requestQueue = new Volley().newRequestQueue(MeInfoActivity1.this);
                                JSONObject map = new JSONObject();
                                try {
                                    map.put("id", person.getId());
                                    map.put("occupation", which);
                                    JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST,
                                            "http://120.24.208.130:1503/user/modify_information", map,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject jsonObject) {
                                                    Log.d("Tag", jsonObject.toString());
                                                    if (jsonObject.toString().contains("id")) {
                                                        person.setOccupation(which);
                                                        if (person.getOccupation() == 0){
                                                            occupation.setText("学生");
                                                        }else if (person.getOccupation() == 1){
                                                            occupation.setText("教师");
                                                        }
                                                        else if (person.getOccupation() == 2){
                                                            occupation.setText("医生");
                                                        }
                                                        else if (person.getOccupation() == 3){
                                                            occupation.setText("程序员");
                                                        }
                                                        else if (person.getOccupation() == 4){
                                                            occupation.setText("其他");
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
        switch (requestCode) {
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
            MeInfoActivity1.this.setResult(0, mIntent);
            finish();
        }
        return false;
    }
}

