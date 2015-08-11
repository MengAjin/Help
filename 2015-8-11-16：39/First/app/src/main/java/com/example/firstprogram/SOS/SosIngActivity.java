package com.example.firstprogram.SOS;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.Class.GetAllParams;
import com.example.Class.Person;
import com.example.evaluate.EvaluateMainActivity;
import com.example.firstprogram.R;
import com.example.firstprogram.TopBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SosIngActivity extends Activity {
    private TextView textview;
    private TopBar topbar;
    private String url;
    private int event_id;
    private Person person;
    private GetAllParams c;
    private JSONObject answer;
    //private JSONObject obj;

    //地图显示相关
    private BaiduMap mBaiduMap;
    private MapView mMapView;

    //帮客们的坐标
    private List<LatLng> support_latlng_list = new ArrayList<>();
    //绘制帮客的坐标
    private DrawIcon drawIcon;

    //定位相关
    boolean isFirstLoc = true;
    private LocationClient mLocClient;

    //定时更新相关
    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            //要做的事情
            //Toast.makeText(SosIngActivity.this, "refresh", Toast.LENGTH_SHORT).show();
            refresh_support_location();
            handler.postDelayed(this, 5000);
        }
    };
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.sos_ing_activity);
        intent = getIntent();
        //初始化
        url = "http://120.24.208.130:1503/event/modify";
        person = (Person) getApplication();
        Intent intent = getIntent();  //获取上一个activity的intent里的事件ID“event_id"
        event_id =  intent.getIntExtra("event_id",-1);

        init();
        topbar_setting();


        //地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        //定位相关
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(new MyLocationListenner());
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);//发起定位请求的间隔时间
        mLocClient.setLocOption(option);
        mLocClient.start();

        //模拟着一组帮客坐标
        //support_latlng_list =  getSupportLalngList();
        drawIcon = new DrawIcon(this, support_latlng_list, mBaiduMap);

        //启动计时器
        handler.postDelayed(runnable, 5000);

    }

    private void init() {
        textview = (TextView) findViewById(R.id.textview1);
        c = new GetAllParams(this);
    }

    private void refresh_support_location(){
        JSONObject param = new JSONObject();
        try {
            param.put("event_id",intent.getIntExtra("event_id",0));
            param.put("id", person.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        c.getList("http://120.24.208.130:1503/event/get_supporter", param,
                new GetAllParams.VolleyJsonCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            if (result.getInt("status") == 200) {
                                JSONArray array = result.getJSONArray("user_account");
                                List<LatLng> temp1 = new ArrayList<>();
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject temp = (JSONObject) array.get(i);
                                    LatLng l = new LatLng(temp.getDouble("latitude"), temp.getDouble("longitude"));
                                    temp1.add(l);
                                }
                                drawIcon.resetOverlay(temp1);
                                textview.setText("已有" + temp1.size() + "人赶来救助");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    /*对topbar的设置*/
    private void topbar_setting(){
        //setOnTopbarClickListener
        topbar = (TopBar) findViewById(R.id.topbar_in_sosing);
        topbar.setOnTopbarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {//点击中止求救
                AlertDialog.Builder dialog = new AlertDialog.Builder(SosIngActivity.this);
                dialog.setMessage("确认中止求救吗？您将会被扣除1个积分！");
                dialog.setTitle("提示");
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        /////////////////////////////////////////////////
                        //向后台发送 中止求救  请求，把事件状态改为 “1”--结束状态
                        JSONObject map = new JSONObject();
                        c = new GetAllParams(SosIngActivity.this);
                        JSONObject temp = new JSONObject();
                        try {
                            map.put("id", person.getId());//用户的id
                            map.put("event_id",event_id);//事件的id
                            map.put("state",1);//事件的状态，0是进行中，1是结束状态
                            map.put("type",0);//操作类型，0是中止求救，1是完成求救
                            //下面是向后台发送请求
                            temp = c.getList(url, map, new GetAllParams.VolleyJsonCallback() {
                                @Override
                                public void onSuccess(JSONObject result) {
                                    //answer = result;
                                    Toast.makeText(SosIngActivity.this, "求救已中止", Toast.LENGTH_SHORT).show();
                                    SosIngActivity.this.finish();//结束”求救中“页面
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                dialog.create().show();
            }

            @Override
            public void rightClick() throws UnsupportedEncodingException {
                Toast.makeText(SosIngActivity.this, " next UI", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder dialog = new AlertDialog.Builder(SosIngActivity.this);
                dialog.setMessage("确认完成求救吗？");
                dialog.setTitle("提示");
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

///////////////////////////////////////////////////////////////////////
// 向后台发送 完成求救  请求，把事件状态改为 “1”--结束状态
                        //跟上面差不多
                        JSONObject map = new JSONObject();
                        c = new GetAllParams(SosIngActivity.this);
                        JSONObject temp = new JSONObject();
                        answer = new JSONObject();
                        try {
                            map.put("id", person.getId());
                            map.put("event_id",event_id);
                            map.put("state",1);
                            map.put("type",1);

                            temp = c.getList(url, map, new GetAllParams.VolleyJsonCallback() {
                                @Override
                                public void onSuccess(JSONObject result) {
                                    answer = result;
                                    Toast.makeText(SosIngActivity.this, "求救已完成", Toast.LENGTH_SHORT).show();
                                    //SosIngActivity.this.finish();//结束”求救中“页面
                                    Intent intent = new Intent(SosIngActivity.this, EvaluateMainActivity.class);
                                    intent.putExtra("event_id", event_id);
                                    startActivity(intent);//跳转到下一页
                                    finish();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                dialog.create().show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        handler.removeCallbacks(runnable);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            Toast.makeText(SosIngActivity.this, "请选择“中止求救”或“完成求救”", Toast.LENGTH_SHORT).show();
        }

        return false;

    }
}
