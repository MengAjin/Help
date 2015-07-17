package com.example.firstprogram;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class SoSActivity1 extends Activity {
    private TextView countSecond;
    private TimeCount time;
    private Button CancelSos;
    private ImageView safety, health;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sos_activity1);

        CancelSos = (Button) findViewById(R.id.sos1Cancel);
        CancelSos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time.cancel();
                SoSActivity1.this.finish();
            }
        });

        safety = (ImageView) findViewById(R.id.sos1SafeView);
        safety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time.cancel();
                Intent intent = new Intent(SoSActivity1.this, SosSafeActivity.class);
                startActivity(intent);
            }
        });
        health = (ImageView) findViewById(R.id.sos1HealthView);
        health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time.cancel();
                Intent intent = new Intent(SoSActivity1.this, SosHealthActivity.class);
                startActivity(intent);
            }
        });

        //Time
        time = new TimeCount(6000, 1000);
        countSecond = (TextView) findViewById(R.id.sosSecond1);
        time.start();

        countSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SoSActivity1.this, " TextView Click Success", Toast.LENGTH_SHORT).show();
            }
        });


    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            countSecond.setText("0");
            countSecond.setClickable(true);
            Intent intent = new Intent(SoSActivity1.this, SosIngActivity.class);
            startActivity(intent);
        }
        @Override
        public void onTick(long millisUntilFinished){//计时过程显示
            countSecond.setClickable(false);
            countSecond.setText(millisUntilFinished /1000+" 秒后发送默认求救信息！");
        }
    }
}

