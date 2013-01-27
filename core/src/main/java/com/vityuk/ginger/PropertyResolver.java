package com.vityuk.ginger;

import java.util.List;
import java.util.Map;

/**
 * Implementations resolve properties values of different types.
 */
public interface PropertyResolver {
    /**
     * Get simple property value.
     *
     * @param key property key, must be not {@code null}
     * @return resolved {@code String} value or {@code null} if value for key was not found
     */
    String get(String key);

    /**
     * Get list property value.
     *
     * @param key property key, must be not {@code null}
     * @return resolved {@code List<String>} value or {@code null} if value for key was not found
     */
    List<String> getList(String key);

    /**
     * Get map property value.
     *
     * @param key property key, must be not {@code null}
     * @return resolved {@code Map<String, String>} value or {@code null} if value for key was not found
     */
    Map<String, String> getMap(String key);
}
