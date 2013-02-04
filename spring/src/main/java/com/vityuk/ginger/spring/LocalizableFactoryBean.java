package com.vityuk.ginger.spring;

import com.vityuk.ginger.Localizable;
import com.vityuk.ginger.Localization;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * This {@code FactoryBean} creates instance of {@link Localizable} bean defined by {@link #setLocalizable(Class)}
 * property.
 * See {@link Localizable} docs for details.
 */
public class LocalizableFactoryBean<T extends Localizable> implements FactoryBean<T>, InitializingBean {
    private Localization localization;
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

    public void setLocalization(Localization localization) {
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
