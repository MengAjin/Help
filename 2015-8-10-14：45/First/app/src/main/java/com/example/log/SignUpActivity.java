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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.firstprogram.R;

import org.json.JSONException;
import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.mob.tools.utils.R.getStringRes;


public class SignUpActivity extends Activity {
    private Button button,sensmsButton;
    private EditText account,password,name,nickname,verEditText;
    private String url;
    private RequestQueue requestQueue;
    //短信相关
    // 填写从短信SDK应用后台注册得到的APPKEY
    private static String APPKEY = "93dd15b8bb62";//463db7238681  27fe7909f8e8
    // 填写从短信SDK应用后台注册得到的APPSECRET
    private static String APPSECRET = "074df7159fbac405e95dbdcc0d5c5c80";//
    public String phString;
    //倒计时相关
    private TimeCount time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);
        this.button = (Button)findViewById(R.id.sign_up_done_next);
        this.account = (EditText) findViewById(R.id.sign_up_phone);
        this.password = (EditText) findViewById(R.id.editText5);
        this.name = (EditText) findViewById(R.id.sign_up_real_name);
        this.nickname = (EditText)findViewById(R.id.sign_up_nickname);
        sensmsButton=(Button) findViewById(R.id.button1);
        verEditText=(EditText) findViewById(R.id.editText2);
        url = "http://120.24.208.130:1503/account/regist";
        button.setOnClickListener(new next_listener());

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
    //按钮“下一步”的监听事件
    private class next_listener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(!TextUtils.isEmpty(verEditText.getText().toString())){
                SMSSDK.submitVerificationCode("86", phString, verEditText.getText().toString());
            }else {
                Toast.makeText(SignUpActivity.this, "verify number is empty", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //按钮“获取验证码”的监听事件
    private class sensms_listener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(!TextUtils.isEmpty(account.getText().toString())){
                SMSSDK.getVerificationCode("86",account.getText().toString());
                phString=account.getText().toString();
            }else {
                Toast.makeText(SignUpActivity.this, "account is empty", Toast.LENGTH_SHORT).show();
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
                //短信注册成功后，返回MainActivity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                    Toast.makeText(getApplicationContext(), "verify success", Toast.LENGTH_SHORT).show();
                    //textView2.setText("提交验证码成功");
                    //验证成功就可以发送注册消息了
                    requestQueue = Volley.newRequestQueue(getApplicationContext());
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("account", Long.parseLong(account.getText().toString()));
                        jsonObject.put("password",password.getText().toString());
                        jsonObject.put("name", name.getText().toString());
                        jsonObject.put("nickname", nickname.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST,
                            url, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                Toast.makeText(SignUpActivity.this, jsonObject.getString("status"), Toast.LENGTH_SHORT).show();
                                if(jsonObject.getInt("status") == 200){
                                    Toast.makeText(SignUpActivity.this, "sign up success", Toast.LENGTH_SHORT).show();
                                    finish();
                                    //Intent intent =  new Intent(SignUpActivity.this,LogInActivity.class);
                                    //startActivity(intent);
                                }else if (jsonObject.getInt("status") == 500){
                                    Toast.makeText(SignUpActivity.this, "sign up failed！", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("TAG", error.getMessage(), error);
                        }
                    });
                    requestQueue.add(jsonRequest);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    Toast.makeText(getApplicationContext(), "验证码已经发送，2分钟之内会收到", Toast.LENGTH_SHORT).show();
                    //textView2.setText("验证码已经发送");
                    time.start();
                }
            } else {
                ((Throwable) data).printStackTrace();
                int resId = getStringRes(SignUpActivity.this, "smssdk_network_error");
                Toast.makeText(SignUpActivity.this, "verify num error", Toast.LENGTH_SHORT).show();
                if (resId > 0) {
                    Toast.makeText(SignUpActivity.this, resId, Toast.LENGTH_SHORT).show();
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
