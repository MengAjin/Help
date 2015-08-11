package com.example.firstprogram.SOS;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.Class.GetAllParams;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by 丽俐 on 2015/7/29.
 */
public class SOSContact {
    private GetAllParams cl;
    private String[] phoneNums;
    private JSONObject answer;
    private JSONArray list;
    private static String httpurl = "http://120.24.208.130:1503/user/get_relation";

    public void sendSMS(JSONObject obj, String message, final Context mcontext) {
        //获取联系人列表
        JSONObject temp = new JSONObject();
        answer = new JSONObject();
        cl = new GetAllParams(mcontext);
        phoneNums = new String[20];
            temp = cl.getList(httpurl, obj, new GetAllParams.VolleyJsonCallback() {
                @Override
                public void onSuccess(JSONObject result) {
                    answer = result;
                    try {
                        list = answer.getJSONArray("user_list");
                        if(list != null) {
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject jo = (JSONObject) list.get(i);
                                phoneNums[i] = Long.toString(jo.getLong("account"));
                                //Log.d("TAG", "SOS主页：phone -> " + "received" + phoneNums[i]);
                                Toast.makeText(mcontext, "联系人号码" + phoneNums[i], Toast.LENGTH_SHORT);
                                //System.out.println("联系人号码" + phoneNums[i]);

                                //调用发送短信API，成功返回发送成功，否则返回发送失败

                            }
                            Log.d("TAG", "SOS主页：phone -> " + "received" + phoneNums[0]);
                        }
                        else {
                            phoneNums = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    }


}
