package com.example.me;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firstprogram.R;
import com.example.log.LogInActivity;
import com.example.main.MainActivity;

public class SetUp extends Activity {
    private TextView log_out;
    private SharedPreferences sp;

    private void init() {
        log_out = (TextView) findViewById(R.id.log_out);

    }
    private class log_out_listener implements  View.OnClickListener{
        public void onClick(View v) {
            Toast.makeText(SetUp.this, "成功退出登录", Toast.LENGTH_SHORT).show();
            sp = getSharedPreferences("userInfo",0);
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(SetUp.this, LogInActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            MainActivity.instance.finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_set_up);
        init();
        log_out.setOnClickListener(new log_out_listener());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
