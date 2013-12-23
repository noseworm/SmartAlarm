package com.example.smartalarm;

import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

public class SetAlarm extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_alarm);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.set_alarm, menu);
		return true;
	}
	
	public void btnAddAlarm_Click(View view) {
		Intent i = new Intent(this, SetSilentAlarm.class);
		i.putExtra("location", ((EditText)findViewById(R.id.txtLocation)).getText().toString());
		TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
		String time = timePicker.getCurrentHour().toString() + ":" + timePicker.getCurrentMinute().toString();
		i.putExtra("time", time);	
		startService(i);
	}
	
	public void btnPreferences_Click(View view) {
		Intent i = new Intent(this, Preferences.class);
		startActivity(i);
	}
}
