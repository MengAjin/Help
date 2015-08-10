package com.example.log;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.Class.GetAllParams;
import com.example.firstprogram.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.mob.tools.utils.R.getStringRes;
public class Password_Find extends Activity {
    private Button sensmsButton,submitButton;
    private EditText account,password,name,nickname,verEditText;
    private String url;
    private RequestQueue requestQueue;
    private EditText password1,password2;
    //短信相关
    // 填写从短信SDK应用后台注册得到的APPKEY
    private static String APPKEY = "93dd15b8bb62";//463db7238681  27fe7909f8e8
    // 填写从短信SDK应用后台注册得到的APPSECRET
    private static String APPSECRET = "074df7159fbac405e95dbdcc0d5c5c80";//
    public String phString;
    //倒计时相关
    private TimeCount time;
    private GetAllParams getAllParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password__find);
        getAllParams = new GetAllParams(this);
        submitButton=(Button)findViewById(R.id.Button2InPasswordFind);
        sensmsButton=(Button) findViewById(R.id.Button1InPassword_Find);//确定按钮
        verEditText=(EditText) findViewById(R.id.EditTextInPasswordFind);//填写验证码
        account=(EditText )findViewById(R.id.password_find_phone);
        password1=((EditText)findViewById(R.id.newpassword_1));
        password2=((EditText)findViewById(R.id.newpassword_2));
        url = "http://120.24.208.130:1503/account/modify_password";
        submitButton.setOnClickListener(new submit_listener());
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
        //短信验证相关
        SMSSDK.initSDK(this, APPKEY, APPSECRET);
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
        //按钮“获取验证码”添加监听
        sensmsButton.setOnClickListener(new sensms_listener());
    }
    //按钮“提交”的监听事件
    private class submit_listener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(!TextUtils.isEmpty(verEditText.getText().toString())){
                SMSSDK.submitVerificationCode("86", phString, verEditText.getText().toString());
            }
            else {
                Toast.makeText(Password_Find.this, "verify number is empty", Toast.LENGTH_SHORT).show();
                return;
            }

        }
    }

    private class sensms_listener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(!TextUtils.isEmpty(account.getText().toString())){
                SMSSDK.getVerificationCode("86",account.getText().toString());
                phString=account.getText().toString();
            }else {
                Toast.makeText(Password_Find.this, "account is empty", Toast.LENGTH_SHORT).show();
                return;
            }
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
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功

                    //textView2.setText("提交验证码成功");
                    //验证成功就可以发送注册消息了
                    JSONObject param1 = new JSONObject();
                    try {
                        param1.put("account", Long.parseLong(account.getText().toString()));
                        param1.put("password", "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    getAllParams.getList("http://120.24.208.130:1503/account/login", param1,
                            new GetAllParams.VolleyJsonCallback() {
                                @Override
                                public void onSuccess(JSONObject result) {
                                    try {
                                        if(result.toString().contains("status")) {
                                            JSONObject param2 = new JSONObject();
                                            param2.put("account",Long.parseLong(account.getText().toString()));
                                            param2.put("password", MD5(password1.getText().toString() + result.getString("salt")));
                                            getAllParams.getList(url, param2, new GetAllParams.VolleyJsonCallback() {
                                                @Override
                                                public void onSuccess(JSONObject result) {
                                                    try {
                                                        if(result.getInt("status") == 200){
                                                            Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    Toast.makeText(getApplicationContext(), "verify success", Toast.LENGTH_SHORT).show();
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    Toast.makeText(getApplicationContext(), "验证码已经发送，2分钟之内会收到", Toast.LENGTH_SHORT).show();
                    time.start();
                }
            } else {
                ((Throwable) data).printStackTrace();
                int resId = getStringRes(Password_Find.this, "smssdk_network_error");
                Toast.makeText(Password_Find.this, "verify num error", Toast.LENGTH_SHORT).show();
                if (resId > 0) {
                    Toast.makeText(Password_Find.this, resId, Toast.LENGTH_SHORT).show();
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
    //MD5
    public static String MD5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}
