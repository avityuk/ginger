package com.vityuk.ginger.util;

import com.google.common.cache.AbstractLoadingCache;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.google.common.base.Preconditions.checkNotNull;

//TODO: add expiration
public final class ThreadLocalLoadingCache<K, V> extends AbstractLoadingCache<K, V> {
    private final ThreadLocal<Map<K, V>> threadLocalCache = new ThreadLocal<Map<K, V>>() {
        @Override
        protected Map<K, V> initialValue() {
            return Maps.newHashMap();
        }
    };

    private final CacheLoader<K, V> cacheLoader;

    public ThreadLocalLoadingCache(CacheLoader<K, V> cacheLoader) {
        this.cacheLoader = checkNotNull(cacheLoader);
    }

    @Override
    public V get(K key) throws ExecutionException {
        Map<K, V> cache = getCache();
        V value = cache.get(checkNotNull(key));
        if (value != null) {
            return value;
        }

        V loadedValue;
        try {
            loadedValue = checkNotNull(cacheLoader.load(key));
        } catch (Exception e) {
            throw new ExecutionException(e);
        }
        cache.put(key, loadedValue);

        return loadedValue;
    }

    @Override
    public V getIfPresent(Object key) {
        return getCache().get(key);
    }

    @Override
    protected void finalize() throws Throwable {
        // Prevent memory leaks, otherwise it will stay attached to thread forever
        threadLocalCache.remove();
    }

    private Map<K, V> getCache() {
        return threadLocalCache.get();
    }
}
