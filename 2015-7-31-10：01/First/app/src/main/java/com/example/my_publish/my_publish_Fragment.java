package com.example.my_publish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.Class.GetAllParams;
import com.example.Class.Person;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class my_publish_Fragment extends Fragment {

	private static final String ARG_POSITION = "position";
	private RequestQueue requestQueue;
	private int position;
	private Person person;
    private JSONArray jsonArray;
    private List<event> listData = new ArrayList<>();
    private ListView lv;
	private static Context context;
	private GetAllParams getAllParams;
	private CardsAdapter cardsAdapter, cardsAdapter2, cardsAdapter3;

    public static my_publish_Fragment newInstance(int position,Context context_) {
		my_publish_Fragment f = new my_publish_Fragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		context = context_;
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestQueue = new Volley().newRequestQueue(getActivity());
		getAllParams = new GetAllParams(context);
		person = (Person) getActivity().getApplication();
		position = getArguments().getInt(ARG_POSITION);
        jsonArray = new JSONArray();


    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		FrameLayout fl = new FrameLayout(getActivity());
		fl.setLayoutParams(params);

		final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
				.getDisplayMetrics());
		params.setMargins(0,0,0,0);
		lv = new ListView(getActivity());
		lv.setLayoutParams(params);

		//如果选择的是“求救”tab
		if (position == 0) {
			JSONObject map = new JSONObject();
			try {
				map.put("id", person.getId());
				map.put("type", 2);
				//查询用户发起的紧急求救事件
				getAllParams.getList("http://120.24.208.130:1503/event/query_launch", map, new GetAllParams.VolleyJsonCallback() {
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
		//如果选择的是“求助”tab
		if (position == 1) {
			fl.removeAllViews();
			JSONObject map = new JSONObject();
			try {
				map.put("id", person.getId());
				map.put("type", 1);
				//查询用户发起的普通求助事件
				getAllParams.getList("http://120.24.208.130:1503/event/query_launch", map, new GetAllParams.VolleyJsonCallback() {
					@Override
					public void onSuccess(JSONObject jsonObject) {
						try {
							if (jsonObject.getInt("status") == 200) {
								Log.d("TAG", jsonObject.toString());
								jsonArray = jsonObject.getJSONArray("event_list");
								Log.d("TAG-jsonArray", jsonArray.toString());
								listData = getListData();
								//lv = new ListView(getActivity());;
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
			//给listview的每个Item添加单击监听事件
			//lv.setOnItemClickListener(new item_listener());
		}
		//如果选择的是“提问”tab
		if (position == 2) {
			fl.removeAllViews();
			JSONObject map = new JSONObject();
			try {
				map.put("id", person.getId());
				map.put("type", 0);
				//查询用户发起的提问事件
				getAllParams.getList("http://120.24.208.130:1503/event/query_launch", map, new GetAllParams.VolleyJsonCallback() {
					@Override
					public void onSuccess(JSONObject jsonObject) {
						try {
							if (jsonObject.getInt("status") == 200) {
								Log.d("TAG", jsonObject.toString());
								jsonArray = jsonObject.getJSONArray("event_list");
								Log.d("TAG-jsonArray", jsonArray.toString());
								listData = getListData();
								//lv = new ListView(getActivity());
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
		public void onItemClick(AdapterView<?> parent, View view, int position, long id){
			//带参数去到下一个界面，参数分别有：event_id, content, time,
			Intent intent = new Intent(getActivity(), MyPublishHelp.class);
			try {
				intent.putExtra("topbar_title","");
				intent.putExtra("event_id", listData.get(position).getEvent_id());
				intent.putExtra("title", "");
				intent.putExtra("content", listData.get(position).getContent());
				intent.putExtra("type",listData.get(position).getType());
				intent.putExtra("time", listData.get(position).getTime());
				intent.putExtra("state", listData.get(position).getState());
				intent.putExtra("follow_nyumber", listData.get(position).getFollow_number());
				intent.putExtra("support_number", listData.get(position).getSupport_number());
				intent.putExtra("position", position);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			startActivityForResult(intent, 1);
		}
	}
	//重写onActivityResult函数
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
			case 1:
				Log.d("List-onActivityResult", String.valueOf(data.getIntExtra("state",3)));
				int p = data.getIntExtra("position", 0);
				//listData.get(position).setState(data.getIntExtra("state",1));
				listData.get(position).state = data.getIntExtra("state", 3);
				cardsAdapter.notifyDataSetChanged();
				break;
			default:
				break;
		}
	}
}