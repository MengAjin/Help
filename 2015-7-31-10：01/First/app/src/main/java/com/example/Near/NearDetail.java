package com.example.Near;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.Navigation.RoutePlanDemo;
import com.example.firstprogram.R;
import com.example.firstprogram.TopBar;

import java.io.UnsupportedEncodingException;

/**
 * Created by Âþ¶¹»­Ñ¿ on 2015/7/31.
 */
public class NearDetail extends Activity {
    private TopBar topbar;
    private int event_id;

    public void init() {
        topbar = (TopBar) findViewById(R.id.topbar);
        topbar.setOnTopbarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                Toast.makeText(NearDetail.this,"×óbtn",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void rightClick() throws UnsupportedEncodingException {
                Intent intent = new Intent(NearDetail.this, RoutePlanDemo.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.near_detail);
        //findviewbyid
        init();
    }
}
