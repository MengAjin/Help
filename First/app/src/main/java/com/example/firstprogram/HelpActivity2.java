package com.example.firstprogram;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class HelpActivity2 extends Activity {

    private TextView displayText;
    private TopBar topbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_activity2);
        displayText = (TextView) findViewById(R.id.helpSuccessTextView);
        topbar = (TopBar) findViewById(R.id.topBar2);

        topbar.setRightBtnIsVisiable(false);

        Intent intent = getIntent();
        String helpTitle = intent.getStringExtra("helpTitle");
        String helpDate = intent.getStringExtra("helpDate");
        String helpNumOfPerson= intent.getStringExtra("helpNumOfPerson");
        String helpAddress= intent.getStringExtra("helpAddress");
        String helpContent= intent.getStringExtra("helpContent");

        displayText.setText("Title: " + helpTitle + "\n"
                + "Date: " + helpDate + "\n"
                + "Num of person: " + helpNumOfPerson + "\n"
                + "Address: " + helpAddress + "\n"
                + "Content: " + helpContent + "\n");

    }
}
