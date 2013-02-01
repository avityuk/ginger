package com.vityuk.ginger;

import java.util.List;
import java.util.Map;

/**
 * Implementations resolve properties values of different types.
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
    List<String> getList(String key);

    /**
     * Get {@code String} {@code Map} property value.
     *
     * @param key property key, must be not {@code null}
     * @return resolved {@code Map<String, String>} value or {@code null} if value for key was not found
     */
    Map<String, String> getMap(String key);
}
