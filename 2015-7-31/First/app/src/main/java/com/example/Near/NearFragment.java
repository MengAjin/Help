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
	private ListView lv;//��ʾ��Ϣ��listview
	private CardsAdapter cardsAdapter;//listview���Զ���������
	private List<event> listData = new ArrayList<>();//��ʾ��listview�е�����
	private JSONArray jsonArray = new JSONArray();//��������������ʷŻص�jaon array
	private GetAllParams getAllParams;//��������
	private int position;//tab�ı�ǩ�ţ������ң�0����ȣ�1�Ǿ�����2������
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


		if(position == 0){//���type=2
			JSONObject map = new JSONObject();
			try {
				//map.put("id", person.getId());
				map.put("type", 2);
				//��ѯ�û�����Ľ�������¼�
				getAllParams.getList("http://120.24.208.130:1503/event/query_all", map, new GetAllParams.VolleyJsonCallback() {
					@Override
					public void onSuccess(JSONObject jsonObject) {
						try {
							if (jsonObject.getInt("status") == 200) {
								Log.d("TAG", jsonObject.toString());
								jsonArray = jsonObject.getJSONArray("event_list");
								Log.d("TAG-jsonArray", jsonArray.toString());
								//listData = getListData();
								//cardsAdapter = new CardsAdapter(context, listData);
								cardsAdapter.items = getListData();
								lv.setAdapter(cardsAdapter);
								cardsAdapter.notifyDataSetChanged();
								//��listview��ÿ��Item��ӵ��������¼�
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
		} else if(position == 1){//����type=1
			JSONObject map = new JSONObject();
			try {
				//map.put("id", person.getId());
				map.put("type", 1);
				//��ѯ�û�����Ľ�������¼�
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
								//��listview��ÿ��Item��ӵ��������¼�
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
		} else {//����type=0
			JSONObject map = new JSONObject();
			try {
				//map.put("id", person.getId());
				map.put("type", 0);
				//��ѯ�û�����Ľ�������¼�
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
								//��listview��ÿ��Item��ӵ��������¼�
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
	//���list������Դ
	private List<event> getListData() throws JSONException {
		List<event> mylist = new ArrayList<>();
		for(int i=0;i<jsonArray.length();i++) {
			event e = new event((JSONObject) jsonArray.get(i));
			mylist.add(e);
		}
		return mylist;
	}

	//list item �ļ�����
	private class item_listener implements AdapterView.OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Toast.makeText(context, "position:"+String.valueOf(position)+"id:"+String.valueOf(id), Toast.LENGTH_SHORT).show();
			//Log.d("info", jsonArray.toString());
			//ҳ����ת
			Intent intent = new Intent();

		}
	}
}