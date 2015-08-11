package com.example.Message;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.firstprogram.R;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends Activity {
	private ListView list;
	private ChatListAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_main);
		new Thread(new Runnable() {
            @Override
            public void run() {
                FaceConversionUtil.getInstace().getFileText(getApplication());
            }
        }).start();

		init();
		set_adapter();
	}
	private void init() {
		list = (ListView) findViewById(R.id.listview);
		list.setOnItemClickListener(new ListView_listener());
	}
	private void set_adapter(){
		adapter = new ChatListAdapter(this, getData());
		list.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	//模拟一组数据
	private List<ChatMsgEntity> getData() {
		List<ChatMsgEntity> data = new ArrayList<>();

		ChatMsgEntity temp = new ChatMsgEntity("007", "2015-8-11", "吃饭了吗", true);
		data.add(temp);
		temp = new ChatMsgEntity("008", "2015-8-11", "吃饭了吗", true);
		data.add(temp);
		temp = new ChatMsgEntity("009", "2015-8-11", "有空吗", true);
		data.add(temp);
		temp = new ChatMsgEntity("小红", "2015-8-11", "哈哈", true);
		data.add(temp);

		return data;
	}
	private class ListView_listener implements AdapterView.OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(MessageActivity.this, ChatActivity.class);
			startActivity(intent);
		}
	}
}
