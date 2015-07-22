package com.example.firstprogram;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class HelpActivity2 extends Activity{
    private TextView content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_activity2);

        content = (TextView) findViewById(R.id.helpSuccessTextView);

        Intent intent = getIntent();
        String string1 = intent.getStringExtra("helpTitle");
        String string2 = intent.getStringExtra("helpDate");
        String string3 = intent.getStringExtra("helpNumOfPerson");
        String string4 = intent.getStringExtra("helpAddress");
        String string5 = intent.getStringExtra("helpContent");

        content.setText("Title:" + string1 + "\n" + "Date:" + string2 + "\n" +"Number of person:" + string3 + "\n"
        +"Address:" + string4 + "\n" +"Content:" + string5 + "\n" );
    }

}
