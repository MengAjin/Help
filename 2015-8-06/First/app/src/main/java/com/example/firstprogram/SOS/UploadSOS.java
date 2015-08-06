package com.example.firstprogram.SOS;

import android.content.Context;
import android.util.Log;

import com.example.Class.GetAllParams;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by 丽俐 on 2015/7/29.
 */
public class UploadSOS {
    private GetAllParams cl;
    private JSONObject answer;
    private JSONArray list;
    private static String httpurl = "http://120.24.208.130:1503/event/add";

    public JSONObject addSOS(JSONObject obj, final Context mcontext, final uploadEvent callback) {
        //获取上传成功后数据
        JSONObject temp = new JSONObject();
        answer = new JSONObject();
        cl = new GetAllParams(mcontext);

        temp = cl.getList(httpurl, obj, new GetAllParams.VolleyJsonCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                answer = result;
                if(answer != null) {
                    try {
                        Log.d("TAG", "SOS：Responce -> " + answer.toString());
                        callback.onSuccess(answer.getInt("event_id"));
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return null;
    }

    public interface uploadEvent{
        void onSuccess(int e_id);
    }
}
