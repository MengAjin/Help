package com.example.me;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.Class.Person;
import com.example.firstprogram.R;
import com.example.my_attend.MyAttendActivity;
import com.example.my_publish.MyPublishActivity;

/**
 * Created by 漫豆画芽 on 2015/7/21.
 */
public class Me1Activity extends Activity{
    private LinearLayout my_infor,my_attend,my_send_out,setting;
    private TextView nick,gender;
    private Person person;
    private ImageView image;//头像

    private void init() {
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
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
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


}
