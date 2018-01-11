package com.concurrent.cache.unit.testcases;

import org.junit.Test;

import com.concurrent.cache.MyCache;
import com.concurrent.cache.MyCacheFactory;
import com.concurrent.cache.enumeration.EvictionStrategy;
import com.concurrent.cache.property.reader.ConfigReader;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit; 
public class TestCase 
{
	@Test
	public void unitTestForSizeBasedCache()
	{
		MyCache<String, String> sizeCache = MyCacheFactory.getCacheType(EvictionStrategy.SIZE_BASED); 
		
		sizeCache.put("test1", "val1");
		sizeCache.put("test2", "val2");
		sizeCache.put("test3", "val3");
		sizeCache.put("test4", "val4");
		sizeCache.put("test5", "val5"); 
		
		assertSame(sizeCache.size(), Integer.parseInt(ConfigReader.readProperty("cache_size")));
		
	}
	
	@Test
	public void unitTestForTimeBasedCache()
	{
		long maxLifeTime = Long.parseLong(ConfigReader.readProperty("cache_time_to_live"))*1000;
		
		MyCache<String, String> timeCache = MyCacheFactory.getCacheType(EvictionStrategy.TIME_BASED);
		timeCache.put("test11", "val11");
		timeCache.put("test22", "val22");
		timeCache.put("test33", "val33");
		timeCache.put("test44", "val44");
		
		try 
		{
			TimeUnit.SECONDS.sleep(3);
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		if (maxLifeTime > 3)
		{
			assertSame(timeCache.size(), 4); 
		}
		else
		{
			assertSame(timeCache.size(), 0); 
		}
	}
}
