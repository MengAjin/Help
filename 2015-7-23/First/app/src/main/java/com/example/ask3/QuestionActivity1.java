package com.example.ask3;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.firstprogram.R;
import com.example.firstprogram.TopBar;

import java.io.UnsupportedEncodingException;

public class QuestionActivity1 extends Activity {
	private EditText edt1 ;
	private EditText edt2;
	private static final String[] str={"1km","2km","3km","其他"};
	private ArrayAdapter<String> arrayAdapter;
	private Spinner spinner;
	private TopBar topbar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_activity1);
		spinner = (Spinner) findViewById(R.id.spinner1);
		edt1 = (EditText)findViewById(R.id.questionTitle);
		edt2 = (EditText)findViewById(R.id.questionContent);
		arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, str);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(arrayAdapter);

		//topbar
		topbar = (TopBar) findViewById(R.id.topBar_in_questionactivity1);
		topbar.setOnTopbarClickListener(new TopBar.topbarClickListener() {
			@Override
			public void leftClick() {
				finish();
			}

			@Override
			public void rightClick() throws UnsupportedEncodingException {
				Toast.makeText(QuestionActivity1.this, " next UI", Toast.LENGTH_SHORT).show();
			}
		});

	}



}
