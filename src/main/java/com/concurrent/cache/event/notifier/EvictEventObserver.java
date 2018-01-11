package com.concurrent.cache.event.notifier;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class EvictEventObserver<K,V> extends Observable
{
	private Map<K,V> cacheMap;
	
	public EvictEventObserver(Map<K, V> cacheMap) 
	{
		super();
		this.cacheMap = cacheMap;
	}

	@Override
	public void notifyObservers(Object arg) 
	{
		System.out.println(arg.toString() + " element removed");
	}
	
}
