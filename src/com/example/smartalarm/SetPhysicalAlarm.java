package com.example.smartalarm;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.AlarmClock;

public class SetPhysicalAlarm extends Service {	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void onStart(Intent i, int startId) {		
		Bundle bundle = i.getExtras();
		Intent intent = new Intent(this, SetAlarmService.class);
		intent.putExtras(bundle);
		startService(intent);
	}
}
