package com.concurrent.cache;

import java.util.LinkedHashMap;

import com.concurrent.cache.event.notifier.EvictEventObserver;
import com.concurrent.cache.property.reader.ConfigReader;

/**
 * 
 * 
 * @author cprak3
 * 
 * Uses LinkedHashMap data structure for size based cache impl  
 *
 * @param <K>
 * @param <V>
 */
public class SizeBasedCache<K,V> implements MyCache<K, V>
{

	private LinkedHashMap<K, V> cache;  //n't thread safe, so while put & get operation, we will take a lock 
	private EvictEventObserver<K,V> observer; 
	
	private int maxSizeOfCache;

	public SizeBasedCache(int maxSizeOfCache)
	{
		this.maxSizeOfCache = maxSizeOfCache;
		cache = new LinkedHashMap<K, V>();
		
		boolean isPushNotificationAllowed = Boolean.parseBoolean(ConfigReader.readProperty("push_notification_allowed"));
		
		if(isPushNotificationAllowed)
		{
			observer = new EvictEventObserver<K, V>(cache);
		}
	}
	
	public void put(K key, V value) 
	{
		synchronized (cache) 
		{
			cache.put(key, value);
			
			if(cache.size() > maxSizeOfCache)
			{
				K keyToBeRemoved=cache.keySet().iterator().next();
				remove(keyToBeRemoved);
			}
		}
	}
	
	public V get(K key) 
	{
		synchronized (cache) 
		{
			V v = cache.get(key);

			if (v == null)
				return null;
			else 
			{
				return v;
			}
		}
	}
	
	public void remove(K key) 
	{
		synchronized (cache) 
		{
			V v = cache.remove(key);
			
			if(observer != null)
			{
				observer.notifyObservers(v);
			}
		}
	}
	
	public int size() 
	{
		synchronized (cache) 
		{
			return cache.size();
		}
	}
}
