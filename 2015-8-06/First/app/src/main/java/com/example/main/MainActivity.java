package com.example.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.example.Class.GetAllParams;
import com.example.Class.Person;
import com.example.Near.NearActivity;
import com.example.contactList.ContactMainActivity;
import com.example.firstprogram.FirstActivity;
import com.example.firstprogram.R;
import com.example.me.Me1Activity;
import com.superbearman6.activitycollections.ActivityCollection;
import com.superbearman6.activitycollections.IndicatorInfo;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActivityCollection {
    private Person person;
    private String token;
    private GetAllParams getAllParams;
    public static MainActivity instance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBottomTabBackground(R.color.light_blue);// 设置底部导航背景图
        instance = this;
        person = (Person) getApplication();
        getAllParams = new GetAllParams(this);
        //推送相关
        Context context = getApplicationContext();
        XGPushManager.registerPush(context);
        token = XGPushConfig.getToken(context);//获得token
        //发送token到服务器
        send_token();
    }

    private void send_token() {
        JSONObject param = new JSONObject();
        try {
            param.put("id", person.getId());
            param.put("token", token);Log.d("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getAllParams.getList("http://120.24.208.130:1503/account/update_token",
                param, new GetAllParams.VolleyJsonCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            if(result.getInt("status") == 200){
                                Log.d("token", "token send success 200");
                            } else{
                                Log.d("token", "token send failed 500");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected boolean isShowWindowFeature() {
        return false;//设置是否显示title;
    }
    @Override
    protected List<IndicatorInfo> setDrawableCollections() {
            List<IndicatorInfo> IndicatorInfos = new ArrayList<IndicatorInfo>();
            IndicatorInfo indicatorInfo_1 = new IndicatorInfo(R.drawable.about_before_1,
                    R.drawable.about_after_1,R.string.bottom_btn1,  12, Color.BLACK,
                    new Intent(MainActivity.this, FirstActivity.class));
            IndicatorInfo indicatorInfo_2 = new IndicatorInfo(R.drawable.near_before_1,
                    R.drawable.near_after_1,R.string.bottom_btn2,  12, Color.BLACK,
                    new Intent(MainActivity.this, NearActivity.class));
            IndicatorInfo indicatorInfo_3 = new IndicatorInfo(R.drawable.contact_before_1,
                    R.drawable.contact_after_1,R.string.bottom_btn3,  12, Color.BLACK,
                    new Intent(MainActivity.this, ContactMainActivity.class));
            IndicatorInfo indicatorInfo_4 = new IndicatorInfo(R.drawable.me_before_1,
                    R.drawable.me_after_1,R.string.bottom_btn4,  12, Color.BLACK,
                    new Intent(MainActivity.this, Me1Activity.class));

            IndicatorInfos.add(indicatorInfo_1);
            IndicatorInfos.add(indicatorInfo_2);
            IndicatorInfos.add(indicatorInfo_3);
            IndicatorInfos.add(indicatorInfo_4);

            return IndicatorInfos;
    }
}
