package com.example.smartalarm;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.*;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class JSONHelper {
	
	public JSONObject getJSON(String url) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = null;
		
		if (url.contains("wunderground")) {
			httpGet = new HttpGet(url);
		} else {
			httpGet = new HttpGet(url.replaceAll(" ", "%20"));
		}
		try {
			HttpResponse response = client.execute(httpGet);
		    StatusLine statusLine = response.getStatusLine();
		    int statusCode = statusLine.getStatusCode();
		    if (statusCode == 200) {
		    	HttpEntity entity = response.getEntity();
		        InputStream content = entity.getContent();
		        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
		        String line;
		        while ((line = reader.readLine()) != null) {
		          builder.append(line);
		        }
		    }
		} catch (ClientProtocolException e) {
		   e.printStackTrace();
		} catch (IOException e) {
		   e.printStackTrace();
		}
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(builder.toString());
		} catch (Exception e) {
		    e.printStackTrace();
		}	
		return jsonObject;
	}
	
	public String getWeatherIcon(String url) {
		String icon = "";
		try {
			JSONObject jsonObj = getJSON(url);
			JSONObject currentObservations = jsonObj.getJSONObject("current_observation");
			icon = currentObservations.getString("icon");
			return icon;
		} catch (Exception e) {
			System.out.println("ERROR");
			return icon;
		}
	}
	
	private int getDuration(String url) {
		try {
			JSONObject jsonObj = getJSON(url);
			JSONArray jsonRoutes = jsonObj.getJSONArray("routes");
			JSONObject jsonFirst = jsonRoutes.getJSONObject(0);
			JSONArray jsonLegs = jsonFirst.getJSONArray("legs");
			JSONObject jsonOverall = jsonLegs.getJSONObject(0);
			JSONObject jsonDuration = jsonOverall.getJSONObject("duration");
			return jsonDuration.getInt("value");
		} catch (Exception e) {
			System.out.println(e.toString());
			System.out.println(e.getStackTrace());
			return -1;
		}
	}
	
	public int getDrivingTime(double latitude, double longitude, String destination) {
		String url = getUrl(latitude, longitude, destination, "driving");
		return getDuration(url);		
	}
	
	public int getWalkingTime(double latitude, double longitude, String destination) {
		String url = getUrl(latitude, longitude, destination, "walking");
		return getDuration(url);	
	}
	
	public int getBikingTime(double latitude, double longitude, String destination) {
		String url = getUrl(latitude, longitude, destination, "bicycling");
		return getDuration(url);	
	}
	
	private String getLocationDetail(String url, String level) {
		String value = "";
		try {
			JSONObject jsonObj = getJSON(url);
			JSONArray jsonAry = jsonObj.getJSONArray("results");
			JSONObject jsonFirst = jsonAry.getJSONObject(0);
			JSONArray jsonAddressComponents = jsonFirst.getJSONArray("address_components");
			for(int i = 0; i < jsonAddressComponents.length(); i++) {
				JSONObject component = jsonAddressComponents.getJSONObject(i);
				JSONArray types = component.getJSONArray("types");
				String type = types.getString(0);
				if (type.equals(level)) {
					value =  component.getString("short_name").toString();
				}
				
			}
			return value;
		} catch(Exception e) {
			System.out.println(e.getStackTrace());
			System.out.println(e.toString());
			return value;
		}
		
	}
	
	public String getProvinceState(double latitude, double longitude) {
		String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + Double.toString(latitude) + "," + Double.toString(longitude) +"&sensor=false";
		return getLocationDetail(url, "administrative_area_level_1");
		
	}
	
	public String getCountry(double latitude, double longitude) {
		String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + Double.toString(latitude) + "," + Double.toString(longitude) +"&sensor=false";
		return getLocationDetail(url, "country");
	}
	
	public String getCity(double latitude, double longitude) {
		String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + Double.toString(latitude) + "," + Double.toString(longitude) +"&sensor=false";
		return getLocationDetail(url, "locality");
	}
	
	private String getUrl(double latitude, double longtitude, String destination, String mode) {
		String base_url = "http://maps.googleapis.com/maps/api/directions/json?";
		String final_url = base_url +"origin="+Double.toString(latitude)+"," +Double.toString(longtitude)+"&destination="+destination+"&mode="+mode+"&sensor=true";
		return final_url;
	}
}
