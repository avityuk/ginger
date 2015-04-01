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


import java.util.concurrent.TimeUnit;

import static com.vityuk.ginger.util.Preconditions.checkArgument;
import static com.vityuk.ginger.util.Preconditions.checkState;

public class CacheBuilder<K, V> {
    static final int UNSET_INT = -1;

    long expireAfterWriteNanos = UNSET_INT;

    public CacheBuilder() {
    }

    public <K1 extends K, V1 extends V> LoadingCache<K1, V1>  build(CacheLoader<? super K1, V1> cacheLoader) {
        return new LocalCache.LocalLoadingCache<K1, V1>(this, cacheLoader);
    }

    public CacheBuilder<K, V> expireAfterWrite(long duration, TimeUnit unit) {
        checkState(expireAfterWriteNanos == UNSET_INT, "expireAfterWrite was already set to %s ns",
                expireAfterWriteNanos);
        checkArgument(duration >= 0, "duration cannot be negative: %s %s", duration, unit);
        this.expireAfterWriteNanos = unit.toNanos(duration);
        return this;
    }
}
