package com.example.ask3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class ask extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ask);
		Intent intent = getIntent();
		String string1 = intent.getStringExtra("wenti");
		String string2 = intent.getStringExtra("neirong");
		TextView edt3 =(TextView)findViewById(R.id.deplay);
		edt3.setText("问题:"+string1+"\n"+"内容:"+string2);
		initEvent();
	}
		private void initEvent(){
			findViewById(R.id.quxiao).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					showDialog1();
				}
			});
		}
	  private void showDialog1() {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("是否中止提问");
				builder.setIcon(R.drawable.ic_launcher);
				builder.setPositiveButton("取消",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						
					}
				});
	         builder.setNegativeButton("确定",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						
					}
				});
			
    
	  
	   AlertDialog dialog = builder.create();
       dialog.show();
       }
}

