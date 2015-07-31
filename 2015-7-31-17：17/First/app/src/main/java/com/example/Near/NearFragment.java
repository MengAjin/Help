package com.example.Near;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Toast;

import com.example.Class.GetAllParams;
import com.example.my_publish.CardsAdapter;
import com.example.my_publish.event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NearFragment extends Fragment {

	private static final String ARG_POSITION = "position";
	private ListView lv;//显示信息的listview
	private CardsAdapter cardsAdapter;//listview的自定义适配器
	private List<event> listData = new ArrayList<>();//显示在listview中的数据
	private JSONArray jsonArray = new JSONArray();//用来接收请求访问放回的jaon array
	private GetAllParams getAllParams;//用于请求
	private int position;//tab的标签号，从左到右，0是求救，1是救助，2是提问
	private static Context context;

	public static NearFragment newInstance(int position,Context context_) {
		NearFragment f = new NearFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		context = context_;
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		position = getArguments().getInt(ARG_POSITION);
		getAllParams = new GetAllParams(context);
		cardsAdapter = new CardsAdapter(context, listData);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		FrameLayout fl = new FrameLayout(getActivity());
		fl.setLayoutParams(params);

		params.setMargins(0,0,0,0);
		lv = new ListView(getActivity());
		lv.setLayoutParams(params);


		if(position == 0){//求救type=2
			JSONObject map = new JSONObject();
			try {
				//map.put("id", person.getId());
				map.put("type", 2);
				//查询用户发起的紧急求救事件
				getAllParams.getList("http://120.24.208.130:1503/event/query_all", map, new GetAllParams.VolleyJsonCallback() {
					@Override
					public void onSuccess(JSONObject jsonObject) {
						try {
							if (jsonObject.getInt("status") == 200) {
								Log.d("TAG", jsonObject.toString());
								jsonArray = jsonObject.getJSONArray("event_list");
								Log.d("TAG-jsonArray", jsonArray.toString());
								listData = getListData();
								cardsAdapter = new CardsAdapter(context, listData);
								lv.setAdapter(cardsAdapter);
								cardsAdapter.notifyDataSetChanged();
								//给listview的每个Item添加单击监听事件
								lv.setOnItemClickListener(new item_listener());
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				});
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if(position == 1){//求助type=1
			JSONObject map = new JSONObject();
			try {
				//map.put("id", person.getId());
				map.put("type", 1);
				//查询用户发起的紧急求救事件
				getAllParams.getList("http://120.24.208.130:1503/event/query_all", map, new GetAllParams.VolleyJsonCallback() {
					@Override
					public void onSuccess(JSONObject jsonObject) {
						try {
							if (jsonObject.getInt("status") == 200) {
								Log.d("TAG", jsonObject.toString());
								jsonArray = jsonObject.getJSONArray("event_list");
								Log.d("TAG-jsonArray", jsonArray.toString());
								listData = getListData();
								cardsAdapter = new CardsAdapter(context, listData);
								lv.setAdapter(cardsAdapter);
								cardsAdapter.notifyDataSetChanged();
								//给listview的每个Item添加单击监听事件
								lv.setOnItemClickListener(new item_listener());
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				});
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {//提问type=0
			JSONObject map = new JSONObject();
			try {
				//map.put("id", person.getId());
				map.put("type", 0);
				//查询用户发起的紧急求救事件
				getAllParams.getList("http://120.24.208.130:1503/event/query_all", map, new GetAllParams.VolleyJsonCallback() {
					@Override
					public void onSuccess(JSONObject jsonObject) {
						try {
							if (jsonObject.getInt("status") == 200) {
								Log.d("TAG", jsonObject.toString());
								jsonArray = jsonObject.getJSONArray("event_list");
								Log.d("TAG-jsonArray", jsonArray.toString());
								listData = getListData();
								cardsAdapter = new CardsAdapter(context, listData);
								lv.setAdapter(cardsAdapter);
								cardsAdapter.notifyDataSetChanged();
								//给listview的每个Item添加单击监听事件
								lv.setOnItemClickListener(new item_listener());
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				});
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		fl.addView(lv);
		return fl;
	}
	//获得list的数据源
	private List<event> getListData() throws JSONException {
		List<event> mylist = new ArrayList<>();
		for(int i=0;i<jsonArray.length();i++) {
			event e = new event((JSONObject) jsonArray.get(i));
			mylist.add(e);
		}
		return mylist;
	}

	//list item 的监听事
	private class item_listener implements AdapterView.OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Toast.makeText(context, "position:"+String.valueOf(position)+"id:"+String.valueOf(id), Toast.LENGTH_SHORT).show();
			//Log.d("info", jsonArray.toString());
			//页面跳转
			final Intent intent = new Intent(context, NearDetail.class);
			try {
				Log.d("TAG-listitem", String.valueOf(listData.get(position).getEvent_id()));
				intent.putExtra("event_id", listData.get(position).getEvent_id());
				intent.putExtra("title",listData.get(position).getLauncher() );
				intent.putExtra("content", listData.get(position).getContent());
				intent.putExtra("type",listData.get(position).getType());
				intent.putExtra("time", listData.get(position).getTime());
				intent.putExtra("state", listData.get(position).getState());
				intent.putExtra("follow_nyumber", listData.get(position).getFollow_number());
				intent.putExtra("support_number", listData.get(position).getSupport_number());
				intent.putExtra("longitude",listData.get(position).getLongitude());
				intent.putExtra("latitude", listData.get(position).getLatitude());
				Log.d("TAG-longitude", String.valueOf(listData.get(position).getLongitude()));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			JSONObject map = new JSONObject();
			try {
				//map.put("id", person.getId());
				map.put("event_id", listData.get(position).getEvent_id());
				//查询用户发起的紧急求救事件
				getAllParams.getList("http://120.24.208.130:1503/comment/query", map, new GetAllParams.VolleyJsonCallback() {
					@Override
					public void onSuccess(JSONObject jsonObject) {
						Log.d("comment-query", jsonObject.toString());
						try {
							if (jsonObject.getInt("status") == 200) {
								intent.putExtra("pingjia",jsonObject.toString());
								startActivity(intent);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
			} catch (JSONException e) {
				e.printStackTrace();
			}


		}
	}
}