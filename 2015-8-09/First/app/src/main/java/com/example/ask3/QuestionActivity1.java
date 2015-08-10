package com.example.ask3;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.Class.Person;
import com.example.firstprogram.R;
import com.example.firstprogram.TopBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class QuestionActivity1 extends Activity {
	private EditText edt1 ;
	private EditText edt2;
	private static final String[] str={"1km","2km","3km","其他"};
	private ArrayAdapter<String> arrayAdapter;
	private Spinner spinner;
	private TopBar topbar;
	private RequestQueue requestQueue;
	private JSONObject return_json1;
	private String url;
    private Person person;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_activity1);
        person = (Person) getApplication();
		spinner = (Spinner) findViewById(R.id.spinner1);
		edt1 = (EditText)findViewById(R.id.questionTitle);
		edt2 = (EditText)findViewById(R.id.questionContent);
		arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, str);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(arrayAdapter);
		url = "http://120.24.208.130:1503/event/add";
		//topbar
		topbar = (TopBar) findViewById(R.id.topBar_in_questionactivity1);
		topbar.setOnTopbarClickListener(new TopBar.topbarClickListener() {
			@Override
			public void leftClick() {
				finish();
			}

			@Override
			public void rightClick() throws UnsupportedEncodingException {
                final String title = edt1.getText().toString();
                final String content = edt2.getText().toString();
                if (title.equals("")) {
                        Toast t = Toast.makeText(QuestionActivity1.this, "请输入标题", Toast.LENGTH_SHORT);
                        t.show();
                        return;
                    }
                if (content.equals("")) {
                    Toast t = Toast.makeText(QuestionActivity1.this, "请输入内容", Toast.LENGTH_SHORT);
                        t.show();
                        return;
                    }
                requestQueue = new Volley().newRequestQueue(QuestionActivity1.this);
                    JSONObject jsonObject = new JSONObject();

                    try {
                        jsonObject.put("content", edt1.getText().toString() +"\n" + edt2.getText().toString());
                        jsonObject.put("id", person.getId());
                        jsonObject.put("type",0);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST,
                            url, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Log.d("TAG", jsonObject.toString());
                            try {
                                if (jsonObject.getInt("status") == 200) {
                                    Toast.makeText(QuestionActivity1.this, "提交成功", Toast.LENGTH_SHORT).show();
                                    finish();

                                }else if (jsonObject.getInt("status") == 500) {
                                    Toast.makeText(QuestionActivity1.this, "提交失败", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
                    requestQueue.add(jsonRequest);
                }

		});
	}



}
