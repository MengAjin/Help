package com.example.my_attend;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.example.Class.GetAllParams;
import com.example.Class.Person;
import com.example.firstprogram.R;
import com.example.firstprogram.myAdapter.CardsAdapter;
import com.example.my_publish.event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 漫豆画芽 on 2015/8/10.
 */
public class MyAttendActivity extends Activity {
    private ListView listview;
    private CardsAdapter cardsAdapter;
    private GetAllParams getAllParams;
    private JSONArray jsonArray;
    private List<event>  listData;
    private Person person;
    private Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myattend_activity);

        init();
        set_Adapter();
    }

    private void init() {
        mContext = this;
        person = (Person) getApplication();
        getAllParams = new GetAllParams(this);
        //findviewbyid
        listview = (ListView) findViewById(R.id.listview);
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
    private void set_Adapter(){
        //http://120.24.208.130:1503/event/query_join
        JSONObject param = new JSONObject();
        try {
            param.put("id", person.getId());
            param.put("type", 2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getAllParams.getList("http://120.24.208.130:1503/event/query_join",
                param,
                new GetAllParams.VolleyJsonCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            jsonArray = result.getJSONArray("event_list");
                            listData = getListData();
                            cardsAdapter = new CardsAdapter(mContext, listData);
                            listview.setAdapter(cardsAdapter);
                            cardsAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
