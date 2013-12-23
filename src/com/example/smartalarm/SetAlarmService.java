package com.example.smartalarm;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;

public class SetAlarmService extends IntentService{
	
	public SetAlarmService() {
		super("SetAlarmService");
	}
	
	@Override
	public void onHandleIntent(Intent i) {
		// Gathers information from the intent.
		String time[] = i.getStringExtra("time").split(":");
		String location = i.getStringExtra("location");
		String province = i.getStringExtra("province");
		String country = i.getStringExtra("country");
		String city = i.getStringExtra("city");
		Double latitude = i.getDoubleExtra("latitude", 0);
		Double longitude = i.getDoubleExtra("longitude", 0);
			
		JSONHelper helper = new JSONHelper();
		String url = "http://api.wunderground.com/api/b97e7f4651afa5fe/conditions/q/"+ latitude + "," + longitude + ".json";
		String icon = helper.getWeatherIcon(url);
			
		int walkingTime = helper.getWalkingTime(latitude, longitude, location);
		int drivingTime = helper.getDrivingTime(latitude, longitude, location);
		int bikingTime = helper.getBikingTime(latitude, longitude, location);
		int finalTime = 0;
		if (icon.equals("rain") || icon.equals("thunderstorm") || icon.equals("chanceofrain")) {
			String method = getApplicationContext().getSharedPreferences("com.example.smartalarm", Context.MODE_PRIVATE).getString("rain", "");
			if (method.equals("Drive")) {
				finalTime = drivingTime;
			} else if (method.equals("Walk")) {
				finalTime = walkingTime;
			} else if (method.equals("Bike")) {
				finalTime = bikingTime;
			}
		} else if (icon.equals("chanceofsnow") || icon.equals("chanceofsleet") || icon.equals("chanceofflurries") || icon.equals("flurries") || icon.equals("sleet") || icon.equals("snow")) {
			String method = getApplicationContext().getSharedPreferences("com.example.smartalarm", Context.MODE_PRIVATE).getString("snow", "");
			if (method.equals("Drive")) {
				finalTime = drivingTime;
			} else if (method.equals("Walk")) {
				finalTime = walkingTime;
			} else if (method.equals("Bike")) {
				finalTime = bikingTime;
			}
		} else {
			String method = getApplicationContext().getSharedPreferences("com.example.smartalarm", Context.MODE_PRIVATE).getString("clear", "");
			if (method.equals("Drive")) {
				finalTime = drivingTime;
			} else if (method.equals("Walk")) {
				finalTime = walkingTime;
			} else if (method.equals("Bike")) {
				finalTime = bikingTime;
			}
		}
		finalTime += getApplicationContext().getSharedPreferences("com.example.smartalarm", Context.MODE_PRIVATE).getInt("offset", 0);
		int alarmMinutes = Integer.parseInt(time[1]);
		int alarmHours = Integer.parseInt(time[0]);
				
		int prepHours = finalTime / 3600;
		finalTime %= 3600;
		int prepMinutes = finalTime / 60;
			
		alarmHours -= prepHours;
		if (prepMinutes > alarmMinutes) {
			prepMinutes -= alarmMinutes;
			alarmMinutes = 60;
			alarmMinutes -= prepMinutes;
			alarmHours--;
		} else {
			alarmMinutes -= prepMinutes;
		}
		// Sets an alarm for the given time with the location as the description.
		Intent alarm = new Intent(AlarmClock.ACTION_SET_ALARM);
		alarm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		alarm.putExtra(AlarmClock.EXTRA_MESSAGE, location);
		alarm.putExtra(AlarmClock.EXTRA_HOUR, alarmHours);
		alarm.putExtra(AlarmClock.EXTRA_MINUTES, alarmMinutes);
		alarm.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
		startActivity(alarm);
	}
}
