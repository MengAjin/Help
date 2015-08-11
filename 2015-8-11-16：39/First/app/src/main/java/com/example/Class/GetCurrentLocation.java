package com.example.Class;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by 漫豆画芽 on 2015/8/6.
 */
public class GetCurrentLocation {
    private LocationManager locationManager;
    //通过系统服务，取得LocationManager对象
    private String provider=LocationManager.GPS_PROVIDER;
    private Context context;

    public GetCurrentLocation(Context context){
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //this.loctionManager = loctionManager;
        //注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置
        locationManager.requestLocationUpdates(provider, 5000, 10, locationListener);


    }
    public Location returnLocation(){
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//高精度
        criteria.setAltitudeRequired(false);//不要求海拔
        criteria.setBearingRequired(false);//不要求方位
        criteria.setCostAllowed(true);//允许有花费
        criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗
        //从可用的位置提供器中，匹配以上标准的最佳提供器
        String provider = locationManager.getBestProvider(criteria, true);
        //获得最后一次变化的位置
        return locationManager.getLastKnownLocation(provider);
    }
    //位置监听器
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        //当位置变化时触发
        @Override
        public void onLocationChanged(Location location) {
            //使用新的location更新TextView显示
            Toast.makeText(context, "refresh", Toast.LENGTH_SHORT).show();
            //当位置变化时，向服务器发送新的经纬度

        }

    };
}
