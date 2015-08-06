package com.example.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
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
import com.example.firstprogram.TopBar;
import com.example.Class.Person;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class MeInfoEditNicknameActivity extends Activity {
    private EditText editNick;
    private TopBar topbar;
    boolean isChange = false;
    private Person person;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_1_myinfo_edit_nickname);
        init();
    }
    public void init() {
        person = (Person) getApplication();
        editNick = (EditText) findViewById(R.id.name_after_edit);
        editNick.setText(person.getNick());
        topbar = (TopBar) findViewById(R.id.topbar_in_modify_nickname);
        editNick.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                isChange = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isChange = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
                isChange = true;
            }
        });
        topbar.setOnTopbarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                Intent mIntent = new Intent();
                mIntent.putExtra("change", person.getNick());
                // 设置结果，并进行传送
                MeInfoEditNicknameActivity.this.setResult(0, mIntent);

                finish();
            }

            @Override
            public void rightClick() throws UnsupportedEncodingException {
                if (!isChange) {
                    Log.d("TAG", "no modify");
                    Toast.makeText(MeInfoEditNicknameActivity.this, "You do not modify yet.", Toast.LENGTH_SHORT).show();
                    Intent mIntent = new Intent();
                    mIntent.putExtra("change", person.getNick());
                    // 设置结果，并进行传送
                    MeInfoEditNicknameActivity.this.setResult(0, mIntent);
                    finish();
                } else {
                    Log.d("TAG", "has been modify");
                    RequestQueue requestQueue = new Volley().newRequestQueue(MeInfoEditNicknameActivity.this);
                    JSONObject map = new JSONObject();
                    try {
                        map.put("id", person.getId());
                        map.put("nickname", editNick.getText().toString());
                        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST,
                                "http://120.24.208.130:1503/user/modify_information", map,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject jsonObject) {
                                        Log.d("Tag", jsonObject.toString());
                                        person.setNick(editNick.getText().toString());
                                        Intent mIntent = new Intent();
                                        mIntent.putExtra("change", editNick.getText().toString());
                                        // 设置结果，并进行传送
                                        MeInfoEditNicknameActivity.this.setResult(1, mIntent);
                                        finish();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Log.d("Tag", "service error");
                            }
                        });
                        requestQueue.add(jsonRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent mIntent = new Intent();
            MeInfoEditNicknameActivity.this.setResult(0, mIntent);
            finish();
        }
        return false;
    }


}
