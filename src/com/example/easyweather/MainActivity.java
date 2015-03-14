package com.example.easyweather;

import com.sqliteHelper.*;
import com.thinkpage.sdk.*;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.util.*;

public class MainActivity extends Activity implements TPWeatherManagerDelegate{
	TPWeatherManager _weatherManager = null;
    int _fetchType;
    DBHelper db;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window win = getWindow();  
        win.requestFeature(Window.FEATURE_LEFT_ICON);  
        setContentView(R.layout.activity_main);
        win.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.titleicon);
        db = new DBHelper(this);
        String defaultCity="";
        String defaultWeather="";
        String defaultTemperature="";
        defaultCity=db.loadDefault("city");
        defaultWeather=db.loadDefault("weather");
        defaultTemperature=db.loadDefault("temperature");
        if(defaultWeather.equals("wrong"))
        {
        	defaultCity="南京";
        }
        if(defaultWeather.equals("wrong"))
        {
        	defaultWeather="0";
        }
        if(defaultTemperature.equals("wrong"))
        {
        	defaultTemperature="25";
        }
        getWeatherImage(defaultWeather);
        final TextView temperatureText=(TextView)findViewById(R.id.temperatureCelsius);
        temperatureText.setText(defaultCity+"  "+defaultTemperature+"℃");
        _fetchType = 0;
        Toast.makeText(this, "欢迎使用迷你天气查询 ^_^", Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void onGetWeatherClick(View view)
    {
    	if (_weatherManager == null)
    	{
    		_weatherManager = new TPWeatherManager("2NQSNBNAP6", this);
    	}
       	EditText aText = (EditText) findViewById(R.id.editText1);
        String string = aText.getText().toString();
        switch (_fetchType) {
            case 0:
            _weatherManager.fetchAllWeather(new TPCity(string), TPWeatherManager.TPWeatherReportLanguage.kSimplifiedChinese, TPWeatherManager.TPTemperatureUnit.kCelsius, TPWeatherManager.TPAirQualitySource.kAQIAll);
            break;
            case 1:
            _weatherManager.fetchCurrentWeather(new TPCity(string), TPWeatherManager.TPWeatherReportLanguage.kSimplifiedChinese, TPWeatherManager.TPTemperatureUnit.kCelsius/*, TPWeatherManager.TPAirQualitySource.kAQIAll*/);
            break;
        }
    }
    
	public void OnRequestSuccess(TPCity city, TPWeather report)
	{
		if (report.city != null)
		{
			Toast.makeText(this, "成功获取" + city.description() + "天气信息", Toast.LENGTH_LONG).show();
			db.delete();
		}
		else
		{
			Toast.makeText(this, "获取" + city.description() + "的天气信息失败，请确认城市信息是否正确", Toast.LENGTH_LONG).show();
		}
        final android.widget.ListView listView = (android.widget.ListView) findViewById(R.id.listView);
        final ArrayList<String> list = new ArrayList<String>();
        final TextView temperatureText=(TextView)findViewById(R.id.temperatureCelsius);
        
        
        if (report.city != null)
            list.add("城市名: "+report.city.getName() + "ID:" + report.city.getCityID());
        if (report.sunsetTime != null)
            list.add("日出时间 : "+report.sunsetTime);
        if (report.sunriseTime != null)
            list.add("日落时间 : "+report.sunriseTime);
        if (report.currentWeather != null)
        {
            String weatherCode=report.currentWeather.code;
        	String weatherTemperature=String.valueOf(report.currentWeather.temperature);
        	String checkCity=report.city.getName();
        	getWeatherImage(weatherCode);
        	temperatureText.setText(checkCity+"  "+weatherTemperature+"℃");
            db.save(checkCity,weatherCode,weatherTemperature);//每次查询把结果写入数据库       	
            list.add("天气 : "+report.currentWeather.text);
            list.add("气温 : "+report.currentWeather.temperature+"℃");
            list.add("能见度 : "+report.currentWeather.visibility);
            list.add("湿度 : "+report.currentWeather.humidity);
            list.add("风速 : "+report.currentWeather.windSpeed);
            list.add("风力 : "+report.currentWeather.windScale);
            list.add("风向 : "+report.currentWeather.windDirection);
        }
        if (report.futureWeathers != null)
        {
            TPWeatherFuture futureWeather = report.futureWeathers[0];
            list.add("预报日期 : "+ futureWeather.date);
            list.add("白天 : "+ futureWeather.code1);
            list.add("夜间 : "+ futureWeather.code2);
            list.add("星期 : "+ futureWeather.day);
            list.add("天气描述 : "+ futureWeather.text);
            list.add("最高气温: "+ futureWeather.temperatureHigh);
            list.add("最低气温: "+ futureWeather.temperatureLow);
        }
        if (report.airQualities != null)
        {
            TPAirQuality airQuality = report.airQualities[0];
            list.add("PM10 : "+ airQuality.pm10);
            list.add("PM25 : "+ airQuality.pm25);
            list.add("AQI : "+ airQuality.aqi);
            list.add("CO : "+ airQuality.co);
            list.add("NO2 : "+ airQuality.no2);
            list.add("O3: "+ airQuality.o3);
            list.add("SO2: "+ airQuality.so2);
        }
        if (report.weatherSuggestions != null)
        {
            list.add("洗车指数:"+report.weatherSuggestions.carwashBrief + " 说明: " + report.weatherSuggestions.carwashDetails);
            list.add("穿衣指数:"+report.weatherSuggestions.dressingBrief + " 说明: " + report.weatherSuggestions.dressingDetails);
            list.add("感冒指数:"+report.weatherSuggestions.fluBrief + " 说明: " + report.weatherSuggestions.fluDetails);
            list.add("运动指数:"+report.weatherSuggestions.sportBrief + " 说明: " + report.weatherSuggestions.sportDetails);
            list.add("出行指数:"+report.weatherSuggestions.travelBrief);
        }
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(itemsAdapter);
	}
	
	public void OnRequestFailure(TPCity city, String errorString)
	{
		Log.v("wrong!!!!","what happend?");  
		Toast.makeText(this, "获取" + city.description() + "天气信息失败，请检查网络", Toast.LENGTH_LONG).show();
	}
	public void getWeatherImage(String code)
	{
		final android.widget.ImageView imageView = (android.widget.ImageView) findViewById(R.id.weatherImage);
		switch(code)
		{
		case "0":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather0); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "1":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather1); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "2":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather2); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "3":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather3); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "4":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather4); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "5":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather5); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "6":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather6); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "7":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather7); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "8":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather8); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "9":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather9); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "10":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather10); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "11":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather11); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "12":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather12); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "13":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather13); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "14":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather14); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "15":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather15); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "16":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather16); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "17":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather17); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "18":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather18); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "19":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather19); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "20":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather20); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "21":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather21); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "22":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather22); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "23":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather23); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "24":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather24); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "25":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather25); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "26":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather26); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "27":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather27); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "28":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather28); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "29":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather29); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "30":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather30); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "31":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather31); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "32":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather32); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "33":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather33); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "34":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather34); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "35":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather35); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "36":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather36); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "37":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather37); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "38":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather38); 
			imageView.setImageDrawable(drawable);
			break;
		}
		case "99":
		{
			Resources resources=getResources();
			Drawable drawable =resources.getDrawable(R.drawable.weather99); 
			imageView.setImageDrawable(drawable);
			break;
		}
		}
	}
    public void onCheckboxClick(View view)
    {
        // Is the view now checked?
        //boolean checked = ((android.widget.CheckBox) view).isChecked();
        android.widget.CheckBox aCheckbox1 = (android.widget.CheckBox) findViewById(R.id.checkBox1);
        android.widget.CheckBox aCheckbox2 = (android.widget.CheckBox) findViewById(R.id.checkBox2);
        // Check which checkbox was clicked
        aCheckbox1.setChecked(false);
        aCheckbox2.setChecked(false);

        switch(view.getId())
        {
            case R.id.checkBox1:
                aCheckbox1.setChecked(true);
                _fetchType = 0;
                break;
            case R.id.checkBox2:
                aCheckbox2.setChecked(true);
                _fetchType = 1;
                break;
        }
    }
}

