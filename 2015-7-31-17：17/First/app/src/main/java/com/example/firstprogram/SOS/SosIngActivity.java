package com.example.firstprogram.SOS;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.example.Class.GetAllParams;
import com.example.Class.Person;
import com.example.evaluate.EvaluateMainActivity;
import com.example.firstprogram.FirstActivity;
import com.example.firstprogram.R;
import com.example.firstprogram.TopBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class SosIngActivity extends Activity {
    private MapView mMapView = null;
    private TopBar topbar;
    private String url;
    private int event_id;
    private Person person;
    private GetAllParams c;
    private JSONObject answer;
    //private JSONObject obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.sos_ing_activity);
        //初始化
        url = "http://120.24.208.130:1503/event/modify";
        person = (Person) getApplication();
        Intent intent = getIntent();  //获取上一个activity的intent里的事件ID“event_id"
        event_id =  intent.getIntExtra("event_id",-1);

        mMapView = (MapView) findViewById(R.id.bmapView);
        //setOnTopbarClickListener
        topbar = (TopBar) findViewById(R.id.topbar_in_sosing);
        topbar.setOnTopbarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {//点击中止求救
                AlertDialog.Builder dialog = new AlertDialog.Builder(SosIngActivity.this);
                dialog.setMessage("确认中止求救吗？您将会被扣除1个积分！");
                dialog.setTitle("提示");
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                         /////////////////////////////////////////////////
                        //向后台发送 中止求救  请求，把事件状态改为 “1”--结束状态
                        JSONObject map = new JSONObject();
                        c = new GetAllParams(SosIngActivity.this);
                        JSONObject temp = new JSONObject();
                        try {
                            map.put("id", person.getId());//用户的id
                            map.put("event_id",event_id);//事件的id
                            map.put("state",1);//事件的状态，0是进行中，1是结束状态
                            map.put("type",0);//操作类型，0是中止求救，1是完成求救
                            //下面是向后台发送请求
                            temp = c.getList(url, map, new GetAllParams.VolleyJsonCallback() {
                                @Override
                                public void onSuccess(JSONObject result) {
                                    //answer = result;
                                    Toast.makeText(SosIngActivity.this, "求救已中止", Toast.LENGTH_SHORT).show();
                                    SosIngActivity.this.finish();//结束”求救中“页面
                                    Intent intent = new Intent(SosIngActivity.this, FirstActivity.class);
                                    startActivity(intent);//跳转到下一页
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                dialog.create().show();
            }

            @Override
            public void rightClick() throws UnsupportedEncodingException {
                Toast.makeText(SosIngActivity.this, " next UI", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder dialog = new AlertDialog.Builder(SosIngActivity.this);
                dialog.setMessage("确认完成求救吗？");
                dialog.setTitle("提示");
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

///////////////////////////////////////////////////////////////////////
// 向后台发送 完成求救  请求，把事件状态改为 “1”--结束状态
                        //跟上面差不多
                        JSONObject map = new JSONObject();
                        c = new GetAllParams(SosIngActivity.this);
                        JSONObject temp = new JSONObject();
                        answer = new JSONObject();
                        try {
                            map.put("id", person.getId());
                            map.put("event_id",event_id);
                            map.put("state",1);
                            map.put("type",1);

                            temp = c.getList(url, map, new GetAllParams.VolleyJsonCallback() {
                                @Override
                                public void onSuccess(JSONObject result) {
                                    answer = result;
                                    Toast.makeText(SosIngActivity.this, "求救已完成", Toast.LENGTH_SHORT).show();
                                    //SosIngActivity.this.finish();//结束”求救中“页面
                                    Intent intent = new Intent(SosIngActivity.this, EvaluateMainActivity.class);
                                    intent.putExtra("event_id", event_id);
                                    startActivity(intent);//跳转到下一页
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                dialog.create().show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
