package com.example.firstprogram.SOS;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.firstprogram.R;


public class SosSafeActivity extends Activity {
    private TextView countSecond;
    private TimeCount time;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sos_safe_activity);

        //Time
        time = new TimeCount(7000, 1000);
        countSecond = (TextView) findViewById(R.id.sos_safe_second);
        time.start();

        send = (Button) findViewById(R.id.sos_safe_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time.cancel();
                Intent intent = new Intent(SosSafeActivity.this, SosIngActivity.class);
                startActivity(intent);
            }
        });
    }

    // TimeCount
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onFinish() {
            //countSecond.setText("时间到");
            countSecond.setClickable(true);
            Intent intent = new Intent(SosSafeActivity.this, SosIngActivity.class);
            startActivity(intent);
        }
        @Override
        public void onTick(long millisUntilFinished){
            countSecond.setClickable(false);
            countSecond.setText(millisUntilFinished /1000+" ");
        }
    }
}
