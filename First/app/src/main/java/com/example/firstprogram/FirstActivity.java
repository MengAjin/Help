package com.example.firstprogram;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


public class FirstActivity extends Activity{
    private ImageView helpImageView, sosImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.first_activity);

        helpImageView= (ImageView) findViewById(R.id.helpImageView);
        sosImageView= (ImageView) findViewById(R.id.sosImageView);

        helpImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, HelpActivity1.class);
                startActivity(intent);
            }
        });

        sosImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(FirstActivity.this, SoSActivity1.class);
                startActivity(intent);
                return false;
            }
        });
    }
}
