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

class ExpireableValue<V> {
    private final V value;
    private final long expiresAfter;

    public ExpireableValue(V value, long expireInNanos) {
        this.value = value;
        this.expiresAfter = (expireInNanos >= 0)? System.currentTimeMillis() + expireInNanos / 1000000 : -1;
    }

    public V getValue() {
        return value;
    }

    public boolean isExpired() {
        if (expiresAfter == -1) {
            return false;
        }
        return System.currentTimeMillis() > expiresAfter;
    }
}
