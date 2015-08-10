package com.example.firstprogram.SOS;

import android.content.Context;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.firstprogram.R;

import java.util.List;

/**
 * Created by 漫豆画芽 on 2015/8/3. 该类用于绘制帮客的地理位置
 */
public class DrawIcon{
    private Context context;
    private List<LatLng> latLng;
    private BaiduMap mBaiduMap;
    private BitmapDescriptor bd = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_gcoding);
    private BitmapDescriptor bd2 = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_marka);


    public DrawIcon(Context context, List<LatLng> latLng, BaiduMap mBaiduMap){
        this.context = context;
        this.latLng = latLng;
        this.mBaiduMap = mBaiduMap;

        initOverlay();
    }
    /**
     * 清除所有Overlay
     *
     * @param view
     */
    public void clearOverlay(View view) {
        mBaiduMap.clear();
    }
    /**
     * 重新添加Overlay
     *
     * @param
     */
    public void resetOverlay(List<LatLng> latLng) {
        clearOverlay(null);
        this.latLng = latLng;
        initOverlay();
    }
    public void clearOverlay(){
        clearOverlay(null);
    }

    private void initOverlay() {
        for(int i = 0; i< latLng.size(); i++){
            if(i == 0){
                OverlayOptions ooA = new MarkerOptions().position(latLng.get(i)).icon(bd)
                        .zIndex(9).draggable(true);
                mBaiduMap.addOverlay(ooA);
            }else{
                OverlayOptions ooA = new MarkerOptions().position(latLng.get(i)).icon(bd2)
                        .zIndex(9).draggable(true);
                mBaiduMap.addOverlay(ooA);
            }

        }
    }
}
