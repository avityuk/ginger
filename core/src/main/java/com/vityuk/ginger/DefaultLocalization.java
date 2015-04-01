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

import com.vityuk.ginger.cache.CacheBuilder;
import com.vityuk.ginger.cache.CacheLoader;
import com.vityuk.ginger.cache.LoadingCache;
import com.vityuk.ginger.provider.LocalizationProvider;
import com.vityuk.ginger.proxy.ProxyBuilderFactory;
import com.vityuk.ginger.util.MiscUtils;

import static com.vityuk.ginger.util.Preconditions.checkArgument;
import static com.vityuk.ginger.util.Preconditions.checkNotNull;

/**
 * @author Andriy Vityuk
 */
public class DefaultLocalization extends AbstractDefaultLocalization<Localizable> {

    public DefaultLocalization(LocalizationProvider localizationProvider) {
        super(Localizable.class, localizationProvider);
    }
}
