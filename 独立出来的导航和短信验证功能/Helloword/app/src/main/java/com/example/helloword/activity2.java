package com.example.helloword;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.superbearman6.activitycollections.ActivityCollection;
import com.superbearman6.activitycollections.IndicatorInfo;

import java.util.ArrayList;
import java.util.List;


public class activity2 extends ActivityCollection {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setBottomTabBackground(0xff555555);// 设置底部导航背景图
        }
    @Override
    protected boolean isShowWindowFeature() {
            return true;//设置是否显示title;
        }

    @Override
    protected List<IndicatorInfo> setDrawableCollections() {
        List<IndicatorInfo> IndicatorInfos = new ArrayList<IndicatorInfo>();
        IndicatorInfo indicatorInfo_1 = new IndicatorInfo(R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,R.string.hello_world,  12, Color.BLACK,
                new Intent(activity2.this, Activity3.class));
        IndicatorInfo indicatorInfo_2 = new IndicatorInfo(R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,R.string.hello_world,  12, Color.BLACK,
                new Intent(activity2.this, Activity4.class));
        IndicatorInfo indicatorInfo_3 = new IndicatorInfo(R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,R.string.hello_world,  12, Color.BLACK,
                new Intent(activity2.this, Activity4.class));
        IndicatorInfo indicatorInfo_4 = new IndicatorInfo(R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,R.string.hello_world,  12, Color.BLACK,
                new Intent(activity2.this, Activity4.class));
        IndicatorInfo indicatorInfo_5 = new IndicatorInfo(R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,R.string.hello_world,  12, Color.BLACK,
                new Intent(activity2.this, Activity4.class));
        IndicatorInfos.add(indicatorInfo_1);
        IndicatorInfos.add(indicatorInfo_2);
        IndicatorInfos.add(indicatorInfo_3);
        IndicatorInfos.add(indicatorInfo_4);
        IndicatorInfos.add(indicatorInfo_5);
        return IndicatorInfos;
    }
}
