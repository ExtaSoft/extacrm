package ru.extas.shiro;

import com.google.appengine.api.memcache.MemcacheService;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import java.util.Collection;
import java.util.Set;

/**
 * Кеширует информацию о пользователях
 *
 * @param <K>
 * @param <V>
 * @author Valery Orlov
 */
class Memcache<K, V> implements Cache<K, V> {

    private final MemcacheService memcacheService;

    Memcache(MemcacheService memcacheService) {
        this.memcacheService = memcacheService;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(K k) throws CacheException {
        return (V) memcacheService.get(k);
    }

    @Override
    public V put(K k, V v) throws CacheException {
        V oldValue = get(k);
        memcacheService.put(k, v);
        return oldValue;
    }

    @Override
    public V remove(K k) throws CacheException {
        V oldValue = get(k);
        memcacheService.delete(k);
        return oldValue;
    }

    @Override
    public void clear() throws CacheException {
        memcacheService.clearAll();
    }

    @Override
    public int size() {
        return (int) memcacheService.getStatistics().getItemCount();
    }

    @Override
    public Set<K> keys() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException();
    }
}
