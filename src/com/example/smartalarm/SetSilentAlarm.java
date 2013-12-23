package com.example.smartalarm;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class SetSilentAlarm extends IntentService implements LocationListener {
	private LocationManager locationManager;
	private static final long MIN_TIME = 1000;
	
	public SetSilentAlarm() {
		super("SetSilentAlarm");
	}
	
	/*@Override
	public void onCreate() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, 1, this);
	}*/
	
	@Override
	protected void onHandleIntent(Intent i) {		
		// Gathers information from the intent.
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, 1, this);
		
		String time[] = i.getStringExtra("time").split(":");
		String location = i.getStringExtra("location");
		
		JSONHelper helper = new JSONHelper();
		double latitude = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER).getLatitude();
		double longitude = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER).getLongitude();
		
		System.out.println("Lat: " + latitude);
		System.out.println("Long: " + longitude);
		
		int walkingTime = helper.getWalkingTime(latitude, longitude, location);
		int drivingTime = helper.getDrivingTime(latitude, longitude, location);
		int bikingTime = helper.getBikingTime(latitude, longitude, location);
		
		System.out.println("Walking: " + walkingTime);
		System.out.println("Biking: " + bikingTime);
		System.out.println("Driving: " + drivingTime);
		
		int longestTime = walkingTime;
		if (drivingTime > longestTime) drivingTime = longestTime;
		if (bikingTime > longestTime) bikingTime = longestTime;
		
		System.out.println("Longest: " + longestTime);
		
		// Add offset in seconds to longest time.
		longestTime += getApplicationContext().getSharedPreferences("com.example.smartalarm", Context.MODE_PRIVATE).getInt("offset", 0);
		
		// Send country, province, and city along with intent.
		String province = helper.getProvinceState(latitude, longitude);
		String city = helper.getCity(latitude, longitude);
		String country = helper.getCountry(latitude, longitude);
		
		Intent intent = new Intent(this, MyReceiver.class);
		intent.putExtra("location", location);
		intent.putExtra("province", province);
		intent.putExtra("city", city);
		intent.putExtra("country", country);
		intent.putExtra("latitude", latitude);
		intent.putExtra("longitude", longitude);
		intent.putExtra("time", time[0] + ":" + time[1]);
		// Start silent alarm.
		Calendar cal = Calendar.getInstance();
		PendingIntent sender = PendingIntent.getBroadcast(this, 192837, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() + 10000, sender);
	}	
	
	@Override
	public void onLocationChanged(Location location) { 
		double latitude = (double) (location.getLatitude());
		double longitude = (double) (location.getLongitude());
	}
	
	@Override 
	public void onProviderDisabled(String provider) {
		System.out.println("Disabled");
	}
	
	@Override 
	public void onProviderEnabled(String provider) {
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, 0, this);
	}
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		System.out.println(status);
	}
}
