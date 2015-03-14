package com.thinkpage.sdk;

import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

public class TPWeatherSuggestions 
{
	public TPWeatherSuggestions(JSONObject jsonResponse)
	{
		try
		{
			dressingBrief = jsonResponse.getJSONObject("dressing").getString("brief");
			dressingDetails = jsonResponse.getJSONObject("dressing").getString("details");

			uvBrief = jsonResponse.getJSONObject("uv").getString("brief");
			uvDetails = jsonResponse.getJSONObject("uv").getString("details");
			
			carwashBrief = jsonResponse.getJSONObject("car_washing").getString("brief");
			carwashDetails = jsonResponse.getJSONObject("car_washing").getString("details");
			
			travelBrief = jsonResponse.getJSONObject("travel").getString("brief");
			travelDetails = jsonResponse.getJSONObject("travel").getString("details");
			
			fluBrief = jsonResponse.getJSONObject("flu").getString("brief");
			fluDetails = jsonResponse.getJSONObject("flu").getString("details");
	
			sportBrief = jsonResponse.getJSONObject("sport").getString("brief");
			sportDetails = jsonResponse.getJSONObject("sport").getString("details");
		}
		catch (final JSONException ex) 
		{
		
		};			
	}
	public String dressingBrief;
	public String dressingDetails;

	/*!
	 *
	 * The brief and details of the UV suggestion
	 **/
	public String uvBrief;
	public String uvDetails;

	/*!
	 *
	 * The brief and details of the car wash suggestion
	 **/
	public String carwashBrief;
	public String carwashDetails;

	/*!
	 *
	 * The brief and details of the travel suggestion
	 **/
	public String travelBrief;
	public String travelDetails;

	/*!
	 *
	 * The brief and details of the flu-prevention suggestion
	 **/
	public String fluBrief;
	public String fluDetails;

	/*!
	 *
	 * The brief and detailed suggestions for outdoor sports
	 **/
	public String sportBrief;
	public String sportDetails;
}
