package com.thinkpage.sdk;
import android.location.Location;

public class TPCity 
{
	//ivars
	private String _name;
	private String _cityid;
	private Location _location;
	
	//functions
	public TPCity(String name)
	{
		_name = name;
		_cityid = null;
		_location = null;
	}
	
	public TPCity(String name, String cityid)
	{
		_name = name;
		_cityid = cityid;
		_location = null;		
	}
	
	public TPCity(Location location)
	{
		_name = null;
		_cityid = null;
		_location = location;		
	}
	
	public String getName()
	{
		return _name;
	}
	
	public String getCityID()
	{
		return _cityid;
	}
	
	public Location getLocation()
	{
		return _location;
	}
	
	public boolean equals(TPCity city)
	{
		if (_name.equals(city.getName()))
		{
			return true;
		}
		
		if (_cityid.equals(city.getCityID()))
		{
			return true;
		}
		
		if (_location.distanceTo(city.getLocation()) < 500/*meters*/)
		{
			return true;
		}
		return false;
	}
	
	public String description()
	{
		if (_name != null)
		{
			return _name;
		}
		else if (_cityid != null)
		{
			return _cityid;
		}
		return null;
	}
}
