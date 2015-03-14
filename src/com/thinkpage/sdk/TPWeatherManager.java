package com.thinkpage.sdk;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.net.Uri;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class TPWeatherManager 
{
	public class TPJsonHttpResponseHandler extends JsonHttpResponseHandler
	{
		private TPWeatherManagerDelegate _weatherManagerDelegate;
		private TPCity _city;
		public TPJsonHttpResponseHandler(TPWeatherManagerDelegate weatherManagerDelegate, TPCity city)
		{
			_weatherManagerDelegate = weatherManagerDelegate;
			_city = city;
		}
		
	    @Override
	    public void onSuccess(int statusCode, Header[] headers, JSONObject response)
	    {
	    	TPWeather weather = new TPWeather(response);
	        System.out.print(response);
	    	_weatherManagerDelegate.OnRequestSuccess(_city, weather);
	    }

	    @Override
	    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) 
	    {
	    	_weatherManagerDelegate.OnRequestFailure(_city, responseString);
	    	System.out.print(responseString);
	    }
	}
	public enum TPWeatherReportLanguage {kEnglish, kSimplifiedChinese, kTraditionalChinese};
	public enum TPTemperatureUnit {kCelsius, kFahrenheit};
	public enum TPAirQualitySource {kAQINone, kAQICity, kAQIAll};
	// ivars
	private String _accessCode;
	private AsyncHttpClient _httpClient;
	private TPWeatherManagerDelegate _delegate;
	private static String kAPISourceCode = "Android2.0.0";
	private static String kBaseThinkPageAPIURL = "https://api.thinkpage.cn/v2/weather/";
	
	public TPWeatherManager(String apiKey, TPWeatherManagerDelegate delegate)
	{
		_accessCode = apiKey;
		_httpClient = new AsyncHttpClient();
		_delegate = delegate;
	}
/*	
	private Uri.Builder URIBuilderWithBasePath()
	{
		//http://api.thinkpage.cn/v1/weather/
		Uri.Builder builder = new Uri.Builder();
		return builder.scheme("http").authority("api.thinkpage.cn").appendPath("v1").appendPath("weather");
	}
	*/
	
	public void fetchCurrentWeather(TPCity city, TPWeatherReportLanguage language, TPTemperatureUnit unit)
	{
		RequestParams params = new RequestParams();
		params.put("city", city.description());
		params.put("language", _languageStringFromID(language));
		params.put("unit", _temperatureUnitStringFromID(unit));
		params.put("key", _accessCode);
		params.put("source", kAPISourceCode);
		_fetchDataFromServer(kBaseThinkPageAPIURL + "now.json", params, city);
	}
	
	public void fetchFutureWeather(TPCity city, TPWeatherReportLanguage language, TPTemperatureUnit unit)
	{
		RequestParams params = new RequestParams();
		params.put("city", city.description());
		params.put("language", _languageStringFromID(language));
		params.put("unit", _temperatureUnitStringFromID(unit));	
		params.put("key", _accessCode);
		params.put("source", kAPISourceCode);
		_fetchDataFromServer(kBaseThinkPageAPIURL + "future.json", params, city);
	}

	public void fetchAirQuality(TPCity city, TPWeatherReportLanguage language, TPTemperatureUnit unit, TPAirQualitySource aqi)
	{
		RequestParams params = new RequestParams();
		params.put("city", city.description());
		params.put("language", _languageStringFromID(language));
		params.put("unit", _temperatureUnitStringFromID(unit));
		params.put("aqi", _aqiStringFromID(aqi));
		params.put("key", _accessCode);
		params.put("source", kAPISourceCode);
		_fetchDataFromServer(kBaseThinkPageAPIURL + "air.json", params, city);
	}

	public void fetchWeatherSuggestions(TPCity city, TPWeatherReportLanguage language, TPTemperatureUnit unit)
	{
		RequestParams params = new RequestParams();
		params.put("city", city.description());
		params.put("language", _languageStringFromID(language));
		params.put("unit", _temperatureUnitStringFromID(unit));
		params.put("key", _accessCode);
		params.put("source", kAPISourceCode);
		_fetchDataFromServer(kBaseThinkPageAPIURL + "suggestion.json", params, city);
	}
	
	public void fetchAllWeather(TPCity city, TPWeatherReportLanguage language, TPTemperatureUnit unit, TPAirQualitySource aqi)
	{
		RequestParams params = new RequestParams();
		params.put("city", city.description());
		params.put("language", _languageStringFromID(language));
		params.put("unit", _temperatureUnitStringFromID(unit));	
		params.put("aqi", _aqiStringFromID(aqi));
		params.put("key", _accessCode);
		params.put("source", kAPISourceCode);
		_fetchDataFromServer(kBaseThinkPageAPIURL + "all.json", params, city);
	}
	
	private void _fetchDataFromServer(String urlStr, RequestParams params, TPCity forCity)
	{
		_httpClient.get(urlStr, params, new TPJsonHttpResponseHandler(_delegate, forCity));		
	}
	
	private String _languageStringFromID(TPWeatherReportLanguage languageid)
	{
	    switch (languageid)
	    {
	        case kEnglish:
	            return "en";
	        case kSimplifiedChinese:
	            return "zh-chs";
	        case kTraditionalChinese:
	            return "zh-cht";
	        default:
	            return "zh-chs";
	    }
	}
	private String _temperatureUnitStringFromID(TPTemperatureUnit unitid)
	{
	    switch (unitid)
	    {
	        case kCelsius:
	            return "c";
	        case kFahrenheit:
	            return "f";
	        default:
	            return "c";
	    }
	}

	private String _aqiStringFromID(TPAirQualitySource aqi)
	{
	    switch (aqi)
	    {
	        case kAQINone:
	            return "";
	        case kAQICity:
	            return "city";
	        case kAQIAll:
	            return "all";
	        default:
	            return "";
	    }
	}	
}
