package com.example.smartalarm;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;

public class GPS_Test extends Activity implements LocationListener {
	private LocationManager locationManager;
	private static final long MIN_TIME = 1000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gps__test);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, 1, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gps__test, menu);
		return true;
	}
	
	@Override
	public void onLocationChanged(Location location) { 
		double latitude = (double) (location.getLatitude());
		double longitude = (double) (location.getLongitude());
		System.out.println(latitude);
		System.out.println(longitude);
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
