package com.thinkpage.sdk;

public interface TPWeatherManagerDelegate 
{
	void OnRequestSuccess(TPCity city, TPWeather report);
	void OnRequestFailure(TPCity city, String errorString);
}
