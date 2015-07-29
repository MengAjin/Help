package com.example.firstprogram;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.ask3.QuestionActivity1;
import com.example.firstprogram.Help.HelpActivity1;
import com.example.firstprogram.SOS.SoSActivity1;
import com.example.Class.Person;


public class FirstActivity extends Activity{
    Person person;
    private ImageView helpImageView, sosImageView, questionImageView;
    private BottomBar bottombar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.first_activity);
        //Log.d("TAG111111", String.valueOf(person.getGender()));


        helpImageView= (ImageView) findViewById(R.id.helpImageView);
        sosImageView= (ImageView) findViewById(R.id.sosImageView);
        questionImageView = (ImageView) findViewById(R.id.questionImageView);

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
        questionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, QuestionActivity1.class);
                startActivity(intent);
            }
        });



    }
}
