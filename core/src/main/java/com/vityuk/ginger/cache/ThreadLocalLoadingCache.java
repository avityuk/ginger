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

package com.vityuk.ginger.cache;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.vityuk.ginger.util.Preconditions.checkArgument;
import static com.vityuk.ginger.util.Preconditions.checkNotNull;

/**
 * This is basic {@link LoadingCache} implementation with thread local storage. It can be useful for caching
 * non-thread safe resources.
 * <p/>
 *
 * @param <K> - type of cache key
 * @param <V> - type of cached value
 * @author Andriy Vityuk
 */
public abstract class ThreadLocalLoadingCache<K, V> extends AbstractLoadingCache<K, V> {
    private final ThreadLocal<Map<K, Object>> threadLocalCache = new ThreadLocal<Map<K, Object>>() {
        @Override
        protected Map<K, Object> initialValue() {
            return new HashMap<K, Object>();
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
    public static <K, V> ThreadLocalLoadingCache<K, V> create(CacheLoader<K, V> cacheLoader, long duration, TimeUnit unit) {
        checkNotNull(cacheLoader);
        checkArgument(duration >= 0, "duration cannot be negative: %s %s", duration, unit);
        return new ExpireableThreadLocalLoadingCache<K, V>(cacheLoader, unit.toNanos(duration));
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
        private final long expireInNanos;

        public ExpireableThreadLocalLoadingCache(CacheLoader<K, V> cacheLoader, long expireInNanos) {
            super(cacheLoader);
            this.expireInNanos = expireInNanos;
        }

        @Override
        protected void storeInCache(Map<K, Object> cache, K key, V value) {
            ExpireableValue<V> expireableValue = new ExpireableValue<V>(value, expireInNanos);
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

}
