package com.vityuk.ginger.loader;

import com.google.common.base.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public class ChainedResourceLoader implements ResourceLoader {
    private final Collection<ResourceLoader> resourceLoaders;

    public ChainedResourceLoader(Collection<ResourceLoader> resourceLoaders) {
        this.resourceLoaders = resourceLoaders;
    }

    @Override
    public boolean isSupported(String location) {
        return getSupportedResourceLoader(location) != null;
    }

    @Override
    public InputStream openStream(String location) throws IOException {
        ResourceLoader resourceLoader = getSupportedResourceLoader(location);
        Preconditions.checkArgument(resourceLoader != null, "Unsupported location: '" + location + "'");
        return resourceLoader.openStream(location);
    }

    private ResourceLoader getSupportedResourceLoader(String location) {
        for (ResourceLoader resourceLoader : resourceLoaders) {
            if (resourceLoader.isSupported(location)) {
                return resourceLoader;
            }
        }
        return null;
    }
}
