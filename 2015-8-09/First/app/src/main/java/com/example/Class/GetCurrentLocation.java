package com.example.Class;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by ������ѿ on 2015/8/6.
 */
public class GetCurrentLocation {
    private LocationManager locationManager;
    //ͨ��ϵͳ����ȡ��LocationManager����
    private String provider=LocationManager.GPS_PROVIDER;
    private Context context;

    public GetCurrentLocation(Context context){
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //this.loctionManager = loctionManager;
        //ע�⣺�˴�����׼ȷ�ȷǳ��ͣ��Ƽ���service��������һ��Thread����run��sleep(10000);Ȼ��ִ��handler.sendMessage(),����λ��
        locationManager.requestLocationUpdates(provider, 5000, 10, locationListener);


    }
    public Location returnLocation(){
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//�߾���
        criteria.setAltitudeRequired(false);//��Ҫ�󺣰�
        criteria.setBearingRequired(false);//��Ҫ��λ
        criteria.setCostAllowed(true);//�����л���
        criteria.setPowerRequirement(Criteria.POWER_LOW);//�͹���
        //�ӿ��õ�λ���ṩ���У�ƥ�����ϱ�׼������ṩ��
        String provider = locationManager.getBestProvider(criteria, true);
        //������һ�α仯��λ��
        return locationManager.getLastKnownLocation(provider);
    }
    //λ�ü�����
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

        //��λ�ñ仯ʱ����
        @Override
        public void onLocationChanged(Location location) {
            //ʹ���µ�location����TextView��ʾ
            Toast.makeText(context, "refresh", Toast.LENGTH_SHORT).show();
            //��λ�ñ仯ʱ��������������µľ�γ��

        }

    };
}
