package com.example.helloword;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

public class HelpLocationActivity extends Activity {
    private static final String BD_KEY = "2WlpYhe660kYuEIrEBQtLhpL";
    private Button poi_search;
    private EditText poi_search_input;
    private MapView mMapView = null;
    private LocationClient mLocationClient;//定位服务的客户端
    private BaiduMap mBaiduMap;
    boolean isFirstLoc = true;// 是否首次定位
    private PoiSearch poiSearch;//poi检索对象
    private MyLocationData locData;//定位数据
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.help_location_activity_main);

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
        //poi检索初始化
        initPoiSearch();
    }

    private void initPoiSearch() {
        poi_search = (Button) findViewById(R.id.poi_search_button);
        poi_search_input = (EditText) findViewById(R.id.poi_search_input_edittext);
        poiSearch = PoiSearch.newInstance();//实例化
        //设置监听函数
        poiSearch.setOnGetPoiSearchResultListener(new PoiListener());

        poi_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置附近检索的参数
                PoiNearbySearchOption nearSearch = new PoiNearbySearchOption();
                nearSearch.keyword(poi_search_input.getText().toString());//检索关键词
                nearSearch.location(new LatLng(locData.latitude, locData.longitude));
                nearSearch.radius(1000);// 检索半径，单位是米
                nearSearch.pageNum(10);
                poiSearch.searchNearby(nearSearch);//发起附近检索
            }
        });

    }
    /*
    * poi检索监听函数*/
    public class PoiListener implements OnGetPoiSearchResultListener{

        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            if (poiResult == null
                    || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
                Toast.makeText(HelpLocationActivity.this, "未找到结果",
                        Toast.LENGTH_LONG).show();
                return;
            }

            if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
                mBaiduMap.clear();
                MyPoiOverlay poiOverlay = new MyPoiOverlay(mBaiduMap);
                poiOverlay.setData(poiResult);// 设置POI数据
                mBaiduMap.setOnMarkerClickListener(poiOverlay);
                poiOverlay.addToMap();// 将所有的overlay添加到地图上
                poiOverlay.zoomToSpan();
                //
                int totalPage = poiResult.getTotalPageNum();// 获取总分页数
                Toast.makeText(
                        HelpLocationActivity.this,
                        "总共查到" + poiResult.getTotalPoiNum() + "个兴趣点, 分为"
                                + totalPage + "页", Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

        }
    }
    /*API中说明onPoiClick(int i)可以覆写，
    这样我们可覆写此方法，
    当点击底图上覆盖物的时候查询POI详细信息。*/
    class MyPoiOverlay extends PoiOverlay {
        public MyPoiOverlay(BaiduMap arg0) {
            super(arg0);
        }
        @Override
        public boolean onPoiClick(int arg0) {
            super.onPoiClick(arg0);
            PoiInfo poiInfo = getPoiResult().getAllPoi().get(arg0);
            // 检索poi详细信息
            poiSearch.searchPoiDetail(new PoiDetailSearchOption()
                    .poiUid(poiInfo.uid));
            return true;
        }
    }
    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {
        //定位请求回调函数
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            //打印经度纬度测试看看
            Log.d("location","Latitude: "+ String.valueOf(location.getLatitude()));
            Log.d("location","Longitude: "+ String.valueOf(location.getLongitude()));
            Log.d("location", location.getAddrStr());

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
