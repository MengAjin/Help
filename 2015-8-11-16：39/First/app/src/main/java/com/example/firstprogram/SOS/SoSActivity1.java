package com.example.firstprogram.SOS;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.Class.GetAllParams;
import com.example.Class.Person;
import com.example.firstprogram.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SoSActivity1 extends Activity {
    private TextView countSecond;
    private TimeCount time;
    private Button CancelSos;
    private ImageView safety, health;
    private String defaultMessage;
    private Person person;
    private int userId;
    private long ownPhpne;
    private Context mcontext;
    private SOSContact sc;
    private UploadSOS up;
    private JSONObject obj;
    private JSONObject obj1;
    private JSONObject upEvent;
    private int event_id;
    //private Api api;
    private GetAllParams cl;
    private String[] phoneNums;
    private JSONObject answer;
    private JSONArray list;
    private static String httpurl = "http://120.24.208.130:1503/user/get_relation";
    //获得当前的经纬度
    private double latitude;
    private double longitude;
    //定位相关
    private LocationClient mLocClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sos_activity1);
        //定位
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(new MyLocationListenner());
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        //option.setScanSpan(1000);//发起定位请求的间隔时间
        mLocClient.setLocOption(option);
        mLocClient.start();

        //初始化
        mcontext = SoSActivity1.this;
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
        defaultMessage = "我出事了！！速来救我！！！     来自" + Long.toString(ownPhpne) + "              ------【易键软件】";
        obj1 = new JSONObject();
        try {
            obj1.put("id", userId);
            obj1.put("type", 2);
            obj1.put("content", defaultMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //取消按钮
        CancelSos = (Button) findViewById(R.id.sos1Cancel);
        CancelSos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time.cancel();
                SoSActivity1.this.finish();
            }
        });
        //安全类按钮
        safety = (ImageView) findViewById(R.id.sos1SafeView);
        safety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time.cancel();
                Intent intent = new Intent(SoSActivity1.this, SosSafeActivity.class);
                startActivity(intent);
            }
        });
        //健康类按钮
        health = (ImageView) findViewById(R.id.sos1HealthView);
        health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time.cancel();
                Intent intent = new Intent(SoSActivity1.this, SosHealthActivity.class);
                startActivity(intent);
            }
        });

        //计时
        time = new TimeCount(6000, 1000);
        countSecond = (TextView) findViewById(R.id.sosSecond1);
        time.start();

        countSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SoSActivity1.this, " TextView Click Success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时结束，即用户没有任何点击按钮的操作
        @Override
        public void onFinish() {
            sc = new SOSContact();
            up = new UploadSOS();
            countSecond.setText("0");
            countSecond.setClickable(true);
            phoneNums = new String[20];
            //发送默认信息
            try {
                obj1.put("longitude", longitude);
                obj1.put("latitude", latitude);
                Toast.makeText(SoSActivity1.this, obj1.toString(),Toast.LENGTH_SHORT).show();
                Log.d("sos Activity1", obj1.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sc.sendSMS(obj, defaultMessage,mcontext);
            //上传求救事件
            upEvent = up.addSOS(obj1, mcontext, new UploadSOS.uploadEvent() {
                @Override
                public void onSuccess(int e_id) {
                    event_id = e_id;
                    Log.d("TAG", "event_id -> " + event_id);
                    //跳转到地图页面
                    Intent intent = new Intent();
                    intent.setClass(SoSActivity1.this, SosIngActivity.class);
                    intent.putExtra("event_id", event_id);
                    startActivity(intent);
                    finish();
                }
            });


        }
        @Override
        public void onTick(long millisUntilFinished){//计时过程显示
            countSecond.setClickable(false);
            countSecond.setText(millisUntilFinished /1000+" 秒后发送默认求救信息！");
        }
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null)
                return;
            latitude = location.getLatitude();
            longitude = location.getLongitude();

        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            Toast.makeText(SoSActivity1.this, "请选择“取消求救”", Toast.LENGTH_SHORT).show();
        }

        return false;

    }
}

