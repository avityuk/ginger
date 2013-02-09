/*
 * Copyright 2013 Andriy Vityuk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vityuk.ginger.util;

import com.google.common.cache.AbstractLoadingCache;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This is basic {@link LoadingCache} implementation with thread local storage. It can be useful for caching
 * non-thread safe resources.
 * <p/>
 * Method {@link #create(com.google.common.cache.CacheLoader)} creates cache instance with items which never expire.
 * Method {@link #create(com.google.common.cache.CacheLoader, long)} creates instance with expiration strategy.
 *
 * @param <K> - type of cache key
 * @param <V> - type of cached value
 * @author Andriy Vityuk
 */
public abstract class ThreadLocalLoadingCache<K, V> extends AbstractLoadingCache<K, V> {
    private final ThreadLocal<Map<K, Object>> threadLocalCache = new ThreadLocal<Map<K, Object>>() {
        @Override
        protected Map<K, Object> initialValue() {
            return Maps.newHashMap();
        }
    };

    private final CacheLoader<K, V> cacheLoader;

    /**
     * Create {@code ThreadLocalLoadingCache} instance.
     *
     * @param cacheLoader - cache loader, must be not null
     * @param <K>         - key type
     * @param <V>         - value type
     * @return cache instance
     */
    public static <K, V> ThreadLocalLoadingCache<K, V> create(CacheLoader<K, V> cacheLoader) {
        return new DefaultThreadLocalLoadingCache<K, V>(checkNotNull(cacheLoader));
    }

    /**
     * Create expireable {@code ThreadLocalLoadingCache} instance.
     *
     * @param cacheLoader          - cache loader, must be not null
     * @param expireInMilliseconds - amount of milliseconds after which entry considered expired
     * @param <K>                  - key type
     * @param <V>                  - value type
     * @return cache instance
     */
    public static <K, V> ThreadLocalLoadingCache<K, V> create(CacheLoader<K, V> cacheLoader,
                                                              long expireInMilliseconds) {
        checkNotNull(cacheLoader);
        checkArgument(expireInMilliseconds >= 0, "Parameter 'expireInMilliseconds' must be >= 0");
        return new ExpireableThreadLocalLoadingCache<K, V>(cacheLoader, expireInMilliseconds);
    }

    private ThreadLocalLoadingCache(CacheLoader<K, V> cacheLoader) {
        this.cacheLoader = checkNotNull(cacheLoader);
    }

    protected abstract void storeInCache(Map<K, Object> cache, K key, V value);

    protected abstract V getFromCache(Map<K, Object> cache, Object key);

    @Override
    public V get(K key) throws ExecutionException {
        Map<K, Object> cache = getCache();
        V value = getFromCache(cache, checkNotNull(key));
        if (value != null) {
            return value;
        }

        V loadedValue;
        try {
            loadedValue = checkNotNull(cacheLoader.load(key));
        } catch (Exception e) {
            throw new ExecutionException(e);
        }
        storeInCache(cache, key, loadedValue);

        return loadedValue;
    }

    @Override
    public V getIfPresent(Object key) {
        Map<K, Object> cache = getCache();
        return getFromCache(cache, key);
    }

    @Override
    protected void finalize() throws Throwable {
        // Prevent memory leaks, otherwise it will stay attached to thread forever
        threadLocalCache.remove();
    }

    private Map<K, Object> getCache() {
        return threadLocalCache.get();
    }

    private static class DefaultThreadLocalLoadingCache<K, V> extends ThreadLocalLoadingCache<K, V> {
        public DefaultThreadLocalLoadingCache(CacheLoader<K, V> cacheLoader) {
            super(cacheLoader);
        }

        @Override
        protected void storeInCache(Map<K, Object> cache, K key, V value) {
            cache.put(key, value);
        }

        @Override
        protected V getFromCache(Map<K, Object> cache, Object key) {
            @SuppressWarnings("unchecked")
            V value = (V) cache.get(key);
            return value;
        }
    }

    private static class ExpireableThreadLocalLoadingCache<K, V> extends ThreadLocalLoadingCache<K, V> {
        private final long expireInMillisec;

        public ExpireableThreadLocalLoadingCache(CacheLoader<K, V> cacheLoader, long expireInMillisec) {
            super(cacheLoader);
            this.expireInMillisec = expireInMillisec;
        }

        @Override
        protected void storeInCache(Map<K, Object> cache, K key, V value) {
            ExpireableValue<V> expireableValue = new ExpireableValue<V>(value, expireInMillisec);
            cache.put(key, expireableValue);
        }

        @Override
        protected V getFromCache(Map<K, Object> cache, Object key) {
            @SuppressWarnings("unchecked")
            ExpireableValue<V> expireableValue = (ExpireableValue<V>) cache.get(key);
            if (expireableValue == null) {
                return null;
            }
            if (expireableValue.isExpired()) {
                cache.remove(key);
                return null;
            }

            return expireableValue.getValue();
        }
    }

    private static class ExpireableValue<V> {
        private final V value;
        private final long expiresAfter;

        public ExpireableValue(V value, long expireInMillisec) {
            this.value = value;
            this.expiresAfter = System.currentTimeMillis() + expireInMillisec;
        }

        public V getValue() {
            return value;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expiresAfter;
        }
    }
}
