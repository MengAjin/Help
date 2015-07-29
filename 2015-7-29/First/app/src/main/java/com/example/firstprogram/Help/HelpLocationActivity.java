package com.example.firstprogram.Help;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.baidu.mapapi.search.poi.PoiSearch;
import com.example.firstprogram.R;

public class HelpLocationActivity extends Activity {
    private static final String BD_KEY = "2WlpYhe660kYuEIrEBQtLhpL";
    private Button poi_search, location_ok;
    private EditText poi_search_input;
    private MapView mMapView = null;
    private LocationClient mLocationClient;//定位服务的客户端
    private BaiduMap mBaiduMap;
    boolean isFirstLoc = true;// 是否首次定位
    private PoiSearch poiSearch;//poi检索对象
    private MyLocationData locData;//定位数据
    private TextView current_location;//显示当前定位结果的textview
    private BDLocation bdLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.help_location_activity_main);
        current_location = (TextView) findViewById(R.id.help_location_address);
        location_ok = (Button) findViewById(R.id.help_location_address_sure);
        location_ok.setOnClickListener(new locaitonOk_listener());

        mMapView = (MapView) findViewById(R.id.bmapView);

        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //初始化mLocationClient
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(new MyLocationListenner());
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        /* 设置坐标类型
        定位SDK可以返回bd09、bd09ll、gcj02三种类型坐标，
        返回国测局经纬度坐标系：gcj02
        返回百度墨卡托坐标系 ：bd09
        返回百度经纬度坐标系 ：bd09ll
        若需要将定位点的位置通过百度Android地图SDK进行地图展示，
        请返回bd09ll，将无偏差的叠加在百度地图上。
        */
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);//设置扫描间隔，单位是毫秒,当<1000(1s)时，定时定位无效
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();

    }
    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {
        //定位请求回调函数
        @Override
        public void onReceiveLocation(BDLocation location) {
            bdLocation = location;
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            //打印经度纬度测试看看
            Log.d("location","Latitude: "+ String.valueOf(location.getLatitude()));
            Log.d("location","Longitude: "+ String.valueOf(location.getLongitude()));
            Log.d("location", location.getAddrStr());
            current_location.setText(location.getAddrStr());

            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());//LatLing是地理坐标基本数据结构
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);//描述地图状态将要发生的变化
                mBaiduMap.animateMapStatus(u);//以动画方式更新地图状态
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
    //确定按钮的监听函数
    private class locaitonOk_listener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent mIntent = new Intent();
            mIntent.putExtra("latitude", locData.latitude);
            mIntent.putExtra("longitude", locData.longitude);
            mIntent.putExtra("address", bdLocation.getAddrStr());
            // 设置结果，并进行传送
            HelpLocationActivity.this.setResult(1, mIntent);
            finish();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
