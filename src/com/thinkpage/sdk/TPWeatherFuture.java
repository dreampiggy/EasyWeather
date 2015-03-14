package com.thinkpage.sdk;

import java.text.SimpleDateFormat;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TPWeatherFuture {
	public TPWeatherFuture(JSONObject jsonResponse)
	{
		try
		{
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			text = jsonResponse.getString("text");
			code1 = jsonResponse.getString("code1");
            code2 = jsonResponse.getString("code2");
			day = jsonResponse.getString("day");
			date = formatter.parse(jsonResponse.getString("date"));
			temperatureHigh = jsonResponse.getInt("high");
			temperatureLow = jsonResponse.getInt("low");
			chanceOfPrecipitation = jsonResponse.getString("cop");
			wind = jsonResponse.getString("wind");
		}
		catch (java.text.ParseException e)
		{
			
		}
		catch (final JSONException ex) 
		{
		
		};		
	}
	/*!
	 *
	 * The general description of the weather (sunny, cloudy, rainning, etc.)
	 **/
	public String text;

	/*!
	 *
	 * The weather code corresponding to text
	 **/
	public String code1;

    public String code2;


	/*!
	 *
	 * What day is it
	 **/
	public String day;

	/*!
	 *
	 * What date is it
	 **/
	public Date date;

	/*!
	 *
	 * The high and low temperature of the day
	 **/
	public int temperatureHigh;
	public int temperatureLow;

	/*!
	 *
	 * Chance of Precipitation
	 **/
	public String chanceOfPrecipitation;

	/*!
	 *
	 * The wind forecast description
	 **/
	public String wind;
}
