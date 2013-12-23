package com.example.smartalarm;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class Preferences extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preferences);
		((EditText)findViewById(R.id.OffsetNumber)).setText("0");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.preferences, menu);
		return true;
	}
	
	public void btnSave_Click(View view) {
		SharedPreferences prefs = this.getSharedPreferences("com.example.smartalarm", Context.MODE_PRIVATE);
		Editor edit = prefs.edit();
		edit.putString("rain", ((Spinner)findViewById(R.id.rain_spinner)).getSelectedItem().toString());
		edit.putString("snow", ((Spinner)findViewById(R.id.snow_spinner)).getSelectedItem().toString());
		edit.putString("clear",((Spinner)findViewById(R.id.clear_spinner)).getSelectedItem().toString());
		edit.putInt("offset", Integer.parseInt(((EditText)findViewById(R.id.OffsetNumber)).getText().toString()) * 60);
		edit.commit();
		
		System.out.println(prefs.getString("rain", ""));
		System.out.println(prefs.getString("snow", ""));
		System.out.println(prefs.getString("clear", ""));
		System.out.println(prefs.getInt("offset", 0));
		
		Intent i = new Intent(this, SetAlarm.class);
		startActivity(i);
	}
	
	public void btnCancel_Click(View view) {
		Intent i = new Intent(this, SetAlarm.class);
		startActivity(i);
	}

}
