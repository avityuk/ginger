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

package com.vityuk.ginger.provider;

import com.google.common.base.Objects;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.io.Closeables;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.vityuk.ginger.LocaleResolver;
import com.vityuk.ginger.LocalizationProvider;
import com.vityuk.ginger.PropertyResolver;
import com.vityuk.ginger.loader.LocalizationLoader;
import com.vityuk.ginger.loader.PropertiesLocalizationLoader;
import com.vityuk.ginger.loader.ResourceLoader;
import com.vityuk.ginger.util.ThreadLocalLoadingCache;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Andriy Vityuk
 */
public class DefaultLocalizationProvider implements LocalizationProvider {
    public static final char LOCALE_SEPARATOR = '_';
    public static final char FILE_EXTENSION_SEPARATOR = '.';

    private final LocaleResolver localeResolver;
    private final ResourceLoader resourceLoader;
    private final LocalizationLoader localizationLoader;
    private final List<String> locations;

    private final LoadingCache<Locale, PropertyResolver> propertyResolverCache;
    private final LoadingCache<MessageKey, MessageFormat> messageFormatCache;

    private DefaultLocalizationProvider(Builder builder) {
        localeResolver = builder.localeResolver;
        resourceLoader = builder.resourceLoader;
        localizationLoader = builder.localizationLoader;
        locations = builder.locations;

        CacheBuilder<Object, Object> cacheBuilder = createCacheBuilder(builder);
        propertyResolverCache = cacheBuilder
                .build(new CacheLoader<Locale, PropertyResolver>() {
                    @Override
                    public PropertyResolver load(Locale locale) throws Exception {
                        return createPropertyResolver(locale);
                    }
                });

        messageFormatCache = new ThreadLocalLoadingCache<MessageKey, MessageFormat>(new CacheLoader<MessageKey, MessageFormat>() {
            @Override
            public MessageFormat load(MessageKey key) throws Exception {
                return createMessageFormat(key.getLocale(), key.getKey());
            }
        });
    }

    @Override
    public String getString(String key) {
        return getPropertyResolver().getString(checkNotNull(key));
    }

    @Override
    public Boolean getBoolean(String key) {
        return getPropertyResolver().getBoolean(checkNotNull(key));
    }

    @Override
    public Integer getInteger(String key) {
        return getPropertyResolver().getInteger(checkNotNull(key));
    }

    @Override
    public Long getLong(String key) {
        return getPropertyResolver().getLong(checkNotNull(key));
    }

    @Override
    public Float getFloat(String key) {
        return getPropertyResolver().getFloat(checkNotNull(key));
    }

    @Override
    public Double getDouble(String key) {
        return getPropertyResolver().getDouble(checkNotNull(key));
    }

    @Override
    public List<String> getStringList(String key) {
        return getPropertyResolver().getList(checkNotNull(key));
    }

    @Override
    public Map<String, String> getStringMap(String key) {
        return getPropertyResolver().getMap(checkNotNull(key));
    }

    @Override
    public String getMessage(String key, Object... arguments) {
        Locale locale = localeResolver.getLocale();
        MessageFormat messageFormat = getMessageFormat(locale, checkNotNull(key));

        return messageFormat.format(arguments);
    }

    private PropertyResolver getPropertyResolver() {
        Locale locale = localeResolver.getLocale();
        return getPropertyResolver(locale);
    }

    private PropertyResolver getPropertyResolver(Locale locale) {
        try {
            return propertyResolverCache.getUnchecked(locale);
        } catch (UncheckedExecutionException e) {
            throw Throwables.propagate(e.getCause());
        }
    }

    private MessageFormat getMessageFormat(Locale locale, String key) {
        try {
            return messageFormatCache.getUnchecked(new MessageKey(locale, key));
        } catch (UncheckedExecutionException e) {
            throw Throwables.propagate(e.getCause());
        }
    }

    private PropertyResolver createPropertyResolver(Locale locale) {
        List<PropertyResolver> propertyResolvers = Lists.newArrayListWithCapacity(locations.size());
        for (String location : locations) {
            propertyResolvers.add(createPropertyResolver(location, locale));
        }
        return createMultiPropertyResolver(propertyResolvers);
    }

    private MessageFormat createMessageFormat(Locale locale, String key) {
        String format = getPropertyResolver(locale).getString(key);
        return new MessageFormat(format, locale);
    }

    private PropertyResolver createPropertyResolver(String location, Locale locale) {
        InputStream inputStream = openLocation(location, locale);
        try {
            return loadLocalization(location, inputStream);
        } finally {
            Closeables.closeQuietly(inputStream);
        }
    }

    private InputStream openLocation(String location, Locale locale) {
        if (!resourceLoader.isSupported(location)) {
            throw new RuntimeException("Unsupported location: '" + location + "'");
        }
        return findResourceForLocale(location, locale);
    }

    private InputStream findResourceForLocale(String location, Locale locale) {
        int idx = location.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        if (idx == -1) {
            throw new RuntimeException("Localization resource should have extension in location: '" + location + "'");
        }
        String prefix = location.substring(0, idx);
        String suffix = location.substring(idx);

        for (Locale candidateLocale : createCandidateLocales(locale)) {
            String localizedLocation = createLocalizedLocation(prefix, suffix, candidateLocale);
            InputStream inputStream = openStream(localizedLocation);
            if (inputStream != null) {
                return inputStream;
            }
        }

        throw new RuntimeException("Unable to find localization resource: '" + location + "' for locale: '"
                + locale + "'");
    }

    private InputStream openStream(String location) {
        try {
            return resourceLoader.openStream(location);
        } catch (IOException e) {
            throw new RuntimeException("Unable to open location: '" + location + "'", e);
        }
    }

    private PropertyResolver loadLocalization(String location, InputStream inputStream) {
        try {
            return localizationLoader.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load localization resource: '" + location + "'", e);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private static CacheBuilder<Object, Object> createCacheBuilder(Builder builder) {
        CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder();
        if (builder.maxCacheTimeInSec >= 0) {
            cacheBuilder.expireAfterWrite(builder.maxCacheTimeInSec, TimeUnit.SECONDS);
        }
        return cacheBuilder;
    }

    private static List<Locale> createCandidateLocales(Locale locale) {
        List<Locale> locales = Lists.newArrayListWithCapacity(2);

        if (!locale.getCountry().isEmpty()) {
            locales.add(locale);
        }
        if (!locale.getLanguage().isEmpty()) {
            locales.add(locales.isEmpty() ? locale : new Locale(locale.getLanguage(), "", ""));
        }
        locales.add(Locale.ROOT);

        return locales;
    }

    private static String createLocalizedLocation(String locationPrefix, String locationSuffix, Locale locale) {
        StringBuilder builder = new StringBuilder(locationPrefix.length() + locationSuffix.length() + 6);

        builder.append(locationPrefix);
        if (!locale.getLanguage().isEmpty()) {
            builder.append(LOCALE_SEPARATOR);
            builder.append(locale.getLanguage());
        }
        if (!locale.getCountry().isEmpty()) {
            builder.append(LOCALE_SEPARATOR);
            builder.append(locale.getCountry());
        }
        builder.append(locationSuffix);

        return builder.toString();
    }

    private static PropertyResolver createMultiPropertyResolver(List<PropertyResolver> propertyResolvers) {
        if (propertyResolvers.size() == 1) {
            return propertyResolvers.get(0);
        }
        return new ChainedPropertyResolver(propertyResolvers);
    }

    public static class Builder {
        private LocaleResolver localeResolver;
        private ResourceLoader resourceLoader;
        private LocalizationLoader localizationLoader;
        private List<String> locations;
        private int maxCacheTimeInSec = -1;

        public Builder withLocaleResolver(LocaleResolver localeResolver) {
            this.localeResolver = localeResolver;
            return this;
        }

        public Builder withResourceLoader(ResourceLoader resourceLoader) {
            this.resourceLoader = resourceLoader;
            return this;
        }

        public Builder withLocalizationLoader(LocalizationLoader localizationLoader) {
            this.localizationLoader = localizationLoader;
            return this;
        }

        public Builder withLocations(List<String> locations) {
            this.locations = locations;
            return this;
        }

        public Builder withMaxCacheTimeInSec(int maxCacheTimeInSec) {
            this.maxCacheTimeInSec = maxCacheTimeInSec;
            return this;
        }

        public DefaultLocalizationProvider build() {
            return new DefaultLocalizationProvider(this);
        }
    }

    private static final class MessageKey {
        private final Locale locale;
        private final String key;

        public MessageKey(Locale locale, String key) {
            this.locale = locale;
            this.key = key;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(key, locale);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            MessageKey that = (MessageKey) o;
            return Objects.equal(key, that.key) && Objects.equal(locale, that.locale);
        }

        public Locale getLocale() {
            return locale;
        }

        public String getKey() {
            return key;
        }
    }
}
