package com.example.firstprogram.Help;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.text.SimpleDateFormat;


public class HelpActivity1 extends Activity {

    private EditText dateText, titleText, addressText, address_add,contentText;
    private TextView personText;
    private ImageView location_imageview;
    private double latitude, longitude;
    private RequestQueue requestQueue;
    private Person person;
    public void initialize(){
        person = (Person) getApplication();
        dateText = (EditText) findViewById(R.id.helpDate);
        titleText = (EditText) findViewById(R.id.helpTitle);
        personText = (TextView) findViewById(R.id.helpNumOfPerson);
        addressText = (EditText) findViewById(R.id.helpAddress);
        contentText = (EditText) findViewById(R.id.helpContent);
        location_imageview = (ImageView) findViewById(R.id.help_location);
        address_add = (EditText) findViewById(R.id.address_edit);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_activity1);
        initialize();

        TopBar topbar = (TopBar) findViewById(R.id.topBar);
        topbar.setOnTopbarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }
            //提交按钮的监听
            @Override
            public void rightClick() throws UnsupportedEncodingException {
                requestQueue = new Volley().newRequestQueue(HelpActivity1.this);//实例化请求队列
                JSONObject map = new JSONObject();
                try {//添加参数
                    map.put("id", person.getId());
                    map.put("type", 1);
                    map.put("max_people", Integer.parseInt(personText.getText().toString()));
                    map.put("content", titleText.getText().toString() +"\n" +contentText.getText().toString());
                    map.put("longitude", longitude);
                    map.put("latitude", latitude);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //实例化一个json请求对象,五个参数
                JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST,
                        "http://120.24.208.130:1503/event/add",
                        map,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                //请求成功要做的事
                                Log.d("TAG", jsonObject.toString());
                                try {
                                    //请求失败
                                    if(jsonObject.getInt("status") == 500){
                                        Toast.makeText(HelpActivity1.this, "failed 500", Toast.LENGTH_SHORT).show();
                                    } else if (jsonObject.getInt("status") == 200){
                                        Toast.makeText(HelpActivity1.this, "求助提交成功，可到“我发出的”列表查看", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(HelpActivity1.this, "connection failed", Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(jsonRequest);
            }
        });
        //日期选择器监听
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the current system date
                SimpleDateFormat currentDate=new SimpleDateFormat("yyyy");
                int year= Integer.parseInt(currentDate.format(new java.util.Date()));
                currentDate=new SimpleDateFormat("MM");
                int mouth= Integer.parseInt(currentDate.format(new java.util.Date()));
                currentDate=new SimpleDateFormat("dd");
                int day= Integer.parseInt(currentDate.format(new java.util.Date()));
                //put out a date picker dialog
                final DatePickerDialog datePicker = new DatePickerDialog(HelpActivity1.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                monthOfYear += 1;
                                dateText.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
                                //Toast.makeText(HelpActivity1. this, year+"-"+monthOfYear+"-"+dayOfMonth, Toast.LENGTH_SHORT).show();
                            }
                        },year,mouth-1,day);
                datePicker.show();
            }
        });
        //自动定位添加监听事件
        location_imageview.setOnClickListener(new location_listener());
    }
    //定位监听事件
    private class location_listener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(HelpActivity1.this, GeoCoderDemo.class);
            startActivityForResult(intent, 1);
        }
    }
    //重写onActivityResult函数，处理定位返回的结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                addressText.setText(data.getStringExtra("address"));
                latitude = data.getDoubleExtra("latitude", 0);
                longitude = data.getDoubleExtra("longitude", 0);
                address_add.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
    /*普通求助人数加或减*/
    public void person_num_listener(View view){
        if(view.getId() == R.id.add){
            int i = Integer.parseInt(personText.getText().toString());
            personText.setText(String.valueOf(i+1));
        }
        if(view.getId() == R.id.subtract){
            int i = Integer.parseInt(personText.getText().toString());
            if(i>1){
                personText.setText(String.valueOf(i-1));
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
