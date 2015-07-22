package com.example.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.firstprogram.FirstActivity;
import com.example.firstprogram.R;
import com.example.firstprogram.contact_list.ContactListActivity;

import com.example.me.Me1Activity;
import com.example.myAppData.Person;
import com.superbearman6.activitycollections.ActivityCollection;
import com.superbearman6.activitycollections.IndicatorInfo;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActivityCollection {
    private Person person;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBottomTabBackground(R.color.light_blue);// 设置底部导航背景图
        person = (Person) getApplication();


    }
    @Override
    protected boolean isShowWindowFeature() {
        return false;//设置是否显示title;
    }
    @Override
    protected List<IndicatorInfo> setDrawableCollections() {
            List<IndicatorInfo> IndicatorInfos = new ArrayList<IndicatorInfo>();
            IndicatorInfo indicatorInfo_1 = new IndicatorInfo(R.mipmap.ic_launcher,
                    R.mipmap.ic_launcher,R.string.bottom_btn1,  12, Color.BLACK,
                    new Intent(MainActivity.this, FirstActivity.class));
            IndicatorInfo indicatorInfo_2 = new IndicatorInfo(R.drawable.near_before,
                    R.drawable.near_after,R.string.bottom_btn2,  12, Color.BLACK,
                    new Intent(MainActivity.this, FirstActivity.class));
            IndicatorInfo indicatorInfo_3 = new IndicatorInfo(R.mipmap.ic_launcher,
                    R.mipmap.ic_launcher,R.string.bottom_btn3,  12, Color.BLACK,
                    new Intent(MainActivity.this, ContactListActivity.class));
            IndicatorInfo indicatorInfo_4 = new IndicatorInfo(R.mipmap.ic_launcher,
                    R.mipmap.ic_launcher,R.string.bottom_btn4,  12, Color.BLACK,
                    new Intent(MainActivity.this, Me1Activity.class));

            IndicatorInfos.add(indicatorInfo_1);
            IndicatorInfos.add(indicatorInfo_2);
            IndicatorInfos.add(indicatorInfo_3);
            IndicatorInfos.add(indicatorInfo_4);

            return IndicatorInfos;
    }
}
