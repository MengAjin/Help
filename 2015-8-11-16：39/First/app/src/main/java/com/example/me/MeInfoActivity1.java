package com.example.me;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.Class.GetAllParams;
import com.example.Class.Person;
import com.example.firstprogram.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by 漫豆画芽 on 2015/7/21.
 */
public class MeInfoActivity1 extends Activity {
    /*头像上传相关*/
    //余上传并保存头像有关的参数
    private ImageView image;
    SelectPicPopupWindow menuWindow;
    private final static int REQUESTCODE_TAKE = 2;
    private final static int REQUESTCODE_PICK = 3;
    private final static int REQUESTCODE_CUTTING = 4;
    private String urlpath;
    private static String IMAGE_FILE_NAME;
    private ProgressDialog pd;
    private String img;
    ImageUtil imageUtil;
    GetAllParams all;
    private Context mContext;

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
        person = (Person) getApplication();
        /*头像显示相关*/
        IMAGE_FILE_NAME = "temp.jpg";
        image = (ImageView) findViewById(R.id.my_info_avata_imageview);
        String temp1 = Integer.toString(person.getId()) + ".jpg";
        Bitmap bt = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/Temp_help/" + temp1);
        if(bt != null) {
            Drawable drawable = new BitmapDrawable(bt);
            image.setImageDrawable(drawable);
        }

        editNickname = (LinearLayout) findViewById(R.id.my_infor_nick2);
        httpurl_person_info = "http://120.24.208.130:1503/user/get_information";

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
        mContext = MeInfoActivity1.this;
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

        //添加头像点击事件
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuWindow = new SelectPicPopupWindow(MeInfoActivity1.this, itemsOnClick);
                menuWindow.showAtLocation(findViewById(R.id.user_info),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
    }

    /*头像相关*/
    //刷新头像函数，将用户从数据库中获取的图片保存于一个叫Help_user的文件中，并取名为temp.jpg，这个文件夹在用户的系统图库中不显示
    private void uploadImage() {

    }


    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()){
                // 拍照
                case R.id.btn_take_photo:
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //下面这句指定调用相机拍照后的照片存储的路径
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                    startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                    break;
                // 相册选择图片
                case R.id.btn_pick_photo:
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(pickIntent, REQUESTCODE_PICK);
                    break;
                default:
                    break;
            }

        }

    };


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
            case REQUESTCODE_PICK:// 直接从相册获取
                try {
                    startPhotoZoom(data.getData());
                } catch (NullPointerException e) {
                    e.printStackTrace();// 用户点击取消操作
                }
                break;
            case REQUESTCODE_TAKE:// 调用相机拍照
                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                startPhotoZoom(Uri.fromFile(temp));
                break;
            case REQUESTCODE_CUTTING:// 取得裁剪后的图片
                if (data != null) {
                    setPicToView(data);
                }
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
    /**
     * 裁剪图片方法实现
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }
    /**
     * 保存裁剪之后的图片数据
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(null, photo);
            urlpath = FileUtil.saveFile(mContext, photo, person.getId());


            image.setImageDrawable(drawable);
            //将图片由bitmap转换为base64
            imageUtil = new ImageUtil();
            img = imageUtil.bitmapToBase64(photo);
            //新线程后台上传服务端

            pd = ProgressDialog.show(mContext, null, "正在上传图片，请稍候...");
            new Thread(uploadImageRunnable).start();
        }
    }

    /**
     * 使用post表单进行文件上传
     */
    Runnable uploadImageRunnable = new Runnable() {
        @Override
        public void run() {
            JSONObject map = new JSONObject();
            try{
                map.put("id", person.getId());
                map.put("operation", 0);
                map.put("avatar", img);
                //Log.d("Tag", "image ->" + img);
                JSONObject temp = new JSONObject();
                all = new GetAllParams(mContext);
                temp = all.getList("http://120.24.208.130:1503/user/avatar", map, new GetAllParams.VolleyJsonCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        //从数据库中获取用户头像，并保存于指定文件夹中
                        JSONObject obj = new JSONObject();
                        try{
                            obj.put("id", person.getId());
                            obj.put("operation", 1);
                            JSONObject temp = new JSONObject();
                            all = new GetAllParams(mContext);
                            temp = all.getList("http://120.24.208.130:1503/user/avatar", obj, new GetAllParams.VolleyJsonCallback() {
                                @Override
                                public void onSuccess(JSONObject result) {
                                    String data = new String();
                                    try {
                                        data = result.getString("avatar");
                                        imageUtil = new ImageUtil();
                                        Bitmap bmp = imageUtil.base64ToBitmap(data);

                                        Drawable drawable = new BitmapDrawable(bmp);
                                        image.setImageDrawable(drawable);
                                        //替换文件中的头像
                                        File appDir = new File(Environment.getExternalStorageDirectory(), "Temp_help");
                                        //person = new Person();
                                        String fileName1 = Integer.toString(person.getId())+".jpg";
                                        File file = new File(appDir, fileName1);
                                        String path = file.getAbsolutePath();
                                        Log.d("Store path ->", path);
                                        try{
                                            FileOutputStream fos = new FileOutputStream(file);
                                            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                            fos.flush();
                                            fos.close();
                                        } catch (FileNotFoundException e){
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
                                        //Log.d("Tag", "image result ->" + result.toString());
                                        pd.dismiss();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


}

