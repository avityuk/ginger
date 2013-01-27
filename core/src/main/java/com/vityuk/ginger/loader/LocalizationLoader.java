package com.vityuk.ginger.loader;

import com.vityuk.ginger.PropertyResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Loader for localization properties.
 * Reads localization properties from given {@code InputStream} and returns {@link PropertyResolver} with loaded
 * properties.
 * <p/>
 * Implementations should never close {@code InputStream} after reading the data.
 */
public interface LocalizationLoader {
    PropertyResolver load(InputStream inputStream) throws IOException;
}
