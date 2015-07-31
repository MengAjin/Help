package com.example.helloword;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.helloword.map.LocationDemo;
import com.example.helloword.map.RoutePlanDemo;

/**
 * Created by Âþ¶¹»­Ñ¿ on 2015/7/30.
 */
public class aaaaMainActivity extends Activity {
    private Button route, location;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aaaa);

        route = (Button) findViewById(R.id.route);
        route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(aaaaMainActivity.this, RoutePlanDemo.class));
            }
        });
        location = (Button) findViewById(R.id.dingwei);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(aaaaMainActivity.this, LocationDemo.class));
            }
        });
    }
}
