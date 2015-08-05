package com.example.log;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.firstprogram.R;
import com.example.main.MainActivity;
import com.example.Class.Person;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;

public class LogInActivity extends Activity {
    private Person person;
    private TextView account, password, signup;
    private Button login;
    private SharedPreferences sp;
    private EditText username;
    private EditText userpassword;
    private String userNameValue,passwordValue;
    private String ss_account, ss_password;
    private String httpurl,httpurl_person_info;
    private boolean flag1;
    private boolean flag2;
    private RequestQueue requestQueue;
    private JSONObject return_json1;
    private boolean flag;
    private void init() {
        person = (Person) getApplication();
        httpurl = "http://120.24.208.130:1503/account/login";
        httpurl_person_info = "http://120.24.208.130:1503/user/get_information";
        account = (TextView) findViewById(R.id.log_account);
        password = (TextView) findViewById(R.id.log_password);
        login = (Button) findViewById(R.id.login_button);
        signup = (TextView) findViewById(R.id.signup_button);
        signup.setOnClickListener(new signup_listener());
    }
    private class signup_listener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
            startActivity(intent);
        }
    }
    private void getMyInfo(){
        JSONObject map = new JSONObject();
        try {
            map.put("account", person.getAccount());
            JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST,
                    httpurl_person_info, map, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.d("TAG", jsonObject.toString());
                    try {
                        if(jsonObject.getString("status") != "500"){
                            if(jsonObject.getString("nickname")!=null){
                                person.setNick(jsonObject.getString("nickname"));
                            }
                            person.setPhone(jsonObject.getInt("phone"));
                            person.setGender(jsonObject.getInt("gender"));
                            if(String.valueOf(jsonObject.getInt("age"))!=null)
                                person.setAge(jsonObject.getInt("age"));
                            else
                                person.setAge(0);
                            person.setLocation(jsonObject.getString("location"));
                            if(String.valueOf(jsonObject.getInt("occupation"))!=null)
                                person.setOccupation(jsonObject.getInt("occupation"));
                            else
                                person.setOccupation(4);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("TAG", volleyError.toString());
                }
            });

            requestQueue.add(jsonRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        init();


        // 初始化用户名、密码
        username = (EditText) findViewById(R.id.log_account);
        userpassword = (EditText) findViewById(R.id.log_password);
        sp = getSharedPreferences("userInfo", 0);
        String name = sp.getString("USER_NAME", "");
        String pass = sp.getString("PASSWORD", "");
        username.setText(name);
        userpassword.setText(pass);
        if(!name.equals("")) {
            Log.d("TAG", "entry onclick1");
            flag1 = false;
            flag2 = false;
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            //Map<String, String> map = new HashMap<String, String>();
            JSONObject map = new JSONObject();
            try {
                map.put("account", Long.parseLong(account.getText().toString()));
                map.put("password", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //JSONObject jsonObject = new JSONObject(map);
            //Log.d("TAG", "params -> " + jsonObject.toString());
            Log.d("TAG", "entry onclick2");
            JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST,
                    httpurl, map, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("TAG", "response -> " + response.toString());
                    if (response.toString().contains("status")) {
                        Toast.makeText(LogInActivity.this, "account isn't exist!", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("TAG", "response -> " + "login1 success");
                        flag1 = true;
                        return_json1 = new JSONObject();
                        return_json1 = response;
                        JSONObject map2 = new JSONObject();
                        try {
                            map2.put("account", return_json1.getString("account"));
                            map2.put("password", MD5(password.getText().toString() + return_json1.getString("salt")));
                            map2.put("salt", return_json1.getString("salt"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JsonObjectRequest jsonRequest2 = new JsonObjectRequest(Request.Method.POST,
                                httpurl, map2, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                Log.d("TAG", "response22 -> " + jsonObject.toString());
                                if (jsonObject.toString().contains("id")) {
                                    try {
                                        //set id
                                        person.setId(jsonObject.getInt("id"));
                                        person.setAccount(Long.parseLong(account.getText().toString()));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(LogInActivity.this, "Login success!", Toast.LENGTH_SHORT).show();
                                    getMyInfo();
                                    finish();
                                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LogInActivity.this, "password isn't match with account", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Log.e("TAG", volleyError.getMessage(), volleyError);
                                Toast.makeText(LogInActivity.this, "connection failed", Toast.LENGTH_SHORT).show();
                            }
                        });

                        requestQueue.add(jsonRequest2);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG", error.getMessage(), error);
                    Toast.makeText(LogInActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                }
            });
            Log.d("TAG", "jsonRequest -> " + jsonRequest.toString());
            requestQueue.add(jsonRequest);
        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("")) {
                    Toast t = Toast.makeText(LogInActivity.this, "请输入账号", Toast.LENGTH_SHORT);
                    t.show();
                    return;
                }
                if (userpassword.getText().equals("")) {
                    Toast t = Toast.makeText(LogInActivity.this, "请输入密码", Toast.LENGTH_SHORT);
                    t.show();
                    return;
                }

                userNameValue = username.getText().toString();
                passwordValue = userpassword.getText().toString();
                SharedPreferences.Editor editor =sp.edit();

                //保存用户名和密码
                editor.putString("USER_NAME", userNameValue);
                editor.putString("PASSWORD", passwordValue);
                editor.commit();

                Log.d("TAG","entry onclick1");
                flag1 = false;
                flag2 = false;
                requestQueue = Volley.newRequestQueue(getApplicationContext());
                //Map<String, String> map = new HashMap<String, String>();
                JSONObject map = new JSONObject();
                try {
                    map.put("account", Long.parseLong(account.getText().toString()));
                    map.put("password", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //JSONObject jsonObject = new JSONObject(map);
                //Log.d("TAG", "params -> " + jsonObject.toString());
                Log.d("TAG","entry onclick2");
                JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST,
                        httpurl, map, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", "response -> " + response.toString());
                        if (response.toString().contains("status")) {
                            Toast.makeText(LogInActivity.this, "account isn't exist!", Toast.LENGTH_SHORT).show();
                        } else{
                            Log.d("TAG", "response -> " + "login1 success");
                            flag1 = true;
                            return_json1 = new JSONObject();
                            return_json1 = response;
                            JSONObject map2 = new JSONObject();
                            try {
                                map2.put("account",return_json1.getString("account"));
                                map2.put("password",MD5(password.getText().toString() + return_json1.getString("salt")));
                                map2.put("salt",return_json1.getString("salt"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            JsonObjectRequest jsonRequest2 = new JsonObjectRequest(Request.Method.POST,
                                    httpurl, map2, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    Log.d("TAG", "response22 -> " + jsonObject.toString());
                                    if (jsonObject.toString().contains("id")) {
                                        try {
                                            //set id
                                            person.setId(jsonObject.getInt("id"));
                                            person.setAccount(Long.parseLong(account.getText().toString()));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(LogInActivity.this, "Login success!", Toast.LENGTH_SHORT).show();
                                        getMyInfo();
                                        finish();
                                        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    } else{
                                        Toast.makeText(LogInActivity.this, "password isn't match with account", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Log.e("TAG", volleyError.getMessage(), volleyError);
                                    Toast.makeText(LogInActivity.this, "connection failed", Toast.LENGTH_SHORT).show();
                                }
                            });

                            requestQueue.add(jsonRequest2);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        Toast.makeText(LogInActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d("TAG", "jsonRequest -> " + jsonRequest.toString());
                requestQueue.add(jsonRequest);



            }

        });
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
