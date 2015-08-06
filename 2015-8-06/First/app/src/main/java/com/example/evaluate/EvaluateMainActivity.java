package com.example.evaluate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.example.Class.GetAllParams;
import com.example.Class.Person;
import com.example.firstprogram.R;
import com.example.firstprogram.TopBar;
import com.example.firstprogram.myAdapter.mymyAdapter;
import com.example.main.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public class EvaluateMainActivity extends Activity {
    private Button helper_button;
    private Button one_key_evaluate_button;


	private String url,url2;
	private int id,event_id;
	private Person person;
	private GetAllParams c;
	private JSONObject answer,answer2;
	private JSONObject obj;
	private JSONObject obj1;
	private JSONArray list;
	private long[] accounts;//帮助者的账号
	private Map<String, Object> helper;
	private List<Map<String, Object>> lists;
	private ListView listview;
	private mymyAdapter adapter;
	private List<Map<String, Object >> listItemContainer;
	private EvaluateAccounts ea;
	private Context mcontext;

	//初始化
	void init(){
		person = (Person) getApplication();
		id = person.getId();
		Intent intent = getIntent();  //获取上一个activity的intent里的事件ID“event_id"
		event_id =  intent.getIntExtra("event_id",-1);
		mcontext = EvaluateMainActivity.this;
		listview = (ListView) findViewById(R.id.activity_evaluate_listview);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluate_main);

		init();

		JSONObject temp = new JSONObject();
		answer = new JSONObject();
		c = new GetAllParams(EvaluateMainActivity.this);
		obj = new JSONObject();
		try {
			obj.put("id", id);
			obj.put("event_id", event_id);
			ea = new EvaluateAccounts();
			accounts = new long[20];
			temp = ea.GetAccount(obj, mcontext, new EvaluateAccounts.getAccount() {
				@Override
				public void onSuccess1(List<Map<String, Object>> as, long[] a) {
					if(as != null) {
						listItemContainer = as;
						accounts = a;
						Log.d("TAG", "Main  receive Account List -> " + accounts[0]);
						Log.d("TAG", "Main  come Account List -> " + a[0]);
						//个人评论在adapter中实现
						adapter = new mymyAdapter(EvaluateMainActivity.this, listItemContainer, a, event_id);
						listview.setAdapter(adapter);

						//点击返回按钮
						TopBar topBar = (TopBar)findViewById(R.id.topbar_in_evaluate);
						topBar.setOnTopbarClickListener(new TopBar.topbarClickListener() {
							@Override
							public void leftClick() {
								Intent intent = new Intent(EvaluateMainActivity.this, MainActivity.class);
								startActivity(intent);
							}
							@Override
							public void rightClick() throws UnsupportedEncodingException {

							}
						});
						//点击“一键评价”
						one_key_evaluate_button = (Button)findViewById(R.id.one_key_evaluate_button);
						one_key_evaluate_button.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(EvaluateMainActivity.this, OneKeyEvaluationActivity.class);
								Log.d("TAG", "Main Account List -> " + accounts[0]);
								Log.d("TAG", "Main event -> " + event_id);
								intent.putExtra("user_account", accounts);
								intent.putExtra("event_id", event_id);
								startActivity(intent);
							}

						});
					}
					else {
						Log.d("TAG", "Main Account List -> " + "is null");
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
