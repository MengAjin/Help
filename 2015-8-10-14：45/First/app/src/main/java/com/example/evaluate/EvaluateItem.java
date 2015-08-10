package com.example.evaluate;

import android.content.Context;
import android.util.Log;

import com.example.Class.GetAllParams;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ÀöÀþ on 2015/7/30.
 */
public class EvaluateItem {

    private HashMap<String, Object> helper;
    private GetAllParams cl;
    private JSONObject answer;
    private JSONObject list;

    private static String httpurl = "http://120.24.208.130:1503/user/get_information";
    //private Long[] accounts;//°ïÖúÕßµÄÕËºÅ
    public JSONObject GetAccount(JSONObject obj, final Context mcontext, final getList callback) {
        JSONObject temp = new JSONObject();
        answer = new JSONObject();
        cl = new GetAllParams(mcontext);

                temp = cl.getList(httpurl, obj, new GetAllParams.VolleyJsonCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        answer = result;

                        if(answer != null) {
                            try{
                                Log.d("TAG", "User_Information is -> " + list.toString());
                                helper = new HashMap<String, Object>();
                                helper.put("image", answer.getString("avatar"));
                                helper.put("name", answer.getString("name"));
                                callback.onSuccess(helper);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            Log.d("TAG", "Account List -> " + "is null");
                            callback.onSuccess(null);
                        }
                    }
                });
        return null;
    }

    public interface getList{
        void onSuccess(HashMap<String, Object> item);
    }
}
