package com.example.me;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Class.GetAllParams;
import com.example.Class.Person;
import com.example.firstprogram.R;
import com.example.my_attend.MyAttendActivity;
import com.example.my_publish.MyPublishActivity;
import com.tandong.swichlayout.BaseEffects;
import com.tandong.swichlayout.SwitchLayout;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 漫豆画芽 on 2015/7/21.
 */
public class Me1Activity extends Activity{
    private LinearLayout my_infor,my_attend,my_send_out,setting;
    private TextView nick,gender;
    private Person person;
    private ImageView image;//头像
    int sta,sta1;

    private void init() {
        //设置进入动画
        SwitchLayout.getSlideFromLeft(this, false, BaseEffects.getLinearInterEffect());

        my_infor = (LinearLayout) findViewById(R.id.my_infor);
        nick = (TextView) findViewById(R.id.my_infor_nick1);
        image = (ImageView) findViewById(R.id.initial_avatar);
        person = (Person) getApplication();
        if(person.getNick()!=null)
            nick.setText(person.getNick());

        //我发出的监听
        my_send_out = (LinearLayout) findViewById(R.id.my_send_out);
        my_send_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Me1Activity.this, MyPublishActivity.class);
                startActivity(intent);
            }
        });
        //我参与的监听
        my_attend = (LinearLayout) findViewById(R.id.my_attend);
        my_attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Me1Activity.this, MyAttendActivity.class);
                startActivity(intent);
            }
        });
        //设置相关
        setting = (LinearLayout) findViewById(R.id.my_setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Me1Activity.this, SetUp.class);
                startActivity(intent);
            }
        });
        /*签到相关*/
        findViewById(R.id.sign_in).setOnClickListener(new qiandao_listener());
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//设置为没有titlebar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_1_activity);
        init();
        my_infor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Me1Activity.this, MeInfoActivity1.class);
                startActivityForResult(intent,1);
            }
        });
        /*头像处理相关*/
        avatar_related();

    }
    /*头像处理相关*/
    private void avatar_related() {
        String p = Environment.getExternalStorageDirectory() + "/Temp_help/"+ Integer.toString(person.getId()) + ".jpg";
        Bitmap bt = BitmapFactory.decodeFile(p);
        if(bt != null) {

            Drawable drawable = new BitmapDrawable(bt);
            image.setImageDrawable(drawable);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (resultCode) {
            case 0:
                nick.setText(data.getStringExtra("change"));
                String p = Environment.getExternalStorageDirectory() + "/Temp_help/"+ Integer.toString(person.getId()) + ".jpg";
                Bitmap bt = BitmapFactory.decodeFile(p);
                if(bt != null) {

                    Drawable drawable = new BitmapDrawable(bt);
                    image.setImageDrawable(drawable);
                }
                break;
            default:
                break;
        }
    }

    /*退出效果*/
    public boolean onKeyDown(int keyCode, KeyEvent event) {// 按返回键时退出Activity的Activity特效动画

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            SwitchLayout.getSlideToLeft(this, true, BaseEffects.getLinearInterEffect());
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*签到相关*/
    private class qiandao_listener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String httpurl = "http://120.24.208.130:1503/account/signin";
            String httpurl_coins = "http://120.24.208.130:1503/user/coins";

            JSONObject obj = new JSONObject();
            JSONObject temp = new JSONObject();
            JSONObject temp1 = new JSONObject();
            GetAllParams c = new GetAllParams(Me1Activity.this);
            final JSONObject[] answer = {new JSONObject()};
            final JSONObject[] answer1 = {new JSONObject()};

            try {
                obj.put("id", person.getId());
                temp1 = c.getList(httpurl_coins, obj, new GetAllParams.VolleyJsonCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        answer1[0] = result;
                        try {
                            sta1 = answer1[0].getInt("love_coin");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                temp = c.getList(httpurl, obj, new GetAllParams.VolleyJsonCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        answer[0] = result;
                        try {
                            sta = answer[0].getInt("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (sta == 200) {
                            Toast.makeText(getApplicationContext(), "签到成功,当前爱心币：" + sta1, Toast.LENGTH_SHORT).show();
                        } else if (sta == 500) {
                            Toast.makeText(getApplicationContext(), "已经签到不能重复签到", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
