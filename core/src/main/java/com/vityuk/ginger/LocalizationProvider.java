package com.vityuk.ginger;

import java.util.List;
import java.util.Map;

public interface LocalizationProvider {
    String getString(String key);

    Boolean getBoolean(String key);

    Integer getInteger(String key);

    Long getLong(String key);

    Float getFloat(String key);

    Double getDouble(String key);

    List<String> getStringList(String key);

    Map<String, String> getStringMap(String key);

    String getMessage(String key, Object... arguments);
}
