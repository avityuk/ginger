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

package com.vityuk.ginger.spring;

import com.vityuk.ginger.Localizable;
import com.vityuk.ginger.Localization;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * This {@code FactoryBean} creates instance of {@link Localizable} bean defined by {@link #setLocalizable(Class)}
 * property.
 * See {@link Localizable} docs for details.
 *
 * @author Andriy Vityuk
 */
public class LocalizableFactoryBean<T extends Localizable> implements FactoryBean<T>, InitializingBean {
    private Localization<Localizable> localization;
    private Class<T> localizable;

    @Override
    public T getObject() throws Exception {
        return localization.getLocalizable(localizable);
    }

    @Override
    public Class<T> getObjectType() {
        return localizable;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setLocalization(Localization<Localizable> localization) {
        this.localization = localization;
    }

    public void setLocalizable(Class<T> localizable) {
        this.localizable = localizable;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (localization == null) {
            throw new IllegalArgumentException("Property 'localization' must be not null.");
        }
        if (localizable == null) {
            throw new IllegalArgumentException("Property 'localizable' must be not null.");
        }
    }
}
