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

package com.vityuk.ginger.loader;

import com.google.common.base.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 * @author Andriy Vityuk
 */
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
