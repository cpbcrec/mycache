package com.concurrent.cache;

public interface MyCache<K,V>
{
	public void put(K key, V value);
	public V get(K key);
	public void remove(K key);
	public int size();
}
