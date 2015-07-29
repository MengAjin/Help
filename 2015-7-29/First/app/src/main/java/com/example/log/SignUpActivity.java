package com.example.log;

import android.app.Activity;
import android.os.Bundle;
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


public class SignUpActivity extends Activity {
    private Button button;
    private EditText account,password,name,nickname;
    private String url;
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);
        this.button = (Button)findViewById(R.id.sign_up_done_next);
        this.account = (EditText) findViewById(R.id.sign_up_phone);
        this.password = (EditText) findViewById(R.id.editText5);
        this.name = (EditText) findViewById(R.id.sign_up_real_name);
        this.nickname = (EditText)findViewById(R.id.sign_up_nickname);
        url = "http://120.24.208.130:1503/account/regist";
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)  {
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
                                Toast.makeText(SignUpActivity.this, "sign up failed£¡", Toast.LENGTH_SHORT).show();
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
            }
        });
    }

}
