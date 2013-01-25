package com.vityuk.ginger.loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Loader for localization properties.
 * Reads localization properties from given {@code InputStream} and returns {@code Map} of localization property-value
 * pairs.
 * <p/>
 * Implementations should never close {@code InputStream} after reading the data.
 */
public interface LocalizationLoader {
    Map<String, String> load(InputStream inputStream) throws IOException;
}
