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
     * ����ת����ƴ������
     */
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;

    /**
     * ����ƴ��������ListView�����������
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
        //ʵ��������תƴ����
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        //�����Ҳഥ������
        sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //����ĸ�״γ��ֵ�λ��
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
//		map_.put("name", "�ϰ��");
//
//		//map_.put("photo", R.mipmap.ic_launcher);
//		data_.add(map_);
//
//		map_ = new HashMap<String, Object>();
//		map_.put("name", "���");
//		//map_.put("photo", R.mipmap.ic_launcher);
//		data_.add(map_);

//		map_ = new HashMap<String, Object>();
//		map_.put("name","��ɯɯ");
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
                                // ����a-z��������Դ����
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
//        //�������������ֵ�ĸı�����������
//        mClearEditText.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                //������������ֵΪ�գ�����Ϊԭ�����б�����Ϊ���������б�
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

        //����leftclick��rightclick�ĵ���¼�
        topBar = (TopBar) findViewById(R.id.topbarInmergencyContactList);
        //����topbar�ļ����¼�
        topBar.setOnTopbarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() throws UnsupportedEncodingException {
                Intent intent = new Intent(EmergencyContactActivity.this,EmergencyContactAdd.class);
                startActivity(intent);
            }
        });
    }


    /**
     * ΪListView�������
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
            //����ת����ƴ��
            String pinyin = characterParser.getSelling((String) data.get(i).get("name"));
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
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
     * ����������е�ֵ���������ݲ�����ListView
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

        // ����a-z��������
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

}
