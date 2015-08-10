package com.example.contactList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.Class.GetAllParams;
import com.example.Class.Person;
import com.example.contactList.SideBar.OnTouchingLetterChangedListener;
import com.example.firstprogram.R;
import com.example.firstprogram.TopBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmergencyContactActivity extends Activity {

    private Context mcontext;
    private JSONObject anwser;
    private JSONArray list;
    private GetAllParams c;
    private JSONObject obj;
    private Person person;
    private static String httpurl = "http://120.24.208.130:1503/user/get_relation";
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    private ClearEditText mClearEditText;
    private LinearLayout emergency;
    private TopBar topBar;

    private int userId,type;
    private GetAllParams getAllParams;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_contact_activity);
        getAllParams = new GetAllParams(this);
        initViews();
    }

    private void initViews() {
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if(position != -1){
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView = (ListView) findViewById(R.id.listView_in_emergencycontact);
        sortListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent();
                intent.setClass(EmergencyContactActivity.this, CheckEmergencyContactActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("account",String.valueOf((((SortModel)adapter.getItem(position)).getAccount())));
                  Log.d("TAG","account put right????????????"+String.valueOf((((SortModel)adapter.getItem(position)).getAccount())));
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
//		List<Map<String, Object>> data_ = new ArrayList<Map<String, Object>>();
//		Map<String,Object> map_ = new HashMap<String, Object>();
//
//		map_.put("name", "孟阿瑾");
//
//		//map_.put("photo", R.mipmap.ic_launcher);
//		data_.add(map_);
//
//		map_ = new HashMap<String, Object>();
//		map_.put("name", "李浩");
//		//map_.put("photo", R.mipmap.ic_launcher);
//		data_.add(map_);

//		map_ = new HashMap<String, Object>();
//		map_.put("name","尹莎莎");
//		//map_.put("photo", R.mipmap.ic_launcher);
//		data_.add(map_);

        JSONObject temp = new JSONObject();
        anwser = new JSONObject();
        mcontext = EmergencyContactActivity.this;
        c = new GetAllParams(mcontext);
        obj = new JSONObject();
        person = (Person)getApplication();
        userId = person.getId();
        try {
            obj.put("id", userId);
            obj.put("type", 0);
            temp = c.getList(httpurl, obj, new GetAllParams.VolleyJsonCallback() {
                @Override
                public void onSuccess(JSONObject result) {
                    Log.d("TAG", "AAAAAAAAAAAAAAA  contact  result -> " + result.toString());
                    anwser = result;
                    if(anwser != null ) {
                        Log.d("TAG","asdfghjklzxncmmmmmmmmmmmmmmmm");
                        try {
                            list = new JSONArray();
                            list = anwser.getJSONArray("user_list");
                            Log.d("TAG","listlistlistlistlistlist"+list.toString());
                            List<Map<String, Object>> data_ = new ArrayList<Map<String, Object>>();

                            for (int i = 0; i < list.length(); i++) {
                                Map<String,Object> map_ = new HashMap<String, Object>();
                                JSONObject jo = (JSONObject) list.get(i);
                                Log.d("TAG","listgetlishtgetlistget");
                                map_.put("name", jo.getString("nickname") + "		" + jo.getLong("account"));
                                map_.put("account",jo.getLong("account"));
                                Log.d("TAG", "BBBBBBBBBBBBBBBBBB  nickname -> " + "received" + jo.getString("nickname") );
                                Log.d("TAG", "CCCCCCCCCCCCCCCCCC  nickname -> " + "received" +jo.getLong("account") );
                                data_.add(map_);

                            }
                            if(data_ != null){
                                Log.d("TAG","data_ --------->   "+data_);
                                SourceDateList = filledData(data_);
                                // 根据a-z进行排序源数据
                                Collections.sort(SourceDateList, pinyinComparator);
                                adapter = new SortAdapter(mcontext, SourceDateList);
                                sortListView.setAdapter(adapter);

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    else {
                    }

                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }





//        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
//
//        //根据输入框输入值的改变来过滤搜索
//        mClearEditText.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
//                filterData(s.toString());
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });

        //设置leftclick和rightclick的点击事件
        topBar = (TopBar) findViewById(R.id.topbarInmergencyContactList);
        //设置topbar的监听事件
        topBar.setOnTopbarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                Intent intent = new Intent(EmergencyContactActivity.this,ContactMainActivity.class);
                startActivity(intent);
            }

            @Override
            public void rightClick() throws UnsupportedEncodingException {
                Intent intent = new Intent(EmergencyContactActivity.this,EmergencyContactAdd.class);
                startActivity(intent);
            }
        });
    }


    /**
     * 为ListView填充数据
     * @param data
     * @return
     */
    private List<SortModel> filledData(List<Map<String, Object>> data){
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for(int i=0; i< data.size(); i++){
            SortModel sortModel = new SortModel();
            sortModel.setName((String) data.get(i).get("name"));
            //sortModel.setImage((Drawable) data.get(i).get("photo"));
            sortModel.setAccount((long) data.get(i).get("account"));
            //汉字转换成拼音
            String pinyin = characterParser.getSelling((String) data.get(i).get("name"));
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if(sortString.matches("[A-Z]")){
                sortModel.setSortLetters(sortString.toUpperCase());
            }else{
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     * @param filterStr
     */
    private void filterData(String filterStr){
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if(TextUtils.isEmpty(filterStr)){
            filterDateList = SourceDateList;
        }else{
            filterDateList.clear();
            for(SortModel sortModel : SourceDateList){
                String name = sortModel.getName();
                if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

}
