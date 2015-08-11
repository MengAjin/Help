package com.example.firstprogram.SOS;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.Class.GetAllParams;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by ���� on 2015/7/29.
 */
public class SOSContact {
    private GetAllParams cl;
    private String[] phoneNums;
    private JSONObject answer;
    private JSONArray list;
    private static String httpurl = "http://120.24.208.130:1503/user/get_relation";

    public void sendSMS(JSONObject obj, String message, final Context mcontext) {
        //��ȡ��ϵ���б�
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
                                //Log.d("TAG", "SOS��ҳ��phone -> " + "received" + phoneNums[i]);
                                Toast.makeText(mcontext, "��ϵ�˺���" + phoneNums[i], Toast.LENGTH_SHORT);
                                //System.out.println("��ϵ�˺���" + phoneNums[i]);

                                //���÷��Ͷ���API���ɹ����ط��ͳɹ������򷵻ط���ʧ��

                            }
                            Log.d("TAG", "SOS��ҳ��phone -> " + "received" + phoneNums[0]);
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
