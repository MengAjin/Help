package com.example.firstprogram.SOS;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.Class.GetCurrentLocation;
import com.example.firstprogram.R;
import com.example.Class.Person;

import org.json.JSONObject;


public class SosSafeActivity extends Activity {
    private TextView countSecond;
    private TimeCount time;
    private Button send;
    private CheckBox ind1, ind2, ind3;
    private String event;
    private String message;
    private static String defaultMessage;
    private Person person;
    private int userId;
    private long ownPhpne;
    private UploadSOS up = new UploadSOS();
    private SOSContact sc;
    private JSONObject obj;
    private JSONObject obj1;
    private Context mcontext;
    private JSONObject upEvent = new JSONObject() ;
    private int event_id;
    //定位相关
    private Location cur_loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sos_safe_activity);
        //初始化
        cur_loc = getLocation();
        person = (Person) getApplication();
        userId = person.getId();
        ownPhpne = person.getAccount();
        obj = new JSONObject();
        try {
            obj.put("id", userId);
            obj.put("type", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mcontext = SosSafeActivity.this;
        message = null;
        defaultMessage = "突发事故！现在几乎丧失行动能力！！速来救我！！";

        //Time
        time = new TimeCount(7000, 1000);
        countSecond = (TextView) findViewById(R.id.sos_safe_second);
        time.start();
        //checkboxs
        ind1 = (CheckBox) findViewById(R.id.safe_checkBox1);
        ind2 = (CheckBox) findViewById(R.id.safe_checkBox2);
        ind3 = (CheckBox) findViewById(R.id.safe_checkBox3);
        //button
        send = (Button) findViewById(R.id.sos_safe_send);
    }

    //监听checkbox和button点击事件
    public void  onClick(View v) {
        event = null;
        message = "突发疾病！速来救我！！疾病类型为:";
        switch (v.getId()) {
            case R.id.safe_checkBox1:
                event = "打劫";
                message = message + event;
                break;
            case R.id.safe_checkBox2:
                event = "车祸";
                message = message + event;
                break;
            case R.id.safe_checkBox3:
                event = "火灾";
                message = message + event;
                break;
            case R.id.sos_safe_send:
                time.cancel();
                //发送短信
                sc.sendSMS(obj, message, mcontext);
                //上传求救事件
                obj1 = new JSONObject();
                try {
                    obj1.put("id", userId);
                    obj1.put("type", 2);
                    obj1.put("content", message);
                    obj1.put("longitude", cur_loc.getLongitude());
                    obj1.put("latitude", cur_loc.getLatitude());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                upEvent = up.addSOS(obj1, mcontext, new UploadSOS.uploadEvent() {
                    @Override
                    public void onSuccess(int e_id) {
                        event_id = e_id;
                        //跳转到地图页面
                        Intent intent = new Intent();
                        intent.setClass(SosSafeActivity.this, SosIngActivity.class);
                        intent.putExtra("event_id", event_id);
                        startActivity(intent);
                    }
                });
        }
    }

    // TimeCount
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onFinish() {
            //countSecond.setText("时间到");
            sc = new SOSContact();
            countSecond.setClickable(true);
            //发生默认短信
            sc.sendSMS(obj, defaultMessage, mcontext);
            //上传求救事件
            obj1 = new JSONObject();
            try {
                obj1.put("id", userId);
                obj1.put("type", 2);
                obj1.put("content", defaultMessage);
                obj1.put("longitude", cur_loc.getLongitude());
                obj1.put("latitude", cur_loc.getLatitude());
            } catch (Exception e) {
                e.printStackTrace();
            }
            upEvent = up.addSOS(obj1, mcontext, new UploadSOS.uploadEvent() {
                @Override
                public void onSuccess(int e_id) {
                    event_id = e_id;
                    //跳转到地图页面
                    Intent intent = new Intent();
                    intent.setClass(SosSafeActivity.this, SosIngActivity.class);
                    intent.putExtra("event_id", event_id);
                    startActivity(intent);
                }
            });;
            //跳转
            Intent intent = new Intent(SosSafeActivity.this, SosIngActivity.class);
            startActivity(intent);
        }
        @Override
        public void onTick(long millisUntilFinished){
            countSecond.setClickable(false);
            countSecond.setText(millisUntilFinished /1000+" ");
        }
    }

    /*获得当前经纬度*/
    private Location getLocation(){
        GetCurrentLocation getCurrentLocation ;
        getCurrentLocation  = new GetCurrentLocation(this);
        return getCurrentLocation.returnLocation();
    }
}
