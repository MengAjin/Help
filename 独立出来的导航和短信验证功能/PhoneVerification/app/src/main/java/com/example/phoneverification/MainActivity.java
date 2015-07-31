package com.example.phoneverification;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.mob.tools.utils.R.getStringRes;

public class MainActivity extends Activity implements OnClickListener {
    private TimeCount time;
    private Button sensmsButton,verificationButton;
    private EditText phonEditText,verEditText;
    // 填写从短信SDK应用后台注册得到的APPKEY
    private static String APPKEY = "936ff0c80764";//463db7238681  27fe7909f8e8
    // 填写从短信SDK应用后台注册得到的APPSECRET
    private static String APPSECRET = "bb79cd5f166e0da05fe5194e3273a804";//
    public String phString;                                              //3684fd4f0e16d68f69645af1c7f8f5bd
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensmsButton=(Button) findViewById(R.id.button1);
        verificationButton=(Button) findViewById(R.id.button2);
        phonEditText=(EditText) findViewById(R.id.editText1);
        verEditText=(EditText) findViewById(R.id.editText2);
        sensmsButton.setOnClickListener(this);
        verificationButton.setOnClickListener(this);
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
        //System.loadLibrary("OSbase");
        SMSSDK.initSDK(this,APPKEY,APPSECRET);
        EventHandler eh = new EventHandler(){

            @Override
            public void afterEvent(int event, int result, Object data) {

                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eh);
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.button1://获取验证码
                if(!TextUtils.isEmpty(phonEditText.getText().toString())){
                    SMSSDK.getVerificationCode("86",phonEditText.getText().toString());
                    phString=phonEditText.getText().toString();
                }else {
                    Toast.makeText(this, "电话不能为空", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.button2://校验验证码
                if(!TextUtils.isEmpty(verEditText.getText().toString())){
                    SMSSDK.submitVerificationCode("86", phString, verEditText.getText().toString());
                }else {
                    Toast.makeText(this, "验证码不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
    Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event="+event);
            Log.d("result", "="+result);
            if (result == SMSSDK.RESULT_COMPLETE) {
                //短信注册成功后，返回MainActivity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                    Toast.makeText(getApplicationContext(), "验证成功", Toast.LENGTH_SHORT).show();
                    //textView2.setText("提交验证码成功");
                    //验证成功就可以发送注册消息了
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    Toast.makeText(getApplicationContext(), "验证码已经发送，两分钟之内会收到", Toast.LENGTH_SHORT).show();
                    //textView2.setText("验证码已经发送");
                    time.start();
                }
            } else {
                ((Throwable) data).printStackTrace();
                int resId = getStringRes(MainActivity.this, "smssdk_network_error");
                Toast.makeText(MainActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                if (resId > 0) {
                    Toast.makeText(MainActivity.this, resId, Toast.LENGTH_SHORT).show();
                }
            }

        }

    };
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            sensmsButton.setText("重新验证");
            sensmsButton.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            sensmsButton.setClickable(false);
            sensmsButton.setText(millisUntilFinished / 1000 + "秒后可重新验证");
        }
    }

}
