package com.example.evaluate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.Class.GetAllParams;
import com.example.Class.Person;
import com.example.firstprogram.R;
import com.example.firstprogram.TopBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class PersonalEvaluationActivity extends Activity {
	//定义变量
	//3行星星：attitude,skill,satisfy
	private RatingBar personal_evaluate_ratingbar1 = null;
	private RatingBar personal_evaluate_ratingbar2 = null;
	private RatingBar personal_evaluate_ratingbar3 = null;
	//评论内容,爱心币
	private EditText assess;
	private EditText love_coin;
	private Context mContext;
	private Person person;
	private GetAllParams c;
	private String httpurl;
	private JSONObject obj;
	private JSONObject answer;
	private int eventId;
	private long[] acc;
	private String a;

	public PersonalEvaluationActivity() {}

	//构造函数
	public PersonalEvaluationActivity(Context context){
		mContext = context;
	}
	@Override
	//具体实现
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_evaluation);
		//初始化变量
		person = (Person)getApplication();
		mContext = PersonalEvaluationActivity.this;
		httpurl = "http://120.24.208.130:1503/user/evaluate";

		//3行星星
		personal_evaluate_ratingbar1 = (RatingBar)findViewById(R.id.personal_evaluate_ratingBar1);
		personal_evaluate_ratingbar2 = (RatingBar)findViewById(R.id.personal_evaluate_ratingBar2);
		personal_evaluate_ratingbar3 = (RatingBar)findViewById(R.id.personal_evaluate_ratingBar3);

		personal_evaluate_ratingbar1.setIsIndicator(false);
		personal_evaluate_ratingbar2.setIsIndicator(false);
		personal_evaluate_ratingbar3.setIsIndicator(false);

		//当星星数量改变时，将星星数量rating显示出来
		personal_evaluate_ratingbar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				Toast.makeText(PersonalEvaluationActivity.this, "rating:" + String.valueOf(rating),
						Toast.LENGTH_LONG).show();
			}
		});
		personal_evaluate_ratingbar2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				Toast.makeText(PersonalEvaluationActivity.this, "rating:"+String.valueOf(rating),
						Toast.LENGTH_LONG).show();
			}
		});
		personal_evaluate_ratingbar3.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				Toast.makeText(PersonalEvaluationActivity.this, "rating:"+String.valueOf(rating),
						Toast.LENGTH_LONG).show();
			}
		});


		//评论内容
		assess = (EditText)findViewById(R.id.textView4);

		//爱心币
		love_coin = (EditText)findViewById(R.id.textView6);


		//获取联系人列表
		Intent intent = getIntent();

		//表头
		TopBar topBar = (TopBar)findViewById(R.id.topbar_in_personal_evaluation);
		topBar.setOnTopbarClickListener(new TopBar.topbarClickListener() {
			@Override
			//左键：返回，返回评价主页面
			public void leftClick() {
				Intent intent = new Intent(PersonalEvaluationActivity.this, EvaluateMainActivity.class);
				startActivity(intent);
			}

			@Override
			//右键：提交，返回主页面
			public void rightClick() throws UnsupportedEncodingException {
				JSONObject temp = new JSONObject();//temp是一个JSON请求
				obj = new JSONObject();//obj是输入参数集合的一个包
				answer = new JSONObject();

				//c是一个已经定义好的GetAllParams，输入参数，就会返回相应的结果
				c = new GetAllParams(mContext);
				//l[0] = "11111122222";

				Intent intent1 = getIntent();
				eventId = intent1.getIntExtra("event_id", 0);
				acc = new long[20];
				acc = intent1.getLongArrayExtra("user_account");
				Log.d("TAG", "One key result -> " + acc[0] + "   " + eventId);
				a = new String();
				a = Long.toString(acc[0]);
				for(int i = 1; i < acc.length; i++) {
					a += ","+acc[i];
				}
				Log.d("TAG", "One key result account string -> " + a);
				try{
					//把所有输入参数打包成一个JSONObject obj
					obj.put("id",person.getId());
					obj.put("user_account",a);
					obj.put("event_id",intent1.getIntExtra("event_id", -1));
					obj.put("attitude", personal_evaluate_ratingbar1.getRating());
					obj.put("skill",personal_evaluate_ratingbar2.getRating());
					obj.put("satisfy",personal_evaluate_ratingbar3.getRating());
					obj.put("assess",assess.getText().toString());
					obj.put("love_coin", love_coin.getText().toString());

					//Toast.makeText(PersonalEvaluationActivity.this, "obj:" + obj.toString(), Toast.LENGTH_LONG).show();
					//得到GetAllParams返回的结果,并发送请求
					temp = c.getList(httpurl, obj, new GetAllParams.VolleyJsonCallback() {
						@Override
						public void onSuccess(JSONObject result) {
							answer = result;
							//把返回的结果打印出来
							Toast.makeText(PersonalEvaluationActivity.this, "提交成功",
									Toast.LENGTH_LONG).show();
							finish();
						}
					});

				}catch (JSONException e) {
					e.printStackTrace();
				}


			}

		});


	}
}
	
