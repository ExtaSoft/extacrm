package ru.extas.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

/**
 * Фабрика для кеша пользователей
 * 
 * @author Valery Orlov
 * 
 */
public class MemcacheManager implements CacheManager {
	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		MemcacheService memcacheService = MemcacheServiceFactory.getMemcacheService(name);

		return new Memcache<K, V>(memcacheService);
	}
}