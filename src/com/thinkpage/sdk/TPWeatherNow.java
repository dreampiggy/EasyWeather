package com.thinkpage.sdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TPWeatherNow 
{
	public TPWeatherNow(JSONObject jsonResponse)
	{
		try
		{
			text = jsonResponse.getString("text");
			code = jsonResponse.getString("code");
			temperature = jsonResponse.getInt("temperature");
			feelsLikeTemperature = jsonResponse.getInt("feels_like");
			windDirection = jsonResponse.getString("wind_direction");
			windSpeed = jsonResponse.getDouble("wind_speed");
			windScale = jsonResponse.getDouble("wind_scale");
			humidity = jsonResponse.getDouble("humidity");
			visibility = jsonResponse.getDouble("visibility");
			pressure = jsonResponse.getDouble("pressure");
			pressureRising = jsonResponse.getString("pressure_rising");
        }
        catch (final JSONException ex)
        {

        };

        try
        {
            //air quality if any
            JSONObject airQualitiesAll = jsonResponse.getJSONObject("air_quality");
            TPAirQuality cityAirQuality = new TPAirQuality(airQualitiesAll.getJSONObject("city"));
            JSONArray stationsAirQualityReport = airQualitiesAll.getJSONArray("stations");
            airQualities = new TPAirQuality[stationsAirQualityReport.length() + 1];
            airQualities[0] = cityAirQuality;
            for (int i = 1; i <= stationsAirQualityReport.length(); ++i)
            {
                airQualities[i] = new TPAirQuality(stationsAirQualityReport.getJSONObject(i - 1));
            }
        }catch (final JSONException ex)
        {

        };
	}
	public String text;

	/*!
	 *
	 * The weather code corresponding to description
	 **/
	public String code;

	/*!
	 *
	 * The temperature/feel like temperature of now
	 **/
	public int temperature;
	public int feelsLikeTemperature;

	/*!
	 *
	 * The wind direction/speed/scale
	 **/
	public String windDirection;
	public double windSpeed;
	public double windScale;

	/*!
	 *
	 * The humidity, visibility of now
	 **/
	public double humidity;
	public double visibility;

	/*!
	 *
	 * The air pressure and pressure rising of now
	 **/
	public double pressure;
	public String pressureRising;
	
	public TPAirQuality[] airQualities;
}
