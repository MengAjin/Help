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
import com.example.firstprogram.FirstActivity;
import com.example.firstprogram.R;
import com.example.firstprogram.TopBar;

import java.io.UnsupportedEncodingException;

public class SosIngActivity extends Activity {
    MapView mMapView = null;
    TopBar topbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.sos_ing_activity);

        mMapView = (MapView) findViewById(R.id.bmapView);
        //setOnTopbarClickListener
        topbar = (TopBar) findViewById(R.id.topbar_in_sosing);
        topbar.setOnTopbarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                AlertDialog.Builder dialog = new AlertDialog.Builder(SosIngActivity.this);
                dialog.setMessage("确认中止求救吗？您将会被扣除1个积分！");
                dialog.setTitle("提示");
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SosIngActivity.this.finish();
                        Intent intent = new Intent(SosIngActivity.this, FirstActivity.class);
                        startActivity(intent);
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
                        SosIngActivity.this.finish();
                        Intent intent = new Intent(SosIngActivity.this, SosDoneActivity.class);
                        startActivity(intent);
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
