package com.example.Class;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by 丽俐 on 2015/7/27.
 */
public class GetAllParams {

    private RequestQueue requestQueue;
    private JSONObject return1;
    private JSONArray ja;
    private int sta;
    private Context mContext;

    public GetAllParams(Context c) {
        mContext = c;
    }

    public JSONObject getList(String httpurl, JSONObject obj, final VolleyJsonCallback callback) {
        requestQueue = Volley.newRequestQueue(mContext);

        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST,
                httpurl, obj, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                if (response.toString().contains("status")) {
                    return1 = new JSONObject();
                    return1 = response;
                    try {
                        sta = return1.getInt("status");
                        if(sta == 200) {//获取成功
                            //Log.d("TAG", "response -> " + "Receive success" + return1.toString());
                            callback.onSuccess(return1);
                        }
                        else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                //Toast.makeText(this, "Login failed!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonRequest);
        return null;
    }

    public interface VolleyJsonCallback{
       void onSuccess(JSONObject result);
    }

}
