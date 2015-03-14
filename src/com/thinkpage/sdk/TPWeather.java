package com.thinkpage.sdk;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.ParseException;

public class TPWeather {
	TPWeather(JSONObject jsonResponse) 
	{
        try
        {
			status = jsonResponse.getString("status");
			JSONArray array = jsonResponse.getJSONArray("weather");
			if (array.length() > 0)
			    {
				// currently only handle the first city's weather
				JSONObject weatherObj = array.getJSONObject(0);
                try
                    {
                // get city name and last update
                    city = new TPCity(weatherObj.getString("city_name"), weatherObj.getString("city_id"));
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                    lastUpdate = formatter.parse(weatherObj.getString("last_update"));
                    }
                catch (java.text.ParseException e)
                    {

                    }
                catch (final JSONException ex)
                    {

                    };

                try
                    {
                    // get current weather
                    currentWeather = new TPWeatherNow(weatherObj.getJSONObject("now"));
                    }
                catch (final JSONException ex)
                    {

                    };

                try
                    {
                    // check and get airqualities and weather suggestions for current weather
                    if (currentWeather != null && currentWeather.airQualities != null) {
                        airQualities = currentWeather.airQualities.clone();
                    }

                    JSONObject todayInfo = weatherObj.getJSONObject("today");
                    sunriseTime = todayInfo.getString("sunrise");
                    sunsetTime = todayInfo.getString("sunset");
                    weatherSuggestions = new TPWeatherSuggestions(todayInfo.getJSONObject("suggestion"));
                    }
                catch (final JSONException ex)
                    {


                    };

                try
                    {
                    //may be the suggestion query
                    weatherSuggestions = new TPWeatherSuggestions(weatherObj.getJSONObject("suggestion"));
                    }
                catch (final JSONException ex)
                    {
                    };

                // future weather
                try
                    {
                    //get future weather
                    JSONArray futureWeatherArray = weatherObj.getJSONArray("future");
                    if (futureWeatherArray.length() > 0)
                        {
                        futureWeathers = new TPWeatherFuture[futureWeatherArray.length()];
                        for (int i = 0; i < futureWeatherArray.length(); ++i)
                            {
                            futureWeathers[i] = new TPWeatherFuture(futureWeatherArray.getJSONObject(i));
                            }
                        }
                    }
                catch (final JSONException ex)
                    {

                    };

                    // air quality if not set
                    if (airQualities == null) {
                        JSONObject airQualitiesAll = weatherObj.getJSONObject("air_quality");
                        TPAirQuality cityAirQuality = new TPAirQuality(airQualitiesAll.getJSONObject("city"));
                        JSONArray stationsAirQualityReport = airQualitiesAll.getJSONArray("stations");
                        airQualities = new TPAirQuality[stationsAirQualityReport.length() + 1];
                        airQualities[0] = cityAirQuality;
                        for (int i = 1; i <= stationsAirQualityReport.length(); ++i) {
                            airQualities[i] = new TPAirQuality(stationsAirQualityReport.getJSONObject(i - 1));
                        }
                    }

			}
	    }
		catch (final JSONException ex) 
		{
			
        };
		
	}

	boolean isValid()
	{
		return false;
	}

	public String status;

	/*!
	 the city this weather report belongs to
	  **/
	public TPCity city;

	/*!
	 the last update date/time of this weather report
	 **/
	public Date lastUpdate;

	/*!
	 current weather information
	 **/
	public TPWeatherNow currentWeather;

	/*!
	 air quality information. TPAirQuality in the NSArray
	 **/
	public TPAirQuality[] airQualities;

    public String sunriseTime;

    public String sunsetTime;

	/*!
	 weather suggestions
	 **/
	public TPWeatherSuggestions weatherSuggestions;

	/*!
	 future weather information, TPWeatherFuture in the NSArray
	 **/
	public TPWeatherFuture[] futureWeathers;
}
