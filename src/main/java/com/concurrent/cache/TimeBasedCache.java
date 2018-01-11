package com.concurrent.cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.concurrent.cache.event.notifier.EvictEventObserver;
import com.concurrent.cache.property.reader.ConfigReader;

/**
 * 
 * @author cprak3
 *
 * Uses ConcurrentHashMap data structure for time based cache impl
 *
 * @param <K>
 * @param <V>
 */

public class TimeBasedCache<K,V> implements MyCache<K, V> 
{

	private long allowedTimeToLive;
	private ConcurrentHashMap<K, V> cache;
	private EvictEventObserver<K, V> observer;
	
	public TimeBasedCache(long allowedTimeToLive)
	{
		this.allowedTimeToLive = allowedTimeToLive * 1000;

		cache = new ConcurrentHashMap<K, V>();
		
		boolean  isPushNotificationAllowed = Boolean.parseBoolean(ConfigReader.readProperty("push_notification_allowed"));
		
		if(isPushNotificationAllowed)
		{
			observer = new EvictEventObserver<K, V>(cache);
		}
		if (allowedTimeToLive > 0) 
		{
			Runnable r = () -> {
				while(true)
				{
					cleanup();
				}
				
			};
			r.run();
		}
	}

	private class CacheDataWrapper
	{
		public long whenAccessedLast = System.currentTimeMillis();
		public V value;

		private CacheDataWrapper(V value)
		{
			this.value = value;
		}

		@Override
		public String toString() 
		{
			return "CacheDataWrapper [lastAccessedTime=" + whenAccessedLast + ", value=" + value + "]";
		}
		
	}


	@SuppressWarnings("unchecked")
	public void put(K key, V value) 
	{
		CacheDataWrapper c = new CacheDataWrapper(value);
		c.whenAccessedLast = System.currentTimeMillis();
		cache.put(key, (V) c);
	}

	public V get(K key) 
	{
		@SuppressWarnings("unchecked")
		CacheDataWrapper c = (CacheDataWrapper) cache.get(key);

		if (c == null)
			return null;
		else 
		{
			c.whenAccessedLast = System.currentTimeMillis();
			return c.value;
		}
	}

	public void remove(K key) 
	{
		@SuppressWarnings("unchecked")
		CacheDataWrapper v=(TimeBasedCache<K, V>.CacheDataWrapper) cache.remove(key);
		if(observer!=null)
		{
			observer.notifyObservers(v.value);
		}
	}

	public int size() 
	{
		return cache.size();
	}

	@SuppressWarnings("unchecked")
	public void cleanup() 
	{
		long now = System.currentTimeMillis();
		ArrayList<K> keysToBeEvicted = new ArrayList<K>();

		Iterator<Entry<K, V>> itr = cache.entrySet().iterator();

		K key = null;
		CacheDataWrapper c = null;

		while (itr.hasNext()) 
		{
			Entry<K, V> entry = itr.next();
			key = (K) entry.getKey();
			c = (TimeBasedCache<K, V>.CacheDataWrapper) entry.getValue();
			if (c != null && (now > (allowedTimeToLive + c.whenAccessedLast)))
			{
				keysToBeEvicted.add(key);
			}
		}
		
		for (K k : keysToBeEvicted) {
			remove(k);
		}
	}
}
