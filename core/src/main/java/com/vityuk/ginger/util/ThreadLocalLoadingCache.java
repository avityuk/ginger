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
