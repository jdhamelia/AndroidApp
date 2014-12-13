package com.jasmindhamelia.androidupmc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SuccessActivity extends Activity implements OnClickListener {
	
	Button goback;
	Button exit;
	TextView Message;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_success);
		
		
		Intent intent = getIntent();
        String successMessage =intent.getStringExtra(MainActivity.SUCCESS_MESSAGE);

        //display success message
        Message = new TextView(this);
        Message=(TextView)findViewById(R.id.textView1);
        Message.setText(successMessage);
        
        goback = new Button(this);
        goback = (Button)findViewById(R.id.button1);
        goback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				//Go to MainActivity
				Intent intent = new Intent(SuccessActivity.this, MainActivity.class);
	    	    startActivity(intent);
	    	    return;
				
			}
		});

        exit = new Button(this);
        exit = (Button)findViewById(R.id.button2);
        exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//closing all the activity and in the end closing first ectivity
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("EXIT", true);
				startActivity(intent);
				return;
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.success, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}


