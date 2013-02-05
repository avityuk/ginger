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

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ThreadLocalLoadingCacheTest {
    private static final long EXPIRATION_TIME = 100L;

    @Mock
    private CacheLoader<String, Integer> cacheLoader;

    @Test(expected = NullPointerException.class)
    public void testCreateWithNullLoader() throws Exception {
        ThreadLocalLoadingCache.create(null);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateExpireableWithNullLoader() throws Exception {
        ThreadLocalLoadingCache.create(null, EXPIRATION_TIME);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testCreateExpireableWithNegativeExpireInMilliseconds() throws Exception {
        ThreadLocalLoadingCache.create(cacheLoader, -1L);
    }

    @Test
    public void testDefaultCacheCanLoadValueWithLoader() throws Exception {
        LoadingCache<String, Integer> cache = createCache();
        String key = "105";
        Integer value = 105;
        when(cacheLoader.load(key)).thenReturn(value);

        Integer result = cache.get(key);

        assertThat(result).isNotNull().isEqualTo(value);
        verify(cacheLoader).load(key);
        verifyNoMoreInteractions(cacheLoader);
    }

    @Test
    public void testDefaultCacheCanCacheLoadedValue() throws Exception {
        LoadingCache<String, Integer> cache = createCache();
        String key = "105";
        Integer value = 105;
        when(cacheLoader.load(key)).thenReturn(value);

        cache.get(key);
        Integer result = cache.get(key);

        assertThat(result).isNotNull().isEqualTo(value);
        verify(cacheLoader).load(key);
        verifyNoMoreInteractions(cacheLoader);
    }

    @Test
    public void testExpireableCacheCanLoadValueWithLoader() throws Exception {
        LoadingCache<String, Integer> cache = createExpireableCache();
        String key = "105";
        Integer value = 105;
        when(cacheLoader.load(key)).thenReturn(value);

        Integer result = cache.get(key);

        assertThat(result).isNotNull().isEqualTo(value);
        verify(cacheLoader).load(key);
        verifyNoMoreInteractions(cacheLoader);
    }

    @Test
    public void testExpireableCacheCanCacheLoadedValue() throws Exception {
        LoadingCache<String, Integer> cache = createExpireableCache();
        String key = "105";
        Integer value = 105;
        when(cacheLoader.load(key)).thenReturn(value);

        cache.get(key);
        Integer result = cache.get(key);

        assertThat(result).isNotNull().isEqualTo(value);
        verify(cacheLoader).load(key);
        verifyNoMoreInteractions(cacheLoader);
    }

    @Test
    public void testExpireableCacheCanExpireCachedValue() throws Exception {
        LoadingCache<String, Integer> cache = createExpireableCache();
        String key = "105";
        Integer value = 105;
        when(cacheLoader.load(key)).thenReturn(value);

        cache.get(key);
        Thread.sleep(EXPIRATION_TIME);
        Integer result = cache.get(key);

        assertThat(result).isNotNull().isEqualTo(value);
        verify(cacheLoader, times(2)).load(key);
        verifyNoMoreInteractions(cacheLoader);
    }

    private ThreadLocalLoadingCache<String, Integer> createCache() {
        return ThreadLocalLoadingCache.create(cacheLoader);
    }

    private ThreadLocalLoadingCache<String, Integer> createExpireableCache() {
        return ThreadLocalLoadingCache.create(cacheLoader, EXPIRATION_TIME);
    }
}
