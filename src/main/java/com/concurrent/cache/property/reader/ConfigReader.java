package com.concurrent.cache.property.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader 
{
	private static String propFileName = "config.properties";
	private static Properties properties = new Properties();
	
	static
	{		
		InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(propFileName);

		try
		{
				properties.load(inputStream);
		} 
		catch (IOException ex) 
		{
			throw new RuntimeException(ex.getMessage());
		}
	}
	
	public static String readProperty(String key)
	{
		return (String) properties.get(key);
	}
}
