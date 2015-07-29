package com.example.my_publish;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Âþ¶¹»­Ñ¿ on 2015/7/29.
 */
public class event {
    public int event_id, launcher_id, type, state, follow_number, support_number;
    private String launcher, content, time;
    private double longitude, latitude, group_pts;
    private JSONObject jsonObject;

    event(JSONObject jsonObject){
        this.jsonObject = jsonObject;
    }
    public int getEvent_id() throws JSONException {
        return jsonObject.getInt("event_id");
    }
    public String getContent() throws JSONException {
        return jsonObject.getString("content");
    }
    public int getState() throws JSONException {
        return jsonObject.getInt("state");
    }
    public void setState(int i){
        state = i;
    }
    public String getTime() throws JSONException {
        return jsonObject.getString("time");
    }
    public int getType() throws JSONException {
        return jsonObject.getInt("type");
    }
    public int getFollow_number() throws JSONException {
        return jsonObject.getInt("follow_number");
    }
    public int getSupport_number() throws JSONException {
        return jsonObject.getInt("support_number");
    }
}
