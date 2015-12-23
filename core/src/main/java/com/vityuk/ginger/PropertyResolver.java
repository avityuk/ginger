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

package com.vityuk.ginger;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementations resolve properties values of different types.
 *
 * @author Andriy Vityuk
 */
public interface PropertyResolver {
    /**
     * Get simple {@code String} property value.
     *
     * @param key property key, must be not {@code null}
     * @return resolved {@code String} value or {@code null} if value for key was not found
     */
    String getString(String key);

    /**
     * Get simple {@code Boolean} property value.
     *
     * @param key property key, must be not {@code null}
     * @return resolved {@code String} value or {@code null} if value for key was not found
     */
    Boolean getBoolean(String key);

    /**
     * Get simple {@code Integer} property value.
     *
     * @param key property key, must be not {@code null}
     * @return resolved {@code String} value or {@code null} if value for key was not found
     */
    Integer getInteger(String key);

    /**
     * Get simple {@code Long} property value.
     *
     * @param key property key, must be not {@code null}
     * @return resolved {@code String} value or {@code null} if value for key was not found
     */
    Long getLong(String key);

    /**
     * Get simple {@code Float} property value.
     *
     * @param key property key, must be not {@code null}
     * @return resolved {@code String} value or {@code null} if value for key was not found
     */
    Float getFloat(String key);

    /**
     * Get simple {@code Double} property value.
     *
     * @param key property key, must be not {@code null}
     * @return resolved {@code String} value or {@code null} if value for key was not found
     */
    Double getDouble(String key);

    /**
     * Get {@code String} {@code List} property value.
     *
     * @param key property key, must be not {@code null}
     * @return resolved {@code List<String>} value or {@code null} if value for key was not found
     */
    List<String> getStringList(String key);

    /**
     * Get {@code String} {@code Map} property value.
     *
     * @param key property key, must be not {@code null}
     * @return resolved {@code Map<String, String>} value or {@code null} if value for key was not found
     */
    Map<String, String> getStringMap(String key);

    /**
     * Get all keys.
     * @return {@code String} list of all keys()
     */
    Set<String> getKeys();
}
