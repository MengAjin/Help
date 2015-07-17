package com.example.ask3;

import javax.security.auth.PrivateCredentialPermission;

import android.R.string;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.StaticLayout;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends Activity {
	EditText edt1 ;
	EditText edt2;
	private static final String[] str={"1km","2km","3km","ÆäËû"};
	private ArrayAdapter<String> arrayAdapter;
	private Spinner spinner;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		spinner = (Spinner) findViewById(R.id.spinner1);
		edt1 = (EditText)findViewById(R.id.editText1);
		edt2 = (EditText)findViewById(R.id.editText2);
		arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, str);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(arrayAdapter);
		
		findViewById(R.id.tijiao).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,ask.class);
				 String str1 = edt1.getText().toString();
				 String str2 = edt2.getText().toString();
				intent.putExtra("wenti",str1);
				intent.putExtra("neirong", str2);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
