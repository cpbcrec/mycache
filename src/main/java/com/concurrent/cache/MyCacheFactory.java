package com.concurrent.cache;

import com.concurrent.cache.enumeration.EvictionStrategy;
import com.concurrent.cache.property.reader.ConfigReader;

/**
 * 
 * Factory class for generating different cache strategy
 * 
 * @author cprak3
 *
 */
public class MyCacheFactory 
{
	public static <K, V> MyCache<K, V> getCacheType(EvictionStrategy strategy)
	{
		MyCache<K, V> cache = null;
		if(strategy.equals(EvictionStrategy.SIZE_BASED))
		{
			int cacheSize = Integer.parseInt(ConfigReader.readProperty("cache_size"));
			cache = new SizeBasedCache<K, V>(cacheSize);
		}
		else if(strategy.equals(EvictionStrategy.TIME_BASED))
		{
			long timeToLive = Long.parseLong(ConfigReader.readProperty("cache_time_to_live"));
			cache = new TimeBasedCache<K, V>(timeToLive);
		}
		return cache;
	}
}
