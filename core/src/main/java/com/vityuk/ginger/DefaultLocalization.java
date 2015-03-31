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

import com.vityuk.ginger.cache.CacheLoader;
import com.vityuk.ginger.cache.LoadingCache;
import com.vityuk.ginger.provider.LocalizationProvider;
import com.vityuk.ginger.proxy.ProxyBuilderFactory;
import com.vityuk.ginger.cache.CacheBuilder;
import com.vityuk.ginger.util.MiscUtils;

import static com.vityuk.ginger.util.Preconditions.checkArgument;
import static com.vityuk.ginger.util.Preconditions.checkNotNull;

/**
 * @author Andriy Vityuk
 */
public class DefaultLocalization implements Localization {

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Localizable> T getLocalizable(Class<T> localizable) {
        checkNotNull(localizable);
        checkArgument(localizable.isInterface(), "Parameter 'localizable' must be an interface");
        checkArgument(Localizable.class.isAssignableFrom(localizable), "%s must extend %s",
                localizable.getName(), Localizable.class.getName());
        try {
            T localizableInstance = (T) localizableCache.getUnchecked((Class<Localizable>) localizable);
            return localizableInstance;
        } catch (Throwable e) {
            throw MiscUtils.propagate(e.getCause());
        }
    }

    protected final LocalizationProvider localizationProvider;

    private final LoadingCache<Class<Localizable>, Localizable> localizableCache;

    public DefaultLocalization(LocalizationProvider localizationProvider) {
        this.localizationProvider = localizationProvider;

        localizableCache = CacheBuilder.newBuilder().build(new CacheLoader<Class<Localizable>, Localizable>() {
            @Override
            public Localizable load(Class<Localizable> localizable) throws Exception {
                return createLocalizableInstance(localizable);
            }
        });
    }

    @Override
    public String getMessage(String key, Object... parameters) {
        return localizationProvider.getMessage(key, parameters);
    }

    @Override
    public String getSelectedMessage(String key, String selector, Object... parameters) {
        return localizationProvider.getSelectedMessage(key, selector, parameters);
    }

    @Override
    public String getPluralMessage(String key, int count, Object... parameters) {
        return localizationProvider.getPluralMessage(key, count, parameters);
    }

    protected  <T extends Localizable> T createLocalizableInstance(Class<T> localizable) {
        T instance = ProxyBuilderFactory.createProxy(localizable, localizationProvider);
        return instance;
    }
}
