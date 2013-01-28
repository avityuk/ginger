package com.vityuk.ginger.loader;

import java.io.IOException;
import java.io.InputStream;

/**
 * Implementations provide resource loading functionality.
 */
public interface ResourceLoader {
    /**
     * Return {@code true} if implementation is able to handle loading for specified {@code location}.
     * It doesn't have to check presence of resource, but rather indicate if it can handle location protocol.
     *
     * @param location resource location, must be not {@code null}
     * @return {@code true} if can handle resource otherwise {@code false}
     */
    boolean isSupported(String location);

    /**
     * Open stream for specified {@code location} and return corresponding {@link InputStream}.
     *
     * @param location resource location, must be not {@code null}
     * @return input stream for opened resource or {@code null} of resource does not exist
     * @throws IOException when resource exists, but unable to open stream
     */
    InputStream openStream(String location) throws IOException;
}
