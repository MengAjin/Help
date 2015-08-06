package com.example.Class;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
                            Log.d("TAG-get", "response -> " + "Receive success" + return1.toString());
                            Toast.makeText(mContext, "200!", Toast.LENGTH_SHORT).show();
                            callback.onSuccess(return1);
                        }
                        else {
                            Log.d("GetAllParams", "500");
                            Toast.makeText(mContext, "500!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mContext, "connection failed!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonRequest);
        return null;
    }

    public interface VolleyJsonCallback{
       void onSuccess(JSONObject result);
    }

}
