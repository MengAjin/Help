package com.example.evaluate;

import android.content.Context;
import android.util.Log;

import com.example.Class.GetAllParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 丽俐 on 2015/7/30.
 */
public class EvaluateAccounts {
    private GetAllParams cl;
    private JSONObject answer;
    private JSONArray list;
    private static String httpurl = "http://120.24.208.130:1503/event/get_supporter";
    private long[] accounts;//帮助者的账号
    private HashMap<String, Object> helper;
    private List<Map<String, Object>> lists;

    public JSONObject GetAccount(JSONObject obj, final Context mcontext, final getAccount callback) {
        //获取上传成功后数据
        JSONObject temp = new JSONObject();
        answer = new JSONObject();
        cl = new GetAllParams(mcontext);

        temp = cl.getList(httpurl, obj, new GetAllParams.VolleyJsonCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                accounts = new long[20];
                answer = result;
                lists = new ArrayList<Map<String, Object>>();
                if(answer != null) {
                    try {
                        list = new JSONArray();
                        list = answer.getJSONArray("user_account");
                        Log.d("TAG", "User_list is -> " + list.toString());
                        if(list != null) {
                            for (int i = 0; i < list.length(); i++) {
                                helper = new HashMap<String, Object>();
                                JSONObject jo = new JSONObject();
                                jo =(JSONObject) list.get(i);
                                //helper.put("image", jo.getString("avatar"));
                                helper.put("name", jo.getString("name"));
                                lists.add(helper);
                                //String s = new String();
                                accounts[i] = jo.getLong("account");

                            }
                            Log.d("TAG", "accounts -> " + accounts[0]);
                            callback.onSuccess1(lists, accounts);
                        }                        else {
                            Log.d("TAG", "Account List -> " + "is null");
                            callback.onSuccess1(null, null);
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return null;
    }

    public interface getAccount{
        void onSuccess1(List<Map<String, Object>> as, long[] a);
        //void onSuccess2(ArrayList<Map<String, Object>> array);
    }
}
