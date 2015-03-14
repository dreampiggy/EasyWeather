package com.thinkpage.sdk;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;

public class TPAirQuality 
{
	public TPAirQuality(JSONObject jsonResponse)
	{
		try
		{
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			aqi = jsonResponse.getDouble("aqi");
			pm25 = jsonResponse.getDouble("aqi");
			pm10 = jsonResponse.getDouble("pm10");
			so2 = jsonResponse.getDouble("so2");
			no2 = jsonResponse.getDouble("no2");
			co = jsonResponse.getDouble("co");
			o3 = jsonResponse.getDouble("o3");
			lastUpdate = formatter.parse(jsonResponse.getString("last_update"));
            quality = jsonResponse.getString("quality");
            stationName = jsonResponse.getString("station");
		}
		catch (java.text.ParseException e) 
		{
			
		}
		catch (final JSONException ex) 
		{
		
		};			
	}
	public String stationName;
	public double aqi;
	public double pm25;
	public double pm10;
	public double so2;
	public double no2;
	public double co;
	public double o3;
	public String quality;
	public Date lastUpdate;	
}
