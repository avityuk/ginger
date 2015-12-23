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

import com.vityuk.ginger.loader.ChainedResourceLoader;
import com.vityuk.ginger.loader.ClasspathResourceLoader;
import com.vityuk.ginger.loader.FileSystemResourceLoader;
import com.vityuk.ginger.loader.LocalizationLoader;
import com.vityuk.ginger.loader.PropertiesLocalizationLoader;
import com.vityuk.ginger.loader.ResourceLoader;
import com.vityuk.ginger.locale.DefaultLocaleResolver;
import com.vityuk.ginger.provider.DefaultLocalizationProvider;
import com.vityuk.ginger.provider.LocalizationProvider;
import com.vityuk.ginger.provider.format.DefaultMessageFormatFactory;
import com.vityuk.ginger.provider.plural.DefaultPluralFormSelectorResolver;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.vityuk.ginger.util.Preconditions.checkArgument;
import static com.vityuk.ginger.util.Preconditions.checkNotNull;

/**
 * A builder of {@link Localization}.
 * <p>
 * It allows to set the following features:
 * <ul>
 * <li>{@code resourceLocation} or {@code resourceLocations} with the locations to localization resources,
 * required</li>
 * <li>{@link LocalizationLoader} for using specific resources format,
 * by default {@link PropertiesLocalizationLoader} used (supports Java properties files)</li>
 * <li>{@link LocaleResolver} for resolving current locale, by default {@link DefaultLocaleResolver} used</li>
 * <li>{@link ResourceLoader} for resolving current locale, by default {@link ClasspathResourceLoader} and
 * {@link FileSystemResourceLoader} are used</li>
 * <li>{@code MaxResourcesCachingTimeInSec} or {@code DisabledResourcesCaching} for manipulating resources caching
 * settings, by default cached resources never expire</li>
 * </ul>
 * </p>
 * <p>
 * Method {@link #build()} creates actual instance of {@code Localization}.
 * Example:
 * <blockquote><pre>
 * </pre></blockquote>
 * new LocalizationBuilder()
 * .withResourceLocations("MyResources.properties"))
 * .build();
 * More advanced example:
 * <blockquote><pre>
 *     new LocalizationBuilder()
 *             .withResourceLocations(Arrays.asList("MyMessages.properties", "MyConstants.properties"))
 *             .withLocalizationLoader(new PropertiesLocalizationLoader())
 *             .withLocaleResolver(new DefaultLocaleResolver())
 *             .withResourceLoader(new ClasspathResourceLoader())
 *             .withMaxResourcesCachingTimeInSec(60)
 *             .build();
 * </pre></blockquote>
 * </p>
 *
 * @author Andriy Vityuk
 */
public class LocalizationBuilder {
    private LocaleResolver localeResolver;
    private ResourceLoader resourceLoader;
    private LocalizationLoader localizationLoader;
    private List<String> resourceLocations = Collections.emptyList();
    private int maxResourcesCachingTimeInSec = -1;

    public LocalizationBuilder withLocaleResolver(LocaleResolver localeResolver) {
        this.localeResolver = checkNotNull(localeResolver, "'localeResolver' must be not null");
        return this;
    }

    public LocalizationBuilder withResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = checkNotNull(resourceLoader, "'resourceLoader' must be not null");
        return this;
    }

    public LocalizationBuilder withResourceLoaders(Collection<ResourceLoader> resourceLoaders) {
        checkNotNull(resourceLoaders, "'resourceLoaders' must be not null");
        checkArgument(!resourceLoaders.isEmpty(), "'resourceLoaders' must be not empty");
        resourceLoader = new ChainedResourceLoader(resourceLoaders);
        return this;
    }

    public LocalizationBuilder withLocalizationLoader(LocalizationLoader localizationLoader) {
        this.localizationLoader = checkNotNull(localizationLoader, "'localizationLoader' must be not null");
        return this;
    }

    public LocalizationBuilder withResourceLocations(List<String> resourceLocations) {
        checkNotNull(resourceLocations, "Parameter 'resourceLocations' must be not null");
        checkArgument(!resourceLocations.isEmpty(), "'resourceLocations' must be not empty");
        this.resourceLocations = resourceLocations;
        return this;
    }

    public LocalizationBuilder withResourceLocation(String resourceLocation) {
        checkNotNull(resourceLocation, "Parameter 'resourceLocation' must be not null");
        this.resourceLocations = Collections.singletonList(resourceLocation);
        return this;
    }

    public LocalizationBuilder withDisabledResourcesCaching() {
        maxResourcesCachingTimeInSec = 0;
        return this;
    }

    public LocalizationBuilder withMaxResourcesCachingTimeInSec(int maxResourcesCachingTimeInSec) {
        checkArgument(maxResourcesCachingTimeInSec > 0, "Parameter 'maxResourcesCachingTimeInSec' must be > 0");
        this.maxResourcesCachingTimeInSec = maxResourcesCachingTimeInSec;
        return this;
    }

    public Localization build() {
        checkArgument(!resourceLocations.isEmpty(), "Parameter 'resourceLocations' must be set");

        if (localeResolver == null) {
            localeResolver = defaultLocaleResolver();
        }

        if (resourceLoader == null) {
            resourceLoader = defaultResourceLoader();
        }

        if (localizationLoader == null) {
            localizationLoader = defaultLocalizationLoader();
        }

        LocalizationProvider localizationProvider = DefaultLocalizationProvider.builder()
                .withLocaleResolver(localeResolver)
                .withLocalizationLoader(localizationLoader)
                .withMaxCacheTimeInSec(maxResourcesCachingTimeInSec)
                .withLocations(resourceLocations)
                .withResourceLoader(resourceLoader)
                .withMessageFormatFactory(new DefaultMessageFormatFactory())
                .withPluralFormSelectorResolver(new DefaultPluralFormSelectorResolver())
                .build();

        return new DefaultLocalization(localizationProvider);
    }


    private DefaultLocaleResolver defaultLocaleResolver() {
        return new DefaultLocaleResolver();
    }

    private ResourceLoader defaultResourceLoader() {
        List<ResourceLoader> resourceLoaders = Arrays.<ResourceLoader>asList(
                new FileSystemResourceLoader(),
                new ClasspathResourceLoader());
        return new ChainedResourceLoader(resourceLoaders);
    }

    private PropertiesLocalizationLoader defaultLocalizationLoader() {
        return new PropertiesLocalizationLoader();
    }

    public static void main(String[] args) {
        new LocalizationBuilder()
                .withResourceLocations(Arrays.asList("MyMessages.properties", "MyConstants.properties"))
                .withLocalizationLoader(new PropertiesLocalizationLoader())
                .withLocaleResolver(new DefaultLocaleResolver())
                .withResourceLoader(new ClasspathResourceLoader())
                .withMaxResourcesCachingTimeInSec(60)
                .build();
    }
}
