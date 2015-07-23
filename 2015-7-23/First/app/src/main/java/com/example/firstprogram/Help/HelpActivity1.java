package com.example.firstprogram.Help;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firstprogram.R;
import com.example.firstprogram.TopBar;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;


public class HelpActivity1 extends Activity {

    private EditText dateText, titleText, personText, addressText, contentText;
    public void initialize(){
        dateText = (EditText) findViewById(R.id.helpDate);
        titleText = (EditText) findViewById(R.id.helpTitle);
        personText = (EditText) findViewById(R.id.helpNumOfPerson);
        addressText = (EditText) findViewById(R.id.helpAddress);
        contentText = (EditText) findViewById(R.id.helpContent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_activity1);
        initialize();

        TopBar topbar = (TopBar) findViewById(R.id.topBar);
        topbar.setOnTopbarClickListener(new TopBar.topbarClickListener() {
            @Override
            public void leftClick() {
                Toast.makeText(HelpActivity1.this, "leftbtn", Toast.LENGTH_SHORT).show();
                String a = String.valueOf(titleText.getText());
            }

            @Override
            public void rightClick() throws UnsupportedEncodingException {
                /*if(titleText.getText().toString()=="" || dateText.getText().toString()=="" ||
                        addressText.getText().toString() == "" || personText.getText().toString() == ""){
                    Toast.makeText(HelpActivity1. this, "ninhaiyoumeitianxiang", Toast.LENGTH_SHORT).show();
                } else if(Integer.parseInt(personText.getText().toString()) < 0 ){
                    //Toast.makeText(HelpActivity1. this, "suoxurenshugeshibudui", Toast.LENGTH_SHORT).show();
                }else */{
                    Intent intent = new Intent(HelpActivity1.this, HelpActivity2.class);

                    intent.putExtra("helpTitle", titleText.getText().toString());
                    intent.putExtra("helpDate", dateText.getText().toString());
                    intent.putExtra("helpNumOfPerson", personText.getText().toString());
                    intent.putExtra("helpAddress", addressText.getText().toString());
                    intent.putExtra("helpContent", contentText.getText().toString());

                    startActivity(intent);
                }

            }
        });

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the current system date
                SimpleDateFormat currentDate=new SimpleDateFormat("yyyy");
                int year= Integer.parseInt(currentDate.format(new java.util.Date()));
                currentDate=new SimpleDateFormat("MM");
                int mouth= Integer.parseInt(currentDate.format(new java.util.Date()));
                currentDate=new SimpleDateFormat("dd");
                int day= Integer.parseInt(currentDate.format(new java.util.Date()));
                //put out a date picker dialog
                final DatePickerDialog datePicker = new DatePickerDialog(HelpActivity1.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                monthOfYear += 1;
                                dateText.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
                                //Toast.makeText(HelpActivity1. this, year+"-"+monthOfYear+"-"+dayOfMonth, Toast.LENGTH_SHORT).show();
                            }
                        },year,mouth-1,day);
                datePicker.show();
            }
        });

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
